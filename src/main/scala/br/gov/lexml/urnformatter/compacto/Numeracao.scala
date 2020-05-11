package br.gov.lexml.urnformatter.compacto

private[compacto] sealed abstract class Numeracao

private[compacto] object Numeracao {

  case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

  case class Numeros(list: List[Int]) extends Numeracao

  case class NumUnico(n: Numero) extends Numeracao //TODO: UmNumero?

}
