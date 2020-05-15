package br.gov.lexml.urnformatter.compacto

private[compacto] sealed abstract class Numero

private[compacto] object Numero {

  case class IntNumero(n: Int) extends Numero

  case class StrNumero(s: String) extends Numero

  case object Unico extends Numero

  case object SemNumero extends Numero

}
