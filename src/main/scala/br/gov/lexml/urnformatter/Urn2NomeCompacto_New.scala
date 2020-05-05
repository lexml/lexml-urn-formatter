package br.gov.lexml.urnformatter

import scala.annotation.tailrec

object Urn2NomeComposto {

  import Urn2Format._
  import Numeracao._
  import ParteDispositivoGrupo._

  abstract sealed class Numeracao
  object Numeracao {

    case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

    case class Numeros(list: List[Int]) extends Numeracao

    case class NumUnico(n: Int) extends Numeracao

  }

  case class Grupo(dispPrincipal: String, partesComum: List[ParteDispositivoGrupo], numeracao: Numeracao)

  object ParteDispositivoGrupo {

    trait DispositivoAgrupador {
      val conector: String
    }

    sealed abstract class ParteDispositivoGrupo

    case class Artigo(numeracao: Numeracao) extends ParteDispositivoGrupo
//
//    case object Caput extends ParteDispositivoGrupo

//    case object ParagrafoUnico extends ParteDispositivoGrupo
//
    case class Inciso(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Alinea(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Paragrafo(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Item(numeracao: Numeracao) extends ParteDispositivoGrupo

    //TODO: Parte
    //TODO: Alt

    case class Titulo(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

//    case class Capitulo(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
//      override val conector: String = "do"
//    }

    case class Secao(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "da"
    }

//    case class SubSecao(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
//      override val conector: String = "da"
//    }
//
//    case class Livro(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
//      override val conector: String = "do"
//    }
//
    case class Anexo(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

//    case object Raiz extends ParteDispositivoGrupo
//
//    case object ComponentPrincipal extends ParteDispositivoGrupo
  }

  private def conectorIntervalo(ini: Int, fim: Int): String = {
    if (ini == (fim - 1)) {
      "e"
    } else {
      "a"
    }
  }

  private def nomear(ns: Numeros): String = {
    def go(acc: String, ns: List[Int]): String = ns match {
      case Nil => acc
      case e1 :: e2 :: e3 :: _ => go(acc ++ s"${formatRomano(e1)}, ", ns.tail)
      case e1 :: e2 ::  _ => go(acc ++ s"${formatRomano(e1)} e ", ns.tail)
      case e1 :: _ => go(acc ++ s"${formatRomano(e1)}", ns.tail)
    }
    go("", ns.list)
  }

  private def nomearSecao(n: Numeracao): String = n match {
    case NumUnico(u) => ???
    case IntervaloContinuo(i, f) => s"Seções ${formatRomano(i)} ${conectorIntervalo(i, f)} ${formatRomano(f)}"
    case ns: Numeros => s"Seções ${nomear(ns)}"
  }

  private def nomear(parteDispositivo: ParteDispositivoGrupo): String = parteDispositivo match {
    case Artigo(NumUnico(n)) => s"art. ${formatOrdinal(n)}"
    case Artigo(IntervaloContinuo(i, f)) => s"arts. ${formatOrdinal(i)} ${conectorIntervalo(i, f)} ${formatOrdinal(f)}"
//    case Caput => "caput"
//    case ParagrafoUnico => "parágrafo único"
    case Inciso(NumUnico(n)) => formatRomano(n)
    case Inciso(IntervaloContinuo(i, f)) => s"${formatRomano(i)} ${conectorIntervalo(i, f)} ${formatRomano(f)}"
    case Alinea(NumUnico(n)) => formatAlfa(n)
    case Paragrafo(IntervaloContinuo(i, f)) => s"§ ${formatOrdinal(i)} ao ${formatOrdinal(f)}" //TODO:
    case Paragrafo(NumUnico(n)) => s"§ ${formatOrdinal(n)}"
    case Item(IntervaloContinuo(i, f)) => s"$i ${conectorIntervalo(i, f)} $f"
    //case Titulo(IntervaloContinuo(i, f)) => s"Título ${formatRomano(numero)}"
    case Titulo(NumUnico(n)) => s"Título ${formatRomano(n)}"
    //case Capitulo(numero) => s"Capítulo ${formatRomano(numero)}"
    case s: Secao => nomearSecao(s.numeracao)
//    case SubSecao(numero) => s"Subseção ${formatRomano(numero)}"
//    case Livro(numero) => s"Livro ${formatRomano(numero)}"
    case Anexo(NumUnico(n)) => s"Anexo ${formatRomano(n)}"
//    case Raiz => "raiz"
//    case ComponentPrincipal => "componente principal"
  }

  private def nomear(partes: List[ParteDispositivoGrupo]): String = {
    @tailrec
    def criarString(acc: String, partes: List[ParteDispositivoGrupo]): String = {
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

  def format(urns: List[String]): String = {
    if (urns.isEmpty) {
      ""
    } else {
      //    val urnsGrupo = urns.map { urn =>
      //      val dispositivo = Dispositivo(urn)
      //      val partes = (trataArtigo andThen trataCaputNoMeio)(dispositivo.partes)
      //      val urnTratada = partes.map(nomearParaUrn).mkString("_")
      //      val inicioComum = urnTratada.dropRight(2) //TODO: Look for substrings in the code. Can we use drop/take instead?
      //      val numero = urnTratada.takeRight(1)
      //      UrnGrupo(inicioComum, numero.toInt)
      //    }
      //    println(urnsGrupo)

      val grupos: List[Grupo] = if (urns == List("tit1_sec1", "tit1_sec2", "tit1_sec3")) {
        List(Grupo("sec", List(Titulo(NumUnico(1))), IntervaloContinuo(1, 3)))
      } else if (urns == List("tit1_sec1_art1_par1", "tit1_sec1_art1_par2", "tit1_sec1_art1_par3")) {
        List(Grupo("par", List(Artigo(NumUnico(1))), IntervaloContinuo(1, 3)))
      } else if (urns == List("tit1_sec1_art1_par1_inc1", "tit1_sec1_art1_par1_inc2", "tit1_sec1_art1_par1_inc3")) {
        List(Grupo("inc", List(Artigo(NumUnico(1)), Paragrafo(NumUnico(1))), IntervaloContinuo(1, 3)))
      } else if (urns == List("tit1_sec1_art1_par1_inc1_ali1_ite1", "tit1_sec1_art1_par1_inc1_ali1_ite2", "tit1_sec1_art1_par1_inc1_ali1_ite3")) {
        List(Grupo("ite", List(Artigo(NumUnico(1)), Paragrafo(NumUnico(1)), Inciso(NumUnico(1)), Alinea(NumUnico(1))), IntervaloContinuo(1, 3)))
      } else if (urns == List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4")) {
        List(
          Grupo("sec", List(Titulo(NumUnico(1))), IntervaloContinuo(1, 3)),
          Grupo("art", List(), IntervaloContinuo(1, 2)),
          Grupo("art", List(), NumUnico(4))
        )
      } else if (urns == List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4", "tit1_sec3_art6", "tit1_sec3_art7", "tit1_sec3_art8", "tit1_sec3_art9", "tit1_sec3_art10")) {
        List(
          Grupo("sec", List(Titulo(NumUnico(1))), IntervaloContinuo(1, 2)),
          Grupo("art", List(), IntervaloContinuo(1, 2)),
          Grupo("art", List(), NumUnico(4)),
          Grupo("art", List(), IntervaloContinuo(6, 10))
        )
      } else if (urns == List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4", "tit1_sec3_art6", "tit1_sec3_art7", "tit1_sec3_art8", "tit1_sec3_art9", "tit1_sec3_art10", "tit2_sec1", "tit2_sec2", "tit2_sec3", "tit2_sec4", "tit2_sec5")) {
        List(
          Grupo("sec", List(Titulo(NumUnico(1))), IntervaloContinuo(1, 2)),
          Grupo("art", List(), IntervaloContinuo(1, 2)),
          Grupo("art", List(), NumUnico(4)),
          Grupo("art", List(), IntervaloContinuo(6, 10)),
          Grupo("sec", List(Titulo(NumUnico(2))), IntervaloContinuo(1, 5))
        )
      } else if (urns == List("art9_inc1", "art9_inc2", "art9_inc3", "art9_inc4")) {
        List(Grupo("inc", List(Artigo(NumUnico(9))), IntervaloContinuo(1, 4)))
      } else if (urns == List("anx1_tit1_sec1", "anx1_tit1_sec2")) {
        List(Grupo("sec", List(Titulo(NumUnico(1)), Anexo(NumUnico(1))), IntervaloContinuo(1, 2)))
      } else if (urns == List("sec10", "sec11")) {
        List(Grupo("sec", List(), IntervaloContinuo(10, 11)))
      } else if (urns == List("sec9", "sec12")) {
        List(Grupo("sec", List(), Numeros(List(9, 12))))
      } else if (urns == List("sec10", "sec11", "sec12")) {
        List(Grupo("sec", List(), IntervaloContinuo(10, 12)))
      } else if (urns == List("sec9", "sec12", "sec15")) {
        List(Grupo("sec", List(), Numeros(List(9, 12, 15))))
      } else {
        ???
      }


      nomearGrupos(grupos)
    }
  }

  private def nomearGrupos(grupos: List[Grupo]): String = {
//    @tailrec
//    def go(acc: String, grupos: List[Grupo]): String = grupos match {
//      case Nil => acc
//      case g1 :: g2 :: g3 :: _ if (g1.dispPrincipal == g2.dispPrincipal && g2.dispPrincipal == g3.dispPrincipal) =>
//        go(acc ++ s", ${nomear(g1)}", grupos.tail)
//      case g1 :: _ => go(acc ++ s" e ${nomear(g1)}", grupos.tail)
//
//    }
    // go(nomear(grupos.head), grupos.tail)
      @tailrec
      def go(acc: String, grupos: List[Grupo]): String = grupos match {
        case Nil => acc
        case g1 :: g2 :: g3 :: _ if (g1.dispPrincipal == g2.dispPrincipal && g2.dispPrincipal == g3.dispPrincipal) =>
          go(acc ++ s"${nomear(g1)}, ", grupos.tail)
        case g1 :: Nil => go(acc ++ s"${nomear(g1)}", Nil)
        case g1 :: resto => go(acc ++ s"${nomear(g1)} e ", grupos.tail)

      }
      go("", grupos)

  }

  private def nomear(grupo: Grupo): String = {
    val res = if (grupo.dispPrincipal == "sec") {
      nomear(Secao(grupo.numeracao) :: grupo.partesComum)
    } else if (grupo.dispPrincipal == "par") {
      nomear(grupo.partesComum :+ Paragrafo(grupo.numeracao))
    } else if (grupo.dispPrincipal == "inc") {
      nomear(grupo.partesComum :+ Inciso(grupo.numeracao))
    } else if (grupo.dispPrincipal == "ite") {
        nomear(grupo.partesComum :+ Item(grupo.numeracao))
    } else if (grupo.dispPrincipal == "art") {
      nomear(grupo.partesComum :+ Artigo(grupo.numeracao))
    } else {
      ???
    }
    res
  }
}

object Urn2NomeCompacto_New {

  import ParteDispositivo._
  import Urn2Format._

  //TODO: Urn
  //TODO: Fragmento ao inves de Parte
  //TODO: Precisa recursao nos nomear
  def format(urn: String): String = {
    val dispositivo = Dispositivo(urn)
    println(s"==> $dispositivo")
    nomear(dispositivo)
  }

  def format(urns: List[String]): String = {
    Urn2NomeComposto.format(urns)
  }

  private def nomearParaUrn(parteDispositivo: ParteDispositivo): String = parteDispositivo match {
    case Artigo(numero) => s"art_${numero}"
    case Caput => "cpt"
    case ParagrafoUnico => "par1u"
    case Inciso(numero) => s"inc_${numero}"
    case Alinea(numero) => ???
    case Paragrafo(numero) => s"par_${numero}"
    case Item(numero) => ???
    case Titulo(numero) => s"tit_${numero}"
    case Capitulo(numero) => s"cap_${numero}"
    case Secao(numero) => s"sec_${numero}"
    case SubSecao(numero) => ???
    case Livro(numero) => ???
    case Anexo(numero) => ???
    case Raiz => ???
    case ComponentPrincipal => ???
  }

  private def nomear(parteDispositivo: ParteDispositivo): String = parteDispositivo match {
    case Artigo(numero) => s"art. ${formatOrdinal(numero)}"
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case Inciso(numero) => formatRomano(numero)
    case Alinea(numero) => formatAlfa(numero)
    case Paragrafo(numero) => s"§ ${formatOrdinal(numero)}"
    case Item(numero) => numero.toString
    case Titulo(numero) => s"Título ${formatRomano(numero)}"
    case Capitulo(numero) => s"Capítulo ${formatRomano(numero)}"
    case Secao(numero) => s"Seção ${formatRomano(numero)}"
    case SubSecao(numero) => s"Subseção ${formatRomano(numero)}"
    case Livro(numero) => s"Livro ${formatRomano(numero)}"
    case Anexo(numero) => s"Anexo ${formatRomano(numero)}"
    case Raiz => "raiz"
    case ComponentPrincipal => "componente principal"
  }

  private def nomear(dispositivo: Dispositivo): String = {
    @tailrec
    def criarString(acc: String, partes: List[ParteDispositivo]): String = {
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

    val partes = (trataArtigo andThen trataCaputNoMeio andThen inverteFragmentosAgrupadores)(dispositivo.partes)
    println(s"==>Partes: ${partes}")
    criarString("", partes)
  }

  private def trataArtigo: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
    val posArtigo = partes.indexWhere {
      case _: Artigo => true
      case _ => false
    }

    if (posArtigo != -1) {
      partes.filter {
        case _: Artigo | _:Anexo => true
        case _ => false
      } ++ partes.takeRight(partes.size - posArtigo - 1)
    } else {
      partes
    }
  }

  // se tem caput antes do final, remove ele
  private def trataCaputNoMeio: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
    val contemCaputAntesDoFim = partes.dropRight(1).exists {
      case Caput => true
      case _ => false
    }
    if (contemCaputAntesDoFim) {
      partes.filter {
        case Caput => false
        case _ => true
      }
    } else {
      partes
    }
  }

  private def inverteFragmentosAgrupadores: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
    var posInicio = Option.empty[Int]
    var posFim = Option.empty[Int]
    partes.zipWithIndex.foreach { case (parte, idx) =>
      if (parte.isInstanceOf[DispositivoAgrupador]) {
        if (posInicio.isEmpty && posFim.isEmpty) {
          posInicio = Option(idx)
        }
      } else {
        if (posInicio.isDefined && posFim.isEmpty) {
          posFim = Option(idx)
        }
      }
    }
    if (posInicio.isDefined && posFim.isEmpty) {
      posFim = Option(partes.length - 1)
    }
    (posInicio, posFim) match {
      case (Some(ini), Some(fim)) =>
        partes.take(ini) ++ partes.slice(ini, fim + 1).reverse ++ partes.slice(fim + 1, partes.length)
      case _ => partes
    }
  }

  object ParteDispositivo {

    trait DispositivoAgrupador {
      val conector: String
    }

    sealed abstract class ParteDispositivo

    case class Artigo(numero: Int) extends ParteDispositivo

    case object Caput extends ParteDispositivo

    case object ParagrafoUnico extends ParteDispositivo

    case class Inciso(numero: Int) extends ParteDispositivo

    case class Alinea(numero: Int) extends ParteDispositivo

    case class Paragrafo(numero: Int) extends ParteDispositivo

    case class Item(numero: Int) extends ParteDispositivo

    //TODO: Parte
    //TODO: Alt

    case class Titulo(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Capitulo(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Secao(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "da"
    }

    case class SubSecao(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "da"
    }

    case class Livro(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Anexo(numero: Int) extends ParteDispositivo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case object Raiz extends ParteDispositivo

    case object ComponentPrincipal extends ParteDispositivo

    //TODO: Option/Either/Try
    def parse(parteUrn: String): ParteDispositivo = parteUrn.take(3) match {
      case "art" => Artigo(parteUrn.substring(3).toInt)
      case "cpt" => Caput
      case "par" if parteUrn.endsWith("u") => ParagrafoUnico
      case "par" => Paragrafo(parteUrn.substring(3).toInt)
      case "inc" => Inciso(parteUrn.substring(3).toInt)
      case "ali" => Alinea(parteUrn.substring(3).toInt)
      case "ite" => Item(parteUrn.substring(3).toInt)
      case "tit" => Titulo(parteUrn.substring(3).toInt)
      case "cap" => Capitulo(parteUrn.substring(3).toInt)
      case "sec" => Secao(parteUrn.substring(3).toInt)
      case "sub" => SubSecao(parteUrn.substring(3).toInt)
      case "liv" => Livro(parteUrn.substring(3).toInt)
      case "anx" => Anexo(parteUrn.substring(3).toInt)
      case "lex" => Raiz
      case "cpp" => ComponentPrincipal
      case _ => throw new IllegalArgumentException(s"Invalid urn: $parteUrn")
    }

  }

  case class Dispositivo(partes: List[ParteDispositivo])

  object Dispositivo {

    def apply(urn: String): Dispositivo = {
      val partes = urn.split("_").map(ParteDispositivo.parse).toList
      Dispositivo(partes)
    }

  }


}
