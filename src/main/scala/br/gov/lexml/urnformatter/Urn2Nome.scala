package br.gov.lexml.urnformatter

import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento._
import scala.util.matching.Regex.Match

object Urn2Nome {

  import Urn2Format._

  def format(urnFrag: String): String = format(getComps(urnFrag))

  private def getComps(urnFrag: String) = {
    if (urnFrag.matches(raw"^(${Inciso.sigla}|${Alinea.sigla}|${Item.sigla}|${Artigo.sigla}|${Titulo.sigla}|${Paragrafo.sigla})_((?:1u|[0-9-])*)$$")) {
      compReN.findFirstMatchIn(urnFrag).toList
    } else {
      urnFrag
        .split("_").toList
        .flatMap(compRe.findFirstMatchIn(_))
    }
  }

  private def format(matches: List[Match]) = {
    val formattedComps = matches
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
      .takeWhile(!isAlteracao(_))
      .flatMap(formatComp(_))
      .reverse
    formattedComps match {
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => "d" + g + " " + txt }).mkString(" ", " ", "")).trim()
      case _ => ""
    }
  }

  private type Comp = (String, List[Numero])
  private type FormattedComp = (String, String)

  private val agregadores: Map[String, (String, String)] = Map(
    Parte.sigla -> ("a", "parte"),
    Livro.sigla -> ("o", "livro"),
    Capitulo.sigla -> ("o", "capítulo"),
    Titulo.sigla -> ("o", "título"),
    Secao.sigla -> ("a", "seção"),
    SubSecao.sigla -> ("a", "subseção"))

  private val isAlteracao: Comp => Boolean = {
    case (Alteracao.sigla, _) => true
    case _ => false
  }

  private val formatComp: Comp => Option[FormattedComp] = {
    case (Omissis.sigla, _) => Some(("o", "omissis"))
    case (Cpp.sigla, _) => Some(("o", "componente principal"))
    case (Raiz.sigla, _) => Some(("a", "raiz"))
    case (Artigo.sigla, Unico :: _) =>
      Some(("o", "artigo único"))
    case (Artigo.sigla, Algum(n) :: cs) =>
      Some(("o", "art. " + formatOrdinal(n) + formatComplementos(cs)))
    case (Anexo.sigla, Algum(n) :: cs) =>
      Some(("o", "anexo " + formatAlfa(n) + formatComplementos(cs)))
    case (Caput.sigla, _) =>
      Some(("o", "caput"))
    case (Paragrafo.sigla, Unico :: _) => Some(("o", "parágrafo único"))
    case (Paragrafo.sigla, Algum(n) :: cs) =>
      Some(("o", "parágrafo " + formatOrdinal(n) + formatComplementos(cs)))
    case (Inciso.sigla, n :: cs) =>
      Some(("o", "inciso " + formatRomano(n.n).toUpperCase + formatComplementos(cs)))
    case (Alinea.sigla, n :: cs) =>
      Some(("a", "alínea " + formatAlfa(n.n).toLowerCase + formatComplementos(cs)))
    case (Item.sigla, n :: cs) =>
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
