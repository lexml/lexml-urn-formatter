package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.Urn2Format.{formatAlfa, formatOrdinal, formatRomano}
import br.gov.lexml.urnformatter.compacto.Numeracao._
import br.gov.lexml.urnformatter.compacto.Numero.IntNumero
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.annotation.tailrec
import scala.util.Try
import org.slf4j.LoggerFactory

private[compacto] object Nomeador {

  val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Nomeador")

  def nomearGrupos(grupos: List[GrupoUrns]): String = {
    println(s"==> grupos: ${grupos.mkString(",")}")
//    val nGrupos = List(GrupoUrns(TipoUrnFragmento.Inciso, List(Artigo(UmNumero(IntNumero(56)))), NumerosNaoContinuos(List(1, 3, 5))))
//    println(s"==> nGrupos: ${nGrupos.mkString(",")}")

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
    case TipoUrnFragmento.Artigo => nomearNaoAgrupador(grupo, Artigo(grupo.numeracoes))
    case TipoUrnFragmento.Caput => nomearNaoAgrupador(grupo, Caput)
    case TipoUrnFragmento.ParagrafoUnico => nomearNaoAgrupador(grupo, ParagrafoUnico)
    case TipoUrnFragmento.Inciso => nomearNaoAgrupador(grupo, Inciso(grupo.numeracoes))
    case TipoUrnFragmento.Alinea => nomearNaoAgrupador(grupo, Alinea(grupo.numeracoes))
    case TipoUrnFragmento.Paragrafo => nomearNaoAgrupador(grupo, Paragrafo(grupo.numeracoes))
    case TipoUrnFragmento.Item => nomearNaoAgrupador(grupo, Item(grupo.numeracoes))
    case TipoUrnFragmento.Parte => nomear(Parte(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Titulo => nomear(Titulo(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Capitulo => nomear(Capitulo(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Secao => nomear(Secao(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.SubSecao => nomear(SubSecao(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Livro => nomear(Livro(grupo.numeracoes) :: grupo.fragmentosComum)
    case TipoUrnFragmento.Anexo =>
      val maxNivel = Try(grupo.fragmentosComum.map {
        case a: Anexo => a.nivel
        case _ => 0
      }.max).getOrElse(0)
      nomear(Anexo(grupo.numeracoes, maxNivel + 1) :: grupo.fragmentosComum)
  }

  private def nomear(numeracoes: List[Numeracao], singular: String, plural: String, conector: String, fmt: Int => String): String =
    nomearComOption(numeracoes, Some(singular), Some(plural), conector, fmt)

  private def nomear(numeracoes: List[Numeracao], conector: String, fmt: Int => String): String =
    nomearComOption(numeracoes, None, None, conector, fmt)

  private def nomearComOption(numeracoes: List[Numeracao], maybeSingular: Option[String], maybePlural: Option[String], conector: String, fmt: (Int) => String): String = {
    val singular = maybeSingular.map(s => s"$s ").getOrElse("")
    val plural = maybePlural.map(s => s"$s ").getOrElse("")
    val sNumeracoesList = numeracoes.zipWithIndex.map {
      case (UmNumero(Numero.IntNumero(i)), _) => fmt(i)
      case (UmNumero(Numero.StrNumero(s)), _) =>
        val partesNumero = s.split("-")
        val primeiraParte = fmt(partesNumero(0).toInt)
        val segundaParte = Try(partesNumero(1).toInt).map(formatAlfa).getOrElse(partesNumero(1)).toUpperCase
        s"${primeiraParte}-${segundaParte}"
      // case IntervaloContinuo(i, f) => s"${plural}${fmt(i)} $conector ${fmt(f)}"
      // case ns: DoisNumeros => s"${plural}${fmt(ns.n1)} e ${fmt(ns.n2)}"
      case (_: SemNumero.type, _) => ""
      case (IntervaloContinuo(inicio, fim), idx) =>
        if (inicio + 1 == fim) {
          s"${fmt(inicio)} e ${fmt(fim)}"
        } else {
          s"${fmt(inicio)} $conector ${fmt(fim)}"
        }
      case (Numeros(values), idx) =>
        val sNumeros = values.dropRight(1).map(fmt).mkString(", ")
        val connectorNumeros = if (idx < numeracoes.size - 1) ", " else " e "
        s"${sNumeros}${connectorNumeros}${fmt(values.last)}"
      case n@_ => throw new IllegalArgumentException(s"Tipo numeração não esperada: $n")
    }

    val sNumeracoes =
      if (sNumeracoesList.size > 1) {
        sNumeracoesList.dropRight(1).mkString(", ") + " e " + sNumeracoesList.last
      } else {
        sNumeracoesList.mkString("")
      }
    if (numeracoes.size > 1 || numeracoes(0).isInstanceOf[IntervaloContinuo] || numeracoes(0).isInstanceOf[Numeros]) {
      s"${plural}${sNumeracoes}"
    } else {
      s"${singular}${sNumeracoes}"
    }
  }

  private def nomearAnexo(a: Anexo): String =  {
    val sNumeracoesList = a.numeracoes.zipWithIndex.map {
      case (UmNumero(Numero.IntNumero(n)), _) =>
        if (a.nivel == 1) {
          formatRomano(n)
        } else if (a.nivel == 2) {
          n
        } else {
          formatAlfa(n).toUpperCase
        }
      case (UmNumero(Numero.StrNumero(n)), _) => n.split(";").last
      case (IntervaloContinuo(inicio, fim), _) =>
        if (inicio + 1 == fim) {
          s"${formatRomano(inicio)} e ${formatRomano(fim)}"
        } else {
          s"${formatRomano(inicio)} a ${formatRomano(fim)}"
        }
      case (Numeros(values), idx) =>
        val sNumeros = values.dropRight(1).map(formatRomano).mkString(", ")
        val connectorNumeros = if (idx < a.numeracoes.size - 1) ", " else " e "
        s"${sNumeros}${connectorNumeros}${formatRomano(values.last)}"
    }
    val sNumeracoes =
      if (sNumeracoesList.size > 1) {
        sNumeracoesList.dropRight(1).mkString(", ") + " e " + sNumeracoesList.last
      } else {
        sNumeracoesList.mkString("")
      }
    if (a.numeracoes.size > 1 || a.numeracoes(0).isInstanceOf[IntervaloContinuo] || a.numeracoes(0).isInstanceOf[Numeros]) {
      s"Anexos ${sNumeracoes}"
    } else {
      s"Anexo ${sNumeracoes}"
    }
    // a.numeracoes match {
      //    case UmNumero(Numero.IntNumero(n)) =>
      //      if (a.nivel == 1) {
      //        s"Anexo ${formatRomano(n)}"
      //      } else if (a.nivel == 2) {
      //        s"Anexo $n"
      //      } else {
      //        s"Anexo ${formatAlfa(n).toUpperCase}"
      //      }
      //    case UmNumero(Numero.StrNumero(n)) =>
      //      s"Anexo ${n.split(";").last}"
      //    // case IntervaloContinuo(i, f) => s"Anexos ${formatRomano(i)} a ${formatRomano(f)}"
      //    case multiplos: MultiplosNumeros =>
      //      val sMultiplos = multiplos.values.zipWithIndex.map {
      //        case (NumeracaoMultipla.IntervaloContinuo(inicio, fim), idx) =>
      //          val connectorAntes = if (idx == 0) {
      //            ""
      //          } else if(idx < multiplos.values.size - 1) {
      //            ", "
      //          } else {
      //            " e "
      //          }
      //          if (inicio + 1 == fim) {
      //            s"${connectorAntes}${formatRomano(inicio)} e ${formatRomano(fim)}"
      //          } else {
      //            s"${connectorAntes}${formatRomano(inicio)} a ${formatRomano(fim)}"
      //          }
      //        case (NumeracaoMultipla.Numeros(values), idx) =>
      //          val sNumeros = values.dropRight(1).map(formatRomano).mkString(", ")
      //          val connectorNumeros = if (idx < multiplos.values.size - 1) ", " else " e "
      //          s"${sNumeros}${connectorNumeros}${formatRomano(values.last)}"
      //      }.mkString("") //TODO:
      //      s"Anexos ${sMultiplos}"
      //
      //    case ns: DoisNumeros => s"Anexos ${formatRomano(ns.n1)} e ${formatRomano(ns.n2)}"
      //    case SemNumero => "Anexo"
//      case n@_ => throw new IllegalArgumentException(s"Tipo numeração não esperada: ${a}")
//    }
  }

  private def nomear(urnFragmento: UrnFragmento, fragmentos: List[UrnFragmento]): String = urnFragmento match {
    case a: Artigo => nomear(a.numeracoes, "art.", "arts.", "a", formatOrdinal)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso =>
      val compacto = fragmentos.size > 1
      if (compacto) nomear(i.numeracoes, "a", formatRomano)
      else nomear(i.numeracoes, "inciso", "incisos", "a", formatRomano).trim
    case a: Alinea => nomear(a.numeracoes, "a", formatAlfa)
    case p: Paragrafo =>
      val compacto = fragmentos.size > 1
      if (compacto) nomear(p.numeracoes, "§", "§§", "ao", formatOrdinal)
      else nomear(p.numeracoes, "§", "§§", "ao", formatOrdinal)
    case i: Item => nomear(i.numeracoes, "a", _.toString)
    case c: Capitulo => nomear(c.numeracoes, "Capítulo", "Capítulos", "a", formatRomano)
    case s: Secao => nomear(s.numeracoes, "Seção", "Seções", "a", formatRomano)
    case sb: SubSecao => nomear(sb.numeracoes, "Subseção", "Subseções", "a", formatRomano)
    case l: Livro => nomear(l.numeracoes, "Livro", "Livros", "a", formatRomano)
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomear(t.numeracoes, "Título", "Títulos", "a", formatRomano)
    case p: Parte => nomear(p.numeracoes, "Parte", "Partes", "a", formatRomano)
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
