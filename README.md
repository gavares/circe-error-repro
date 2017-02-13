### Summary

This project provides a repro case for a bug in Circe that results in error 
```
could not find Lazy implicit value of type io.circe.generic.extras.encoding.ConfiguredObjectEncoder[Principal]
```

when the compile target is run. The error can be resolved by moving the contents of `Principal.scala` into
`CirceSerializers.scala` near the top of the file so that it looks like: 

```scala
package sk.test
import cats.syntax.either._
import io.circe._
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.{Configuration => CirceCfg}
import sk.test.Principal._

/**
  * Created by gavares on 2/12/17.
  */
sealed trait Principal {
  def name: String
}


object Principal {
  case class UserPrincipal(name: String) extends Principal
  case class AgentPrincipal(name: String) extends Principal

}


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
```
