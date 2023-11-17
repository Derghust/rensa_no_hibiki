package com.github.derghust.pekkotemplate.message

import org.apache.pekko.actor.typed.ActorRef

final case class ArgumentMessage(cmd: String, args: List[String]) extends Message
final case class ArgumentMessageRequester(
    cmd: String,
    requester: ActorRef[Message],
    args: List[String],
) extends Message
