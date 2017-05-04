package br.gov.lexml.urnformatter

object Urn2Nome {

  import Urn2Format._

  type Comp = (String, List[Numero])

  def format(urnFrag: String) = {
    val comps = urnFrag
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
      .flatMap(formatComp(_))
      .reverse
    comps match {
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => "d" + g + " " + txt }).mkString(" ", " ", "")).trim()
      case _ => ""
    }
  }

  type FormattedComp = (String, String)

  val agregadores: Map[String, (String, String)] = Map(
    "prt" -> ("a", "parte"),
    "liv" -> ("o", "livro"),
    "cap" -> ("o", "capítulo"),
    "tit" -> ("o", "título"),
    "sec" -> ("a", "seção"),
    "sub" -> ("a", "subseção"))

  def formatComp: Comp => Option[FormattedComp] = {
    case ("alt", _) => Some(("a", "alteração"))
    case ("omi", _) => Some(("o", "omissis"))

    case ("art", Unico :: _) =>
      Some(("o", "artigo único"))
    case ("art", Algum(n) :: cs) =>
      Some(("o", "artigo " + formatOrdinal(n) + formatComplementos(cs)))
    case ("cpt", _) =>
      Some(("o", "caput"))
    case ("par", Unico :: _) => Some(("o", "parágrafo único"))
    case ("par", Algum(n) :: cs) =>
      Some(("o", "parágrafo " + formatOrdinal(n) + formatComplementos(cs)))
    case ("inc", n :: cs) =>
      Some(("o", "inciso " + formatRomano(n.n).toUpperCase + formatComplementos(cs)))
    case ("ali", n :: cs) =>
      Some(("a", "alínea " + formatAlfa(n.n).toLowerCase + formatComplementos(cs)))
    case ("ite", n :: cs) =>
      Some(("o", "item " + n.n.toString + formatComplementos(cs)))
    case (tip, n :: cs) if agregadores contains tip => {
      val (g, t) = agregadores(tip)
      val ntxt = n match {
        case Unico => "único"
        case Algum(n) => formatRomano(n).toUpperCase
      }
      Some((g, t + " " + ntxt + formatComplementos(cs)))
    }
    case _ => None
  }

}
