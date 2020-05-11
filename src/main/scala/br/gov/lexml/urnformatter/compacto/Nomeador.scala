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

  private def nomear(grupo: GrupoUrns): String = {
    val res = if (grupo.dispPrincipal == "sec") {
      nomear(Secao(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "par") {
      nomear(grupo.partesComum :+ Paragrafo(grupo.numeracao))
    } else if (grupo.dispPrincipal == "inc") {
      nomear(grupo.partesComum :+ Inciso(grupo.numeracao))
    } else if (grupo.dispPrincipal == "ite") {
      nomear(grupo.partesComum :+ Item(grupo.numeracao))
    } else if (grupo.dispPrincipal == "art") {
      val contemAnexo = grupo.partesComum.exists {
        case _: Anexo => true
        case _ => false
      }
      if (contemAnexo) {
        nomear(Artigo(grupo.numeracao) :: grupo.partesComum)
      } else {
        nomear(grupo.partesComum :+ Artigo(grupo.numeracao))
      }
    } else if (grupo.dispPrincipal == "tit") {
      nomear(Titulo(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "ali") {
      nomear(grupo.partesComum :+ Alinea(grupo.numeracao))
    } else if (grupo.dispPrincipal == "par1u") {
      nomear(grupo.partesComum :+ ParagrafoUnico)
    } else if (grupo.dispPrincipal == "anx") {
      val maxNivel = Try(grupo.partesComum.map {
        case a: Anexo => a.nivel
        case _ => 0
      }.max).getOrElse(0)
      nomear(Anexo(grupo.numeracao, maxNivel + 1) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "cpt") {
      nomear(grupo.partesComum :+ Caput)
    } else if (grupo.dispPrincipal == "cap") {
      nomear(Capitulo(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "sub") {
      nomear(SubSecao(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "liv") {
      nomear(Livro(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "prt") {
      nomear(Parte(grupo.numeracao) :: grupo.partesComum)
    } else {
      ???
    }
    res
  }

  private def conectorIntervalo(ini: Int, fim: Int): String = {
    if (ini == (fim - 1)) {
      "e"
    } else {
      "a"
    }
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

  private def nomearSecao(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(i)) => s"Seção ${formatRomano(i)}"
    case IntervaloContinuo(i, f) => s"Seções ${formatRomano(i)} ${conectorIntervalo(i, f)} ${formatRomano(f)}"
    case ns: Numeros => s"Seções ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomearArtigo(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"art. ${formatOrdinal(n)}"
    case NumUnico(Numero.StrNumero(s)) => {
      val partes = s.split("-")
      s"art. ${formatOrdinal(partes(0).toInt)}-${partes(1)}"
    }
    case IntervaloContinuo(i, f) => s"arts. ${formatOrdinal(i)} ${conectorIntervalo(i, f)} ${formatOrdinal(f)}"
    case ns: Numeros => s"arts. ${nomearNumeros(ns, formatOrdinal)}"
  }

  private def nomearAlinea(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => formatAlfa(n)
    case IntervaloContinuo(i, f) => s"${formatAlfa(i)} ${conectorIntervalo(i, f)} ${formatAlfa(f)}"
    case ns: Numeros => nomearNumeros(ns, formatAlfa)
  }

  private def nomearInciso(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => formatRomano(n)
    case IntervaloContinuo(i, f) => s"${formatRomano(i)} ${conectorIntervalo(i, f)} ${formatRomano(f)}"
    case ns: Numeros => nomearNumeros(ns, formatRomano)
  }

  private def nomearParagrafo(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"§ ${formatOrdinal(n)}"
    case IntervaloContinuo(i, f) => s"§§ ${formatOrdinal(i)} ao ${formatOrdinal(f)}"
    case ns: Numeros => s"§§ ${nomearNumeros(ns, formatOrdinal)}"
  }

  private def nomearTitulo(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"Título ${formatRomano(n)}"
    case IntervaloContinuo(i, f) => s"Títulos ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Títulos ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomearItem(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => n.toString
    case IntervaloContinuo(i, f) => s"$i ${conectorIntervalo(i, f)} $f"
    case ns: Numeros => nomearNumeros(ns, _.toString)
  }

  private def nomearParte(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"Parte $n"
    case IntervaloContinuo(i, f) => s"Partes $i ${conectorIntervalo(i, f)} $f"
    case ns: Numeros => s"Partes ${nomearNumeros(ns, _.toString)}"
  }

  private def nomearCapitulo(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"Capítulo ${formatRomano(n)}"
    case IntervaloContinuo(i, f) => s"Capítulos ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Capítulos ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomearSubSecao(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"Subseção ${formatRomano(n)}"
    case IntervaloContinuo(i, f) => s"Subseções ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Subseções ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomearLivro(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => s"Livro ${formatRomano(n)}"
    case IntervaloContinuo(i, f) => s"Livros ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Livros ${nomearNumeros(ns, formatRomano)}"
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

  private def nomear(parteDispositivo: UrnFragmento): String = parteDispositivo match {
    case a: Artigo => nomearArtigo(a.numeracao)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso => nomearInciso(i.numeracao)
    case a: Alinea => nomearAlinea(a.numeracao)
    case p: Paragrafo => nomearParagrafo(p.numeracao)
    case i: Item => nomearItem(i.numeracao)
    case c: Capitulo => nomearCapitulo(c.numeracao)
    case s: Secao => nomearSecao(s.numeracao)
    case sb: SubSecao => nomearSubSecao(sb.numeracao)
    case l: Livro => nomearLivro(l.numeracao)
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomearTitulo(t.numeracao)
    case p: Parte => nomearParte(p.numeracao)
  }

  private def nomear(partes: List[UrnFragmento]): String = {
    @tailrec
    def criarString(acc: String, partes: List[UrnFragmento]): String = {
      if (partes.isEmpty) {
        acc
      } else {
        partes.head match {
          case h: DispositivoAgrupador if acc.isEmpty => criarString(nomear(h), partes.tail)
          case h: DispositivoAgrupador => criarString(s"${acc} ${h.conector} ${nomear(h)}", partes.tail)
          case h if acc.isEmpty => criarString(nomear(h), partes.tail)
          case h => criarString(s"${acc}, ${nomear(h)}", partes.tail)
        }
      }
    }

    criarString("", partes)
  }

}
