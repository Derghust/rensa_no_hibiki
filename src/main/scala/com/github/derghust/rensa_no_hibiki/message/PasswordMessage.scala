package com.github.derghust.rensa_no_hibiki.message

import com.github.derghust.rensa_no_hibiki.structure.Authentication
import com.github.derghust.rensa_no_hibiki.structure.GenericType.Id
import org.apache.pekko.actor.typed.ActorRef

trait PasswordMessage extends Message

object PasswordMessage:

  trait Validity
  object Validity:
    case object Valid                   extends Validity
    case class Invalid(message: String) extends Validity

  trait Request  extends PasswordMessage with Metadata.Task
  trait Response extends PasswordMessage with Metadata.Task

  object Request:
    final case class ValidateLogin(
      userPassword: String,
      dbPassword: String,
      response: ActorRef[PasswordMessage],
      override val taskID: Option[Id] = None
    ) extends Request
    final case class ValidateRegister(
      password: String,
      response: ActorRef[PasswordMessage],
      override val taskID: Option[Id] = None
    ) extends Request
    final case class Hash(
      userPassword: String,
      response: ActorRef[PasswordMessage],
      override val taskID: Option[Id] = None
    ) extends Request

  object Response:
    final case class ValidatedLogin(status: Authentication, override val taskID: Option[Id] = None) extends Response
    final case class ValidatedRegister(status: Validity, override val taskID: Option[Id] = None)    extends Response
    final case class Hashed(hashedPassword: String, override val taskID: Option[Id] = None)         extends Response

  final case class Initialize(numOfWorkers: Int) extends PasswordMessage
