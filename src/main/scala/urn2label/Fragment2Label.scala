package urn2label

import br.gov.lexml.parser.pl.output.LexmlRenderer

object Fragment2Label {

  abstract sealed class Numero {
    val n: Int
  }
  final case object Unico extends Numero {
    override val n = 1
  }
  final case class Algum(n: Int) extends Numero

  val compRe = "^([a-z]+)((?:1u|[0-9-])*)$".r
  type Comp = (String, List[Numero])

  def readInt: String => Numero = {
    case "1u" => Unico
    case x => Algum(x.toInt)
  }

  def format(urnFrag: String): String = {
    val urnFinal =
      urnFrag
        .split("_").toList
        .flatMap(compRe.findFirstMatchIn(_))
        .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
        .last

    formatComp(urnFinal) match {
      case Some(x) => x
      case None => ""
    }
  }

  type FormattedComp = String

  val agregadores: Map[String, String] = Map(
    "prt" -> "Parte",
    "liv" -> "Livro",
    "cap" -> "Capítulo",
    "sec" -> "Seção",
    "sub" -> "Subseção")

  def formatComp: Comp => Option[FormattedComp] = {
    case ("art", Unico :: _) => Some("Art. Único.")
    case ("art", Algum(n) :: cs) =>
      Some("Art. " + formatOrdinal(n) + formatComplementos(cs))
    case ("cpt", _) => None
    case ("par", Unico :: _) => Some("Parágrafo Único.")
    case ("par", Algum(n) :: cs) =>
      Some("§ " + formatOrdinal(n) + formatComplementos(cs))
    case ("inc", n :: cs) =>
      Some(formatRomano(n.n).toUpperCase + formatComplementos(cs))
    case ("ali", n :: cs) =>
      Some(formatAlfa(n.n).toLowerCase + formatComplementos(cs))
    case ("ite", n :: cs) =>
      Some(n.n.toString + formatComplementos(cs))
    case (tip, n :: cs) if agregadores contains tip => {
      val t = agregadores(tip)
      val ntxt = n match {
        case Unico => "único"
        case Algum(n) => formatRomano(n).toUpperCase
      }
      Some(t + " " + ntxt)
    }
    case _ => None
  }

  def formatOrdinal(num: Int): String = LexmlRenderer.renderOrdinal(num)

  def formatRomano(n: Int): String = LexmlRenderer.renderRomano(n)

  def formatAlfa(n: Int): String = LexmlRenderer.renderAlphaSeq(n - 1)

  def formatComplementos(cs: List[Numero]): String = cs.map(c => formatComplemento(c.n)).map("-" + _).mkString("")
  def formatComplemento(n: Int): String = formatAlfa(n - 1).toUpperCase

}