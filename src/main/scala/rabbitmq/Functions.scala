package rabbitmq

import com.rabbitmq.client.{AMQP, _}

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
        println("received: " + fromBytes(body))
        val replyToProps =  new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build()

        // TO DO Replace value by the return of function
        val valueToReturn = "Return"
        channel.basicPublish( "", properties.getReplyTo(), replyToProps, valueToReturn.getBytes())
      }
    }
    channel.basicConsume(queue,true, consumer)
  }

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
}
