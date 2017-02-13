package test

import cats.syntax.either._
import io.circe._
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.{Configuration => CirceCfg}
import test.Principal._

/**
  * Created by gavares on 2/12/17.
  */
object CirceSerializers {
  implicit val circeCfg: CirceCfg = CirceCfg.default.withDiscriminator("kind")

  // --
  // -- Principal Serializers
  // --
  implicit val AgentPrincipalEncoder: ObjectEncoder[AgentPrincipal] = deriveEncoder[AgentPrincipal]
  implicit val AgentPrincipalDecoder: Decoder[AgentPrincipal] = deriveDecoder[AgentPrincipal]

  implicit val UserPrincipalEncoder: ObjectEncoder[UserPrincipal] = deriveEncoder[UserPrincipal]
  implicit val UserPrincipalDecoder: Decoder[UserPrincipal] = deriveDecoder[UserPrincipal]

  implicit val PrincipalEncoder: ObjectEncoder[Principal] = deriveEncoder[Principal]
  implicit val PrincipalDecoder: Decoder[Principal] = {
    Decoder.instance[Principal] { cursor =>
      cursor.downField("kind").as[String].flatMap {
        case "UserPrincipal" => cursor.as[UserPrincipal]
        case "AgentPrincipal" => cursor.as[AgentPrincipal]
        case other => DecodingFailure(s"Could not locate a Principal decoder for json with `kind` value '$other'", Nil).asLeft
      }
    }
  }
}
