package co.s4n.akka

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import akka.pattern._


object PingPong extends App {
  val system = ActorSystem("PingPongActorSystem")
  implicit val _: ExecutionContext = system.dispatcher

  val ping: ActorRef = system.actorOf(Props[Ping], "Ping")
  val pong: ActorRef = system.actorOf(Props[Pong], "Pong")
  println(pong.path)

  ping ! "Hello world!"


  /* End example */
  Thread.sleep(5000)
  system.shutdown()
}

object PingPong2 extends App {
  val system = ActorSystem("PingPongActorSystem")
  implicit val _: ExecutionContext = system.dispatcher

  val ping: ActorRef = system.actorOf(Props[Ping], "Ping")
  val pong: ActorRef = system.actorOf(Props[Pong], "Pong")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  val response: Future[String] = (ping ? PingRequest( "PING" )).mapTo[String]
  response map println

  /* End example */
  Thread.sleep(10000)
  system.shutdown()
}


class Ping extends Actor {
  private implicit val _: ExecutionContext = context.dispatcher
  private val pong: ActorSelection = context.actorSelection("akka://PingPongActorSystem/user/Pong")

  override def receive: Receive = {
    case message: String =>
      pong ! "PING"
    case ping: PingRequest =>
      val originalSender = sender()
      implicit val timeout = Timeout(5, TimeUnit.SECONDS)
      val response: Future[PongRequest] = (pong ? ping).mapTo[PongRequest]
      response map ( originalSender ! _.message )
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
      sender ! PongRequest( "PONG" )
    case _ => println("Unmanaged message at Pong")
  }

}

case class PingRequest( message: String )
case class PongRequest( message: String )

