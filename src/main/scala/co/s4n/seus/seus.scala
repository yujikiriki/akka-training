package co.s4n.seus

import akka.actor._
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object SeusApp extends App {
  implicit val timeout = Timeout(5.seconds)

  val system = ActorSystem("Seus")
  val authenticator = system.actorOf(Props[Authenticator], "Authenticator")
  val response: Future[String] = (authenticator ? Authenticate(user = "usuario1", password = "password")).mapTo[String]
  val token = Await.result(response, 3.seconds)
  println(s"token = $token")

  val sessions = ( authenticator ? GetSessions() )
  val ss = Await.result(sessions, 3.seconds)
  println( s"ss = $ss" )

  val response2: Future[String] = (authenticator ? Authenticate(user = "usuario2", password = "password")).mapTo[String]
  val token2 = Await.result(response2, 3.seconds)
  println(s"token2 = $token2")

  val response3: Future[String] = (authenticator ? Authenticate(user = "usuario3", password = "password")).mapTo[String]
  val token3 = Await.result(response3, 3.seconds)
  println(s"token = $token3")

  val sessions3 = ( authenticator ? GetSessions() )
  val ss3 = Await.result(sessions, 3.seconds)
  println( s"ss3 = $ss3" )

  val response4: Future[String] = (authenticator ? Authenticate(user = "usuario4", password = "password")).mapTo[String]
  val token4 = Await.result(response4, 3.seconds)
  println(s"token4 = $token4")


  system.shutdown()
}

class Authenticator extends Actor {
  implicit val _: ExecutionContext = context.dispatcher
  implicit val timeout = Timeout(5.seconds)
  val sessions = collection.mutable.Map[String, ActorRef]()

  override def receive: Receive = {
    case request: Authenticate =>
      val originalSender = sender()
      authenticate(request, originalSender)
    case request: GetSessions =>
      sender() ! sessions.keys
    case _ => println("Message not supported")
  }

  private def authenticate(request: Authenticate, originalSender: ActorRef): Unit = {
    if (sessions contains request.user) {
      val userSession = context.actorSelection(s"akka://Seus/user/Authenticator/${request.user}")
      val token: Future[String] = (userSession ? GetToken()).mapTo[String]
      token pipeTo originalSender
      ()
    }
    else {
      val newSession = context.actorOf(Props[Session], request.user)
      sessions.put(request.user, newSession)
      val token = generateToken()
      sender() ! token
      newSession ! Token(token)
    }
  }

  private def generateToken(): String =
    java.util.UUID.randomUUID.toString
}


class Session extends Actor {

  var token: Option[String] = None

  override def receive: Actor.Receive = {
    case request: GetToken =>
      sender() ! token.get
    case request: Token =>
      token = Some(request.value)
  }
}

// Messages
case class Authenticate(user: String, password: String)

case class GetToken()

case class Token(value: String)

case class GetSessions()