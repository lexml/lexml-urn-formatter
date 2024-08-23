package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.Urn2Format.{formatAlfa, formatOrdinal, formatRomano}
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.UrnFragmento._
import org.slf4j.LoggerFactory

import scala.annotation.tailrec
import scala.util.Try

private[compacto] class Nomeador(grupos: List[GrupoUrns], referenciaMesmoArtigo: Boolean) {

  private val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Nomeador")
  private var groupPosicao = -1

  def nomearGrupos: String = {
    @tailrec
    def go(acc: String, grupos: List[GrupoUrns]): String = {
      groupPosicao = groupPosicao + 1
      grupos match {
        case Nil => acc
        case g1 :: g2 :: g3 :: _ if g1.dispPrincipal == g2.dispPrincipal && g2.dispPrincipal == g3.dispPrincipal =>
          go(s"${acc}${nomear(g1)}, ", grupos.tail)
        case g1 :: Nil => go(s"${acc}${nomear(g1)}", Nil)
        case g1 :: _ => go(s"${acc}${nomear(g1)} e ", grupos.tail)
      }
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

      case _@TipoUrnFragmento.Artigo =>
        nomeDispositivo.getOrElse("artigo")

      case _@TipoUrnFragmento.Caput =>
        nomeDispositivo.getOrElse("caput")

      case _@TipoUrnFragmento.Paragrafo =>
        nomeDispositivo.getOrElse("parágrafo")

      case _ =>
        logger.warn(s"fallback nomearDispositivo: $nomeDispositivo - $urnAgrupador")
        nomeDispositivo.getOrElse("")
    }
  }

  private def nomearNaoAgrupador(grupo: GrupoUrns, principal: UrnFragmento): String = {
    val idxAnx = grupo.posAnexo
    if (idxAnx == -1) {
      if (grupo.fragmentosComum.exists(_.tipo == TipoUrnFragmento.Caput)) {
        nomear(grupo.fragmentosComum.tail :+ principal :+ grupo.fragmentosComum.head)
      } else {
        nomear(grupo.fragmentosComum :+ principal)
      }
    } else {
      val todosMenosAnx = grupo.fragmentosComum.zipWithIndex.filter(_._2 != idxAnx).map(_._1)
      nomear(todosMenosAnx :+ principal :+ grupo.fragmentosComum(idxAnx))
    }
  }

  private def nomear(grupo: GrupoUrns): String = grupo.dispPrincipal match {
    case TipoUrnFragmento.Artigo => nomearNaoAgrupador(grupo, Artigo(grupo.numeros))
    case TipoUrnFragmento.Caput => nomearNaoAgrupador(grupo, Caput)
    case TipoUrnFragmento.ParagrafoUnico => nomearNaoAgrupador(grupo, ParagrafoUnico)
    case TipoUrnFragmento.Inciso =>
      if (referenciaMesmoArtigo) {
        nomear(Inciso(grupo.numeros) :: grupo.fragmentosComum)
      } else {
        nomearNaoAgrupador(grupo, Inciso(grupo.numeros))
      }
    case TipoUrnFragmento.Alinea =>
      if (referenciaMesmoArtigo && groupPosicao == 0) {
        nomear(Alinea(grupo.numeros) :: grupo.fragmentosComum)
      } else {
        nomearNaoAgrupador(grupo, Alinea(grupo.numeros))
      }
    case TipoUrnFragmento.Paragrafo => nomearNaoAgrupador(grupo, Paragrafo(grupo.numeros))
    case TipoUrnFragmento.Item => nomearNaoAgrupador(grupo, Item(grupo.numeros))
    case TipoUrnFragmento.Parte => nomear(Parte(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Titulo => nomear(Titulo(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Capitulo => nomear(Capitulo(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Secao => nomear(Secao(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.SubSecao => nomear(SubSecao(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Livro => nomear(Livro(grupo.numeros) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Anexo =>
      val maxNivel = Try(grupo.fragmentosComum.map {
        case a: Anexo => a.nivel
        case _ => 0
      }.max).getOrElse(0)
      nomear(Anexo(grupo.numeros, maxNivel + 1) :: grupo.fragmentosComum)
  }

  private def nomear(numeros: List[Numero], singular: String, plural: String, fmt: Int => String): String =
    nomearComOption(numeros, Some(singular), Some(plural), fmt)

  private def nomear(numeros: List[Numero], fmt: Int => String): String =
    nomearComOption(numeros, None, None, fmt)

  private def nomearComOption(numeros: List[Numero], maybeSingular: Option[String], maybePlural: Option[String], fmt: Int => String): String = {
    val sNumeracoesList = numeros.zipWithIndex.map {
      case (Numero.IntNumero(i), _) => fmt(i)
      case (Numero.StrNumero(s), _) =>
        val partesNumero = s.split("-")
        val primeiraParte = fmt(partesNumero(0).toInt)
        val segundaParte = Try(partesNumero(1).toInt).map(formatAlfa).getOrElse(partesNumero(1)).toUpperCase
        s"${primeiraParte}-${segundaParte}"
      case (_: Numero.SemNumero.type, _) => ""
      case n@_ => throw new IllegalArgumentException(s"Tipo numeração não esperada: $n")
    }
    val singular = maybeSingular.map(s => s"$s ").getOrElse("")
    val plural = maybePlural.map(s => s"$s ").getOrElse("")
    pluralOuSingular(numeros.size, singular, plural) + concatNumerosStr(sNumeracoesList)
  }

  private def nomearAnexo(a: Anexo): String = {
    val sNumeracoesList = a.numeros.zipWithIndex.map {
      case (Numero.IntNumero(n), _) =>
        if (a.nivel == 1) {
          formatRomano(n)
        } else if (a.nivel == 2) {
          n.toString
        } else {
          formatAlfa(n).toUpperCase
        }
      case (Numero.StrNumero(n), _) => n.split(";").last
      case n@_ => throw new IllegalArgumentException(s"Tipo numeração não esperada: $n")
    }
    pluralOuSingular(a.numeros.size, "Anexo", "Anexos") + " " + concatNumerosStr(sNumeracoesList)
  }

  private def pluralOuSingular(sizeNumeros: Int, singular: String, plural: String): String = if (sizeNumeros > 1) plural else singular

  private def concatNumerosStr(numerosStr: List[String]): String =
    if (numerosStr.size > 1) {
      numerosStr.dropRight(1).mkString(", ") + " e " + numerosStr.last
    } else {
      numerosStr.mkString("")
    }

  private def nomear(urnFragmento: UrnFragmento, fragmentos: List[UrnFragmento]): String = urnFragmento match {
    case a: Artigo => nomear(a.numeros, "art.", "arts.", formatOrdinal)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso => nomear(i.numeros, "inciso", "incisos", formatRomano).trim
    case a: Alinea => nomear(a.numeros, formatAlfa(_))
    case p: Paragrafo =>
      val compacto = fragmentos.size > 1
      if (compacto) nomear(p.numeros, "§", "§§", formatOrdinal)
      else nomear(p.numeros, "§", "§§", formatOrdinal)
    case i: Item => nomear(i.numeros, _.toString)
    case c: Capitulo => nomear(c.numeros, "Capítulo", "Capítulos", formatRomano)
    case s: Secao => nomear(s.numeros, "Seção", "Seções", formatRomano)
    case sb: SubSecao => nomear(sb.numeros, "Subseção", "Subseções", formatRomano)
    case l: Livro => nomear(l.numeros, "Livro", "Livros", formatRomano)
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomear(t.numeros, "Título", "Títulos", formatRomano)
    case p: Parte => nomear(p.numeros, "Parte", "Partes", formatRomano)
  }

  private def nomear(urnFragmentos: List[UrnFragmento]): String = {
    @tailrec
    def criarString(acc: String, fragmentos: List[UrnFragmento]): String = {
      if (fragmentos.isEmpty) {
        acc
      } else {
        (fragmentos.head, fragmentos.head.tipo) match {
          case (h, _) if acc.isEmpty => criarString(nomear(h, urnFragmentos), fragmentos.tail)
          case (h, d: DispositivoAgrupador) => criarString(s"${acc} ${d.conector} ${nomear(h, urnFragmentos)}", fragmentos.tail)
          case (h, d) if (referenciaMesmoArtigo && fragmentos.tail.isEmpty && (h.tipo == TipoUrnFragmento.Caput || h.tipo == TipoUrnFragmento.Paragrafo)) =>
            criarString(s"${acc} ${d.conector} ${nomear(h, urnFragmentos)}", fragmentos.tail)
          case (h, _) => criarString(s"${acc}, ${nomear(h, urnFragmentos)}", fragmentos.tail)
        }
      }
    }

    criarString("", urnFragmentos)
  }

}
