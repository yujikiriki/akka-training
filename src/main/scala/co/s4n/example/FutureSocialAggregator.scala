package co.s4n.example

import java.util.concurrent.Executors

import co.s4n.sa.SocialWeb20Adapter
import scala.concurrent.{ExecutionContext, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._


class FutureSocialAggregator {

  private val executorService = Executors.newFixedThreadPool(4)
  private implicit val _ = ExecutionContext.fromExecutorService(executorService)

  def amigos(): Seq[String] = {
    val swa = new SocialWeb20Adapter
    val fb: Seq[String] = swa.facebookFriends()
    val gmail: Seq[String] = swa.gmailContacts()
    val inst: Seq[String] = swa.instagramFollowers()
    val tw: Seq[String] = swa.twitterFollowers()
    fb ++ gmail ++ inst ++ tw
  }

  // Parallel execution
  def futureAmigos(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    /* Declare futures first */
    val gmail: Future[Seq[String]] = fswa.gmail
    val tw: Future[Seq[String]] = fswa.twitter
    gmail.flatMap {
      gcontacts =>
        tw.flatMap {
          tfollowers =>
            Future(tfollowers ++ gcontacts)
        }
    }
  }

  // Sequential execution
  def mapAmigos(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    val d: Future[Future[Seq[String]]] = fswa.gmail map {
      g =>
        fswa.twitter map {
          t =>
            g ++ t
        }
    }
    d.flatMap(x => x)
  }

  // Sequential execution
  def seqfcAmigos(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    for {
      gmailContacts <- fswa.gmail
      tw <- fswa.twitter
    } yield gmailContacts ++ tw
  }

  // Parallel execution
  def fcAmigos(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    /* Declare futures first */
    val gmail: Future[Seq[String]] = fswa.gmail
    val twitter: Future[Seq[String]] = fswa.twitter
    for {
      gmailContacts <- gmail
      tw <- twitter
    } yield gmailContacts ++ tw
  }

  // Parallel execution
  def zipAmigos(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    for {
      (gmailContacts, tw) <- fswa.gmail zip fswa.twitter
    } yield gmailContacts ++ tw
  }

  // Error handling
  def amigosErrorHandling(): Future[Seq[String]] = {
    val fswa = new FunctionalSWA
    val instagram: Future[Seq[String]] = fswa.instagram()
    instagram
  }

}

class FunctionalSWA {
  private val executorService = Executors.newFixedThreadPool(4)
  private implicit val executionContext = ExecutionContext.fromExecutorService(executorService)

  private val swa = new SocialWeb20Adapter

  def gmail(): Future[Seq[String]] = Future(swa.gmailContacts())

  def twitter(): Future[Seq[String]] = Future(swa.twitterFollowers())

  def instagram(): Future[Seq[String]] = Future(throw new Exception("I died"))

}
