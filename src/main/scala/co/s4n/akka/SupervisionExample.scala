package co.s4n.akka

package co.s4n.akka

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.routing.RoundRobinPool
import akka.util.Timeout
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import akka.pattern._

object SupervisedPingPong extends App {
  val system = ActorSystem("PingPongActorSystem")
  implicit val _: ExecutionContext = system.dispatcher

//  val pingPongSupervisor: ActorRef = system.actorOf(Props[PingPongSupervisor], "PingPongSupervisor")
  val pingPongSupervisor: ActorRef = system.actorOf(RoundRobinPool(2).props(Props[PingPongSupervisor]), "PingPongSupervisor")

  pingPongSupervisor ! "Hola mundo!"
  pingPongSupervisor ! "Hola mundo!"

  /* End example */
  Thread.sleep(5000)
  system.shutdown()
}

class PingPongSupervisor extends Actor {
  import akka.actor.SupervisorStrategy._

  val ping: ActorRef = context.actorOf(Props[Ping], "Ping")
  val pong: ActorRef = context.actorOf(Props[Pong], "Pong")

  /* Let it crash */
  override val supervisorStrategy = OneForOneStrategy() {
    case e: Exception =>
      println( "El actor falla" )
      Stop
  }

  override def receive: Actor.Receive = {
    case message: String =>
      ping.forward( message )
  }
}

class Ping extends Actor {
  private implicit val _: ExecutionContext = context.dispatcher
  private val pong: ActorSelection = context.actorSelection("akka://PingPongActorSystem/user/Pong")

  override def receive: Receive = {
    case message: String =>
      println( s"Message $message received at Ping")
      throw new Exception( "Let me crash!" )
    case ping: PingRequest =>
      val originalSender = sender()
      implicit val timeout = Timeout(5, TimeUnit.SECONDS)
      val response: Future[PongRequest] = (pong ? ping).mapTo[PongRequest]
      response map (originalSender ! _.message)
      ()
    case message =>
      println(s"Unmanaged message at Ping: $message")
  }

}

class Pong extends Actor {

  override def receive: Receive = {
    case message: String =>
      sender ! "PONG"
    case ping: PingRequest =>
      sender ! PongRequest("PONG")
    case _ => println("Unmanaged message at Pong")
  }

}

case class PingRequest(message: String)

case class PongRequest(message: String)

