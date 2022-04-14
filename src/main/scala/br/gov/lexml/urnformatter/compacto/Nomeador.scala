package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.Urn2Format.{formatAlfa, formatOrdinal, formatRomano}
import br.gov.lexml.urnformatter.compacto.Numeracao.{IntervaloContinuo, UmNumero, DoisNumeros, SemNumero}
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.annotation.tailrec
import scala.util.Try

import org.slf4j.LoggerFactory

private[compacto] object Nomeador {

  val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Nomeador")

  def nomearGrupos(grupos: List[GrupoUrns]): String = {
    @tailrec
    def go(acc: String, grupos: List[GrupoUrns]): String = grupos match {
      case Nil => acc
      case g1 :: g2 :: g3 :: _ if g1.dispPrincipal == g2.dispPrincipal && g2.dispPrincipal == g3.dispPrincipal =>
        go(s"${acc}${nomear(g1)}, ", grupos.tail)
      case g1 :: Nil => go(s"${acc}${nomear(g1)}", Nil)
      case g1 :: _ => go(s"${acc}${nomear(g1)} e ", grupos.tail)
    }

    go("", grupos)
  }

  def nomearDispositivo(nomeDispositivo: Option[String], urnAgrupadorInput: String): String = {
    val urnAgrupador = UrnParser.removeRaizECppEAtc(urnAgrupadorInput.split("_").toList).mkString("_")

    if (urnAgrupador == "") nomeDispositivo.getOrElse("")
    else AgrupadorUrn.urnFragmento(urnAgrupador).tipo match {
      case d: TipoUrnFragmento with DispositivoAgrupador =>
        val nomeDispositivoFmt = nomeDispositivo.map(_ + " " + d.pronomeDemostrativo + " ").getOrElse("")
        s"${nomeDispositivoFmt}${nomear(List(AgrupadorUrn.urnFragmento(urnAgrupador))).toLowerCase.trim}"

      case d@TipoUrnFragmento.Artigo =>
        nomeDispositivo.getOrElse("artigo")

      case d@TipoUrnFragmento.Caput =>
        nomeDispositivo.getOrElse("caput")

      case d@TipoUrnFragmento.Paragrafo =>
        nomeDispositivo.getOrElse("parágrafo")

      case _ =>
        logger.warn(s"fallback nomearDispositivo: $nomeDispositivo - $urnAgrupador")
        nomeDispositivo.getOrElse("")
    }
  }

  private def nomearNaoAgrupador(grupo: GrupoUrns, principal: UrnFragmento): String = {
    val idxAnx = grupo.posAnexo
    if (idxAnx == -1) {
      nomear(grupo.fragmentosComum :+ principal)
    } else {
      val todosMenosAnx = grupo.fragmentosComum.zipWithIndex.filter(_._2 != idxAnx).map(_._1)
      nomear(todosMenosAnx :+ principal :+ grupo.fragmentosComum(idxAnx))
    }
  }

  private def nomear(grupo: GrupoUrns): String = grupo.dispPrincipal match {
    case TipoUrnFragmento.Artigo => nomearNaoAgrupador(grupo, Artigo(grupo.numeracao))
    case TipoUrnFragmento.Caput => nomearNaoAgrupador(grupo, Caput)
    case TipoUrnFragmento.ParagrafoUnico => nomearNaoAgrupador(grupo, ParagrafoUnico)
    case TipoUrnFragmento.Inciso => nomearNaoAgrupador(grupo, Inciso(grupo.numeracao))
    case TipoUrnFragmento.Alinea => nomearNaoAgrupador(grupo, Alinea(grupo.numeracao))
    case TipoUrnFragmento.Paragrafo => nomearNaoAgrupador(grupo, Paragrafo(grupo.numeracao))
    case TipoUrnFragmento.Item => nomearNaoAgrupador(grupo, Item(grupo.numeracao))
    case TipoUrnFragmento.Parte => nomear(Parte(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Titulo => nomear(Titulo(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Capitulo => nomear(Capitulo(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Secao => nomear(Secao(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.SubSecao => nomear(SubSecao(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Livro => nomear(Livro(grupo.numeracao) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Anexo =>
      val maxNivel = Try(grupo.fragmentosComum.map {
        case a: Anexo => a.nivel
        case _ => 0
      }.max).getOrElse(0)
      nomear(Anexo(grupo.numeracao, maxNivel + 1) :: grupo.fragmentosComum)
  }

  private def nomear(n: Numeracao, singular: String, plural: String, conector: String, fmt: Int => String): String =
    nomearComOption(n, Some(singular), Some(plural), conector, fmt)

  private def nomear(n: Numeracao, conector: String, fmt: Int => String): String =
    nomearComOption(n, None, None, conector, fmt)

  private def nomearComOption(n: Numeracao, maybeSingular: Option[String], maybePlural: Option[String], conector: String, fmt: (Int) => String): String = {
    val singular = maybeSingular.map(s => s"$s ").getOrElse("")
    val plural = maybePlural.map(s => s"$s ").getOrElse("")
    n match {
      case UmNumero(Numero.IntNumero(i)) => s"${singular}${fmt(i)}"
      case UmNumero(Numero.StrNumero(s)) => {
        val partesNumero = s.split("-")
        val primeiraParte = fmt(partesNumero(0).toInt)
        val segundaParte = Try(partesNumero(1).toInt).map(formatAlfa).getOrElse(partesNumero(1)).toUpperCase
        s"${singular}${primeiraParte}-${segundaParte}"
      }
      case IntervaloContinuo(i, f) => s"${plural}${fmt(i)} $conector ${fmt(f)}"
      case ns: DoisNumeros => s"${plural}${fmt(ns.n1)} e ${fmt(ns.n2)}"
      case sn: SemNumero.type => singular
      case _ => throw new IllegalArgumentException(s"Tipo numeração não esperada: $n")
    }
  }

  private def nomearAnexo(a: Anexo): String = a.numeracao match {
    case UmNumero(Numero.IntNumero(n)) =>
      if (a.nivel == 1) {
        s"Anexo ${formatRomano(n)}"
      } else if (a.nivel == 2) {
        s"Anexo $n"
      } else {
        s"Anexo ${formatAlfa(n).toUpperCase}"
      }
    case UmNumero(Numero.StrNumero(n)) =>
      s"Anexo ${n.split(";").last}"
    case IntervaloContinuo(i, f) => s"Anexos ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: DoisNumeros => s"Anexos ${formatRomano(ns.n1)} e ${formatRomano(ns.n2)}"
    case SemNumero => "Anexo"
    case _ => throw new IllegalArgumentException(s"Tipo numeração não esperada: ${a.numeracao}")
  }

  private def nomear(urnFragmento: UrnFragmento, fragmentos: List[UrnFragmento]): String = urnFragmento match {
    case a: Artigo => nomear(a.numeracao, "art.", "arts.", "a", formatOrdinal)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso =>
      val compacto = fragmentos.size > 1
      if (compacto) nomear(i.numeracao, "a", formatRomano)
      else nomear(i.numeracao, "inciso", "incisos", "a", formatRomano).trim
    case a: Alinea => nomear(a.numeracao, "a", formatAlfa)
    case p: Paragrafo =>
      val compacto = fragmentos.size > 1
      if (compacto) nomear(p.numeracao, "§", "§§", "ao", formatOrdinal)
      else nomear(p.numeracao, "§", "§§", "ao", formatOrdinal)
    case i: Item => nomear(i.numeracao, "a", _.toString)
    case c: Capitulo => nomear(c.numeracao, "Capítulo", "Capítulos", "a", formatRomano)
    case s: Secao => nomear(s.numeracao, "Seção", "Seções", "a", formatRomano)
    case sb: SubSecao => nomear(sb.numeracao, "Subseção", "Subseções", "a", formatRomano)
    case l: Livro => nomear(l.numeracao, "Livro", "Livros", "a", formatRomano)
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomear(t.numeracao, "Título", "Títulos", "a", formatRomano)
    case p: Parte => nomear(p.numeracao, "Parte", "Partes", "a", formatRomano)
  }

  private def nomear(urnFragmentos: List[UrnFragmento]): String = {
    @tailrec
    def criarString(acc: String, fragmentoes: List[UrnFragmento]): String = {
      if (fragmentoes.isEmpty) {
        acc
      } else {
        (fragmentoes.head, fragmentoes.head.tipo) match {
          case (h, _: DispositivoAgrupador) if acc.isEmpty => criarString(nomear(h, urnFragmentos), fragmentoes.tail)
          case (h, d: DispositivoAgrupador) => criarString(s"${acc} ${d.conector} ${nomear(h, urnFragmentos)}", fragmentoes.tail)
          case (h, _) if acc.isEmpty => criarString(nomear(h, urnFragmentos), fragmentoes.tail)
          case (h, _) => criarString(s"${acc}, ${nomear(h, urnFragmentos)}", fragmentoes.tail)
        }
      }
    }

    criarString("", urnFragmentos)
  }

}
