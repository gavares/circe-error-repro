package test

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
