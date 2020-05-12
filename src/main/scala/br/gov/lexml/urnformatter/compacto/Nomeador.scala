package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.Urn2Format.{formatAlfa, formatOrdinal, formatRomano}
import br.gov.lexml.urnformatter.compacto.Numeracao.{IntervaloContinuo, NumUnico, Numeros}
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.annotation.tailrec
import scala.util.Try

private[compacto] object Nomeador {

  def nomearGrupos(grupos: List[GrupoUrns]): String = {
    @tailrec
    def go(acc: String, grupos: List[GrupoUrns]): String = grupos match {
      case Nil => acc
      case g1 :: g2 :: g3 :: _ if (g1.dispPrincipal == g2.dispPrincipal && g2.dispPrincipal == g3.dispPrincipal) =>
        go(acc ++ s"${nomear(g1)}, ", grupos.tail)
      case g1 :: Nil => go(acc ++ s"${nomear(g1)}", Nil)
      case g1 :: resto => go(acc ++ s"${nomear(g1)} e ", grupos.tail)

    }

    go("", grupos)

  }

  private def nomear(grupo: GrupoUrns): String = grupo.dispPrincipal match {
    case TipoUrnFragmento.Artigo =>
      val contemAnexo = grupo.partesComum.exists {
        case _: Anexo => true
        case _ => false
      }
      if (contemAnexo) {
        nomear(Artigo(grupo.numeracao) :: grupo.partesComum)
      } else {
        nomear(grupo.partesComum :+ Artigo(grupo.numeracao))
      }
    case TipoUrnFragmento.Caput => nomear(grupo.partesComum :+ Caput)
    case TipoUrnFragmento.ParagrafoUnico => nomear(grupo.partesComum :+ ParagrafoUnico)
    case TipoUrnFragmento.Inciso => nomear(grupo.partesComum :+ Inciso(grupo.numeracao))
    case TipoUrnFragmento.Alinea => nomear(grupo.partesComum :+ Alinea(grupo.numeracao))
    case TipoUrnFragmento.Paragrafo => nomear(grupo.partesComum :+ Paragrafo(grupo.numeracao))
    case TipoUrnFragmento.Item => nomear(grupo.partesComum :+ Item(grupo.numeracao))
    case TipoUrnFragmento.Parte => nomear(Parte(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.Titulo => nomear(Titulo(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.Capitulo => nomear(Capitulo(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.Secao => nomear(Secao(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.SubSecao => nomear(SubSecao(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.Livro => nomear(Livro(grupo.numeracao) :: grupo.partesComum)
    case TipoUrnFragmento.Anexo =>
      val maxNivel = Try(grupo.partesComum.map {
        case a: Anexo => a.nivel
        case _ => 0
      }.max).getOrElse(0)
      nomear(Anexo(grupo.numeracao, maxNivel + 1) :: grupo.partesComum)
  }

  private def nomearNumeros(ns: Numeros, fmt: Int => String): String = {
    def go(acc: String, ns: List[Int]): String = ns match {
      case Nil => acc
      case e1 :: e2 :: e3 :: _ => go(acc ++ s"${fmt(e1)}, ", ns.tail)
      case e1 :: e2 :: _ => go(acc ++ s"${fmt(e1)} e ", ns.tail)
      case e1 :: _ => go(acc ++ s"${fmt(e1)}", ns.tail)
    }

    go("", ns.list)
  }

  private def nomear(n: Numeracao, singular: String, plural: String, conector: String, fmt: Int => String): String =
    nomearComOption(n, Some(singular), Some(plural), conector, fmt)

  private def nomear(n: Numeracao, conector: String, fmt: Int => String): String =
    nomearComOption(n, None, None, conector, fmt)

  private def nomearComOption(n: Numeracao, maybeSingular: Option[String], maybePlural: Option[String], conector: String, fmt: (Int) => String): String = {
    val singular = maybeSingular.map(s => s"$s ").getOrElse("")
    val plural = maybePlural.map(s => s"$s ").getOrElse("")
    n match {
      case NumUnico(Numero.IntNumero(i)) => s"${singular}${fmt(i)}"
      case NumUnico(Numero.StrNumero(s)) => {
        val partes = s.split("-")
        s"${singular}${fmt(partes(0).toInt)}-${partes(1)}"
      }
      case IntervaloContinuo(i, f) => s"${plural}${fmt(i)} $conector ${fmt(f)}"
      case ns: Numeros => s"${plural}${nomearNumeros(ns, fmt)}"
    }
  }

  private def nomearAnexo(a: Anexo): String = a.numeracao match {
    case NumUnico(Numero.IntNumero(n)) =>
      if (a.nivel == 1) {
        s"Anexo ${formatRomano(n)}"
      } else if (a.nivel == 2) {
        s"Anexo $n"
      } else {
        s"Anexo ${formatAlfa(n).toUpperCase}"
      }
    case IntervaloContinuo(i, f) => s"Anexos ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Anexos ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomear(urnFragmento: UrnFragmento): String = urnFragmento match {
    case a: Artigo => nomear(a.numeracao, "art.", "arts.", "a", formatOrdinal)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso => nomear(i.numeracao, "a", formatRomano)
    case a: Alinea => nomear(a.numeracao, "a", formatAlfa)
    case p: Paragrafo => nomear(p.numeracao, "§", "§§", "ao", formatOrdinal)
    case i: Item => nomear(i.numeracao, "a", _.toString)
    case c: Capitulo => nomear(c.numeracao, "Capítulo", "Capítulos", "a", formatRomano)
    case s: Secao => nomear(s.numeracao, "Seção", "Seções", "a", formatRomano)
    case sb: SubSecao => nomear(sb.numeracao, "Subseção", "Subseções", "a", formatRomano)
    case l: Livro => nomear(l.numeracao, "Livro", "Livros", "a", formatRomano)
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomear(t.numeracao, "Título", "Títulos", "a", formatRomano)
    case p: Parte => nomear(p.numeracao, "Parte", "Partes", "a", _.toString)
  }

  //TODO: Remover all "partes"
  //TODO: Check compiler warnings
  private def nomear(partes: List[UrnFragmento]): String = {
    @tailrec
    def criarString(acc: String, partes: List[UrnFragmento]): String = {
      if (partes.isEmpty) {
        acc
      } else {
        (partes.head, partes.head.tipo) match {
          case (h, _: DispositivoAgrupador) if acc.isEmpty => criarString(nomear(h), partes.tail)
          case (h, d: DispositivoAgrupador) => criarString(s"${acc} ${d.conector} ${nomear(h)}", partes.tail)
          case (h, _) if acc.isEmpty => criarString(nomear(h), partes.tail)
          case (h, _) => criarString(s"${acc}, ${nomear(h)}", partes.tail)
        }
      }
    }

    criarString("", partes)
  }

}
