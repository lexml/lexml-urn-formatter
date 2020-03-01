package br.gov.lexml.urnformatter

object Urn2NomeCompacto {

  import Urn2Format._

  type Comp = (String, List[Numero])

  def format(urnFrag: String) = {
    val comps = urnFrag
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
      .flatMap(formatComp(_))

    val size = comps.size;
    val aux = comps.zipWithIndex
        .filter {
            case (e, i) => !(e == (",","caput") && i < size - 1)
            case _ => false
        }
        .map {
            case (e, i) => e
        }
    
    aux match {
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => g + " " + txt }).mkString("", "", "")).trim()
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
      Some(("o", "art. único"))
    case ("art", Algum(n) :: cs) =>
      Some(("o", "art. " + formatOrdinal(n) + formatComplementos(cs)))
    case ("cpt", _) =>
      Some((",", "caput"))
    case ("par", Unico :: _) => Some((",", "parágrafo único"))
    case ("par", Algum(n) :: cs) =>
      Some((", §", formatOrdinal(n) + formatComplementos(cs)))
    case ("inc", n :: cs) =>
      Some((",", formatRomano(n.n).toUpperCase + formatComplementos(cs)))
    case ("ali", n :: cs) =>
      Some((",", formatAlfa(n.n).toLowerCase + formatComplementos(cs)))
    case ("ite", n :: cs) =>
      Some((",", n.n.toString + formatComplementos(cs)))
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
