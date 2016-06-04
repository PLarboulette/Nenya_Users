package amqp

import java.util.logging.Logger
import com.rabbitmq.client.{AMQP, _}
import play.api.libs.json.{JsValue, Json}
import services.UsersService

/**
  * Created by Pierre on 05/04/2016.
  */

object Functions {

  val logger : Logger = Logger.getLogger("Functions Logger")

  /**
    * Connect to RabbitMQ
    * @param host the host of the RabbitMQ instance
    * @return The Connection Object
    */
  def connect(host: String): Connection = {
    val factory: ConnectionFactory = new ConnectionFactory()
    factory.setHost(host)
    factory.newConnection()
  }

  /**
    * Create channel from a Connection Object
    * @param connection the connection Object
    * @return A new channel
    */
  def createChannel (connection : Connection) : Channel = {
    connection.createChannel()
  }

  /**
    * Receive messages
    * @param channel The Channel Object
    * @param exchange "users" | "projects" | ...
    * @param exchangeType "direct" | ...
    * @param routingKey "login" | "create_X" | ...
    */
  def receive(channel : Channel, exchange : String, exchangeType : String, routingKey : String) : Unit = {
    logger.info("[Connection to RabbitMQ on exchange : " +exchange+ " and with routing key : "+routingKey)

    val queue = channel.queueDeclare(routingKey, false, false, false, null).getQueue
    channel.exchangeDeclare(exchange, exchangeType)
    channel.queueBind(queue, exchange, routingKey)

    // Get messages from queue
    val consumer : DefaultConsumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]){
        val replyToProps = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build()
        val parsedMessage : JsValue  = Json.parse(fromBytes(body))

        logger.info("[Received Message from exchange : " +exchange+ " and for : "+routingKey+ " requests ]  / Data : " + parsedMessage)

        // Switch on the routing key and use the good function
        val valueToReturn : String = routingKey match {
          case "login" => UsersService.login(parsedMessage).toString()
          case "create_user" => UsersService.createUser(parsedMessage).toString()
          case "update_user" => UsersService.updateUser(parsedMessage).toString
          case "delete_user" => UsersService.deleteUser(parsedMessage).toString()
          case "get_user" => UsersService.getUser(parsedMessage).toString()
          case "get_users" => UsersService.getUsers(parsedMessage).toString()
          case _ => "You shouldn't pass ! "
        }
        channel.basicPublish( "", properties.getReplyTo, replyToProps, valueToReturn.getBytes())
        logger.info("[Published Message for : "+routingKey+ " requests ] / Data : " + valueToReturn)
      }
    }
    channel.basicConsume(queue,true, consumer)
  }

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
}