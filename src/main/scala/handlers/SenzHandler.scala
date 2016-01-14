package handlers

import actors.handlers.RegFail
import akka.actor.ActorContext
import utils.{Senz, SenzType}

/**
 * Created by eranga on 1/14/16.
 */
object SenzHandler {

  def handle(senz: Senz)(implicit context: ActorContext) = {
    senz match {
      case Senz(SenzType.GET, sender, receiver, attr, signature) =>
        val senz = Senz(SenzType.GET, sender, receiver, attr, signature)
        handleGet(senz)
      case Senz(SenzType.PUT, sender, receiver, attr, signature) =>
        val senz = Senz(SenzType.PUT, sender, receiver, attr, signature)
        handlePut(senz)
      case Senz(SenzType.SHARE, sender, receiver, attr, signature) =>
        val senz = Senz(SenzType.SHARE, sender, receiver, attr, signature)
        handlerShare(senz)
      case Senz(SenzType.DATA, sender, receiver, attr, signature) =>
        val senz = Senz(SenzType.DATA, sender, receiver, attr, signature)
        handleData(senz)
      case Senz(SenzType.PING, _, _, _, _) =>
      // we ignore ping messages
    }
  }

  def handleGet(senz: Senz) = {
    // save in database

    // send balance query to epic
  }

  def handlePut(senz: Senz) = {
    // save in database

    // send transaction request to epic
  }

  def handleData(senz: Senz)(implicit context: ActorContext) = {
    //val reg = context.actorSelection("../SenzReader/AgentRegistrationHandler")
    val reg = context.actorSelection("/user/SenzReader/AgentRegistrationHandler")

    senz.attributes.get("#msg") match {
      case Some("ShareDone") =>
      case Some("ShareFail") =>
      case Some("UserCreated") =>
      case Some("UserCreationFailed") =>
      case Some("SignatureVerificationFailed") =>
        reg ! RegFail
      case other =>
        println(s"not supported message $other")
    }
  }

  def handlerShare(senz: Senz) = {
    // nothing to do with share
  }

}
