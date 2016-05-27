package amqp

import com.rabbitmq.client.{AMQP, _}
import play.api.libs.json.Json


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
        println("[LOG] [RECEIVED MESSAGE FROM AUTHENTICATION]  " + fromBytes(body))
        val replyToProps =  new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build()
        val incomingMessage = fromBytes(body)
        val parsedMessage = Json.parse(incomingMessage)
        println(parsedMessage.\("type").as[String])



        val valueToReturn = fromBytes(body)
        println("[LOG] [PUBLISHED MESSAGE TO AUTHENTICATION]  " + fromBytes(body))

        channel.basicPublish( "", properties.getReplyTo(), replyToProps, valueToReturn.getBytes())
      }
    }
    channel.basicConsume(queue,true, consumer)
  }

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
}
