package amqp

import com.rabbitmq.client.{AMQP, _}
import play.api.libs.json.{JsValue, Json}
import services.UsersService


/**
  * Created by plarboul on 05/04/2016.
  */
object Functions {

  def connect(host: String): Connection = {
    val factory: ConnectionFactory = new ConnectionFactory()
    factory.setHost(host)
    factory.newConnection()
  }

  def createChannel (connection : Connection) : Channel = {
    connection.createChannel()
  }

  def receive (channel : Channel, exchange : String, exchangeType : String, routingKey : String) : Unit = {

    val queue = channel.queueDeclare().getQueue
    channel.exchangeDeclare(exchange, exchangeType)
    channel.queueBind(queue, exchange, routingKey)

    val consumer : DefaultConsumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]){
        val replyToProps =  new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build()

        // Switch on the routing key and use the good function

        val parsedMessage : JsValue  = Json.parse(fromBytes(body))

        val valueToReturn : String = routingKey match {
          case "login" => login(parsedMessage)
          case "create_user" => createUser(parsedMessage)
          case _ => "You shouldn't pass ! "

        }
        channel.basicPublish( "", properties.getReplyTo, replyToProps, valueToReturn.getBytes())
      }
    }
    channel.basicConsume(queue,true, consumer)
  }

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")

  def login (body : JsValue) : String = {
    println("[LOG] [Received Message / LOGIN request ]  " + body)
    println("[LOG] [Published Message / LOGIN request]  " + body)

    // TODO Check user
    body.toString()
  }

  def createUser (body : JsValue) : String = {
    println("[LOG] [Received Message / CREATE USER request ]  " + body)
    println("[LOG] [Published Message / CREATE USER request]  " + body)

    // Insert new user in database and return the user created
    UsersService.createUser(body).toString()
  }
}
