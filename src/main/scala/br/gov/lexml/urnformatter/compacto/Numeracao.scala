package br.gov.lexml.urnformatter.compacto

private[compacto] sealed abstract class Numeracao

private[compacto] sealed abstract class NumeracaoMultipla

//private[compacto] object NumeracaoMultipla {
//
//  case class IntervaloContinuo(inicio: Int, fim: Int) extends NumeracaoMultipla
//
//  case class Numeros(values: List[Int]) extends NumeracaoMultipla
//
//}

private[compacto] object Numeracao {

  // case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

  //TODO: Remove
  case class DoisNumeros(n1: Int, n2: Int) extends Numeracao

  // precisa ser continuo???
  // case class MultiplosNumeros(values: List[NumeracaoMultipla]) extends Numeracao

  case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

  case class Numeros(values: List[Int]) extends Numeracao

  case class UmNumero(n: Numero) extends Numeracao

  case object SemNumero extends Numeracao

}
