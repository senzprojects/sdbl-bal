package boot

import java.net.DatagramSocket

import actors._
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import crypto.RSAUtils

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
 * Created by eranga on 1/9/16.
 */
object Main extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system = ActorSystem("senz")

  val socket = new DatagramSocket()

  // first generate key pair
  RSAUtils.initRSAKeys()

  // initialize actors
  val senzSender = system.actorOf(Props(classOf[SenzSender], socket), name = "SenzSender")
  val senzListener = system.actorOf(Props(classOf[SenzListener], socket), name = "SenzListener")
  val senzReader = system.actorOf(Props[SenzReader], name = "SenzReader")
  val pingSender = system.actorOf(Props[PingSender], name = "PingSender")

  // init sender and wait until its success   
  implicit val timeout = Timeout(5 seconds)
  val future = senzSender ? InitSender
  future onComplete {
    case Success(result) =>
      // start listener, ping sender and reader
      senzListener ! InitListener
      //pingSender ! Ping
      senzReader ! InitReader
    case Failure(result) =>
      println("init fails")
  }
}
