package co.s4n.example

import org.scalatest.FunSuite
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class FutureSocialAggregatorTest extends FunSuite {

  test("One: Sequential execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos = fsa.amigos()
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    info(s"One amigosList = $amigos")
    assert(amigos.length > 0)
  }

  test("Two: Parallel execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos: Future[Seq[String]] = fsa.futureAmigos
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    amigos map {
      amigosList =>
        info(s"Two amigosList = $amigosList")
        assert( amigosList.length > 0 )
    }
    ()
  }

  test("Three: Parallel execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos: Future[Seq[String]] = fsa.fcAmigos()
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    amigos map {
      amigosList =>
        info(s"Three amigosList = $amigosList")
        assert( amigosList.length > 0 )
    }
    ()
  }

  test("Four: Parallel execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos: Future[Seq[String]] = fsa.zipAmigos()
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    amigos map {
      amigosList =>
        info(s"Four amigosList = $amigosList")
        assert( amigosList.length > 0 )
    }
    ()
  }

  test("Five: Sequential execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos: Future[Seq[String]] = fsa.seqfcAmigos()
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    amigos map {
      amigosList =>
        info(s"Five amigosList = $amigosList")
        assert( amigosList.length > 0 )
    }
    ()
  }

  test("Six: Sequential execution") {
    val fsa = new FutureSocialAggregator
    val ini = System.currentTimeMillis()
    val amigos: Future[Seq[String]] = fsa.mapAmigos()
    val end = System.currentTimeMillis()
    info( s"Total time: ${end - ini}" )
    amigos map {
      amigosList =>
        info(s"Six amigosList = $amigosList")
        assert( amigosList.length > 0 )
    }
    ()
  }

  test("Seven: error handling with map") {
    val fsa = new FutureSocialAggregator
    val handling: Future[Seq[String]] = fsa.amigosErrorHandling()
    info( s"handling = ${handling}" )
    handling map {
      r =>
        info( "Never printed" )
    }
    ()
  }

  test("Eight: error handling with callbacks") {
    val fsa = new FutureSocialAggregator
    val handling: Future[Seq[String]] = fsa.amigosErrorHandling()
    handling.onComplete {
      case Success( result ) => info( s"result = $result" )
      case Failure( ex ) => ex.printStackTrace()
    }
  }

  test("Nine: error handling with callbacks") {
    val fsa = new FutureSocialAggregator
    val handling: Future[Seq[String]] = fsa.amigosErrorHandling()

    handling.onSuccess {
      case result => info( s"result = $result" )
    }

    handling onFailure {
      case ex => ex.printStackTrace()
    }
  }
}