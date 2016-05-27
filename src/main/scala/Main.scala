import amqp.Functions
import com.rabbitmq.client.{AMQP, _}


/**
  * Created by plarboul on 05/04/2016.
  */


object Main extends App {

  val connection = Functions.connect("localhost")
  val channel : Channel = Functions.createChannel(connection)
  Functions.receive(channel, "users", "direct", "login")

}
