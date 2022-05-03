package br.gov.lexml.urnformatter.compacto

private[compacto] sealed abstract class Numeracao

private[compacto] object Numeracao {

  case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

  case class DoisNumeros(n1: Int, n2: Int) extends Numeracao

  // precisa ser continuo???
  case class NumerosNaoContinuos(ns: List[Int]) extends Numeracao

  case class UmNumero(n: Numero) extends Numeracao

  case object SemNumero extends Numeracao

}
