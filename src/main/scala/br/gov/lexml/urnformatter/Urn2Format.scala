package br.gov.lexml.urnformatter

import br.gov.lexml.parser.pl.output.LexmlRenderer

object Urn2Format {

  val compReN = "^([a-z]+\\*+?|[a-z]+)_((?:1u|[0-9-])*)$".r
  val compRe = "^([a-z]+\\*+?|[a-z]+)((?:1u|[0-9-])*)$".r
  val artRe = "^anx((?:1u|[0-9-])*)$".r

  abstract sealed class Numero {
    val n: Int
  }

  final case object Unico extends Numero {
    override val n = 1
  }

  final case class Algum(n: Int) extends Numero

  def readInt: String => Numero = {
    case "1u" => Unico
    case x => Algum(x.toInt)
  }

  def formatOrdinal(num: Int): String = LexmlRenderer.renderOrdinal(num)

  def formatRomano(n: Int): String = LexmlRenderer.renderRomano(n)

  def formatAlfa(n: Int): String = LexmlRenderer.renderAlphaSeq(n - 1)

  def formatComplementos(cs: List[Numero]): String = cs.map(c => formatComplemento(c.n + 1)).map("-" + _).mkString("")
  def formatComplemento(n: Int): String = formatAlfa(n - 1).toUpperCase

}