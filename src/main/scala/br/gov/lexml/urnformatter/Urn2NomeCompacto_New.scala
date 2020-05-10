package br.gov.lexml.urnformatter


import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.{Success, Try}

//TODO: Teste/verificar se algo quebra com dispositivo com numero grande (1000 e pk por exemplo)
//TODO: teste com multiplo que tem caput
object Urn2NomeComposto {

  import Urn2Format._
  import Numeracao._
  import ParteDispositivoGrupo._

  sealed abstract class Numero

  object Numero {

    case class IntNumero(n: Int) extends Numero

    case class StrNumero(s: String) extends Numero

    case object Unico extends Numero

    case object SemNumero extends Numero

  }


  sealed abstract class Numeracao

  object Numeracao {

    case class IntervaloContinuo(inicio: Int, fim: Int) extends Numeracao

    case class Numeros(list: List[Int]) extends Numeracao

    case class NumUnico(n: Numero) extends Numeracao //TODO: UmNumero?

  }

  case class UrnGrupo(inicioComum: String, disPrincipal: String, numero: Numero)

  case class Grupo(dispPrincipal: String, partesComum: List[ParteDispositivoGrupo], numeracao: Numeracao)

  object ParteDispositivoGrupo {

    trait DispositivoAgrupador {
      val conector: String
    }

    sealed abstract class ParteDispositivoGrupo

    case class Artigo(numeracao: Numeracao) extends ParteDispositivoGrupo

    //
    case object Caput extends ParteDispositivoGrupo

    case object ParagrafoUnico extends ParteDispositivoGrupo

    case class Inciso(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Alinea(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Paragrafo(numeracao: Numeracao) extends ParteDispositivoGrupo

    case class Item(numeracao: Numeracao) extends ParteDispositivoGrupo

    //TODO: Parte
    //TODO: Alt

    case class Titulo(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Capitulo(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Secao(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "da"
    }

    case class SubSecao(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "da"
    }

    case class Livro(numeracao: Numeracao) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

    case class Anexo(numeracao: Numeracao, nivel: Int) extends ParteDispositivoGrupo with DispositivoAgrupador {
      override val conector: String = "do"
    }

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
    // case NumUnico(n) => s"Título ${formatRomano(n)}"
    // case IntervaloContinuo(i, f) => s"Título ${formatRomano(i)} a ${formatRomano(f)}"
    case ns: Numeros => s"Títulos ${nomearNumeros(ns, formatRomano)}"
  }

  private def nomearItem(n: Numeracao): String = n match {
    case NumUnico(Numero.IntNumero(n)) => n.toString
    case IntervaloContinuo(i, f) => s"$i ${conectorIntervalo(i, f)} $f"
    case ns: Numeros => nomearNumeros(ns, _.toString)
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

  private def nomear(parteDispositivo: ParteDispositivoGrupo): String = parteDispositivo match {
    case a: Artigo => nomearArtigo(a.numeracao)
    case Caput => "caput"
    case ParagrafoUnico => "parágrafo único"
    case i: Inciso => nomearInciso(i.numeracao)
    case a: Alinea => nomearAlinea(a.numeracao)
    case p: Paragrafo => nomearParagrafo(p.numeracao)
    case i: Item => nomearItem(i.numeracao)
    case Titulo(NumUnico(Numero.IntNumero(n))) => s"Título ${formatRomano(n)}"
    case Capitulo(NumUnico(Numero.IntNumero(n))) => s"Capítulo ${formatRomano(n)}"
    case s: Secao => nomearSecao(s.numeracao)
    case SubSecao(NumUnico(Numero.IntNumero(n))) => s"Subseção ${formatRomano(n)}"
    case Livro(NumUnico(Numero.IntNumero(n))) => s"Livro ${formatRomano(n)}"
    case a: Anexo => nomearAnexo(a)
    case t: Titulo => nomearTitulo(t.numeracao)
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


      def trataArtigo: List[String] => List[String] = { partes =>
        val posArtigo = partes.indexWhere(_.startsWith("art"))

        if (posArtigo != -1) {
          partes.filter(p => p.startsWith("art") || p.startsWith("anx")) ++ partes.takeRight(partes.size - posArtigo - 1)
        } else {
          partes
        }
      }

      // se tem caput antes do final, remove ele
      def trataCaputNoMeio: List[String] => List[String] = { partes =>
        val contemCaputAntesDoFim = partes.dropRight(1).exists(_.startsWith("cpt"))

        if (contemCaputAntesDoFim) {
          partes.filter(!_.startsWith("cpt"))
        } else {
          partes
        }
      }

      def removeRaizEComponentePrincipal: List[String] => List[String] = { partes =>
        partes.filterNot(p => p.startsWith("lex") || p.startsWith("cpp"))
      }

      val urnsGrupo = urns.map { urn =>
        val partesStr = (trataArtigo andThen trataCaputNoMeio andThen removeRaizEComponentePrincipal) (urn.split("_").toList)
        val ultimaParte = partesStr.last
        val dispPrincipal = if (!ultimaParte.contains("par1u")) ultimaParte.take(3) else "par1u"
        val inicioComum = partesStr.dropRight(1) :+ dispPrincipal
        val numero = if (ultimaParte.size == 3) {
          Numero.SemNumero
        } else if (ultimaParte.endsWith("1u")) {
          Numero.Unico
        } else {
          val numeroStr = ultimaParte.drop(3)
          Try(numeroStr.toInt) match {
            case Success(i) => Numero.IntNumero(i)
            case _ => Numero.StrNumero(numeroStr)
          }
        }
        UrnGrupo(inicioComum.mkString("_"), dispPrincipal, numero)
      }

      def parse(parteUrn: String, getEAlteraNivel: String => Int): ParteDispositivoGrupo = parteUrn.take(3) match {
        case "art" =>
          val numStr = parteUrn.substring(3)
          val num = Try(numStr.toInt).map(Numero.IntNumero).getOrElse(Numero.StrNumero(numStr))
          Artigo(NumUnico(num))
        case "cpt" => Caput
        case "par" if parteUrn.contains("par1u") => ParagrafoUnico
        case "par" => Paragrafo(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "inc" => Inciso(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "ali" => Alinea(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "ite" => Item(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "tit" => Titulo(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "cap" => Capitulo(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "sec" => Secao(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "sub" => SubSecao(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "liv" => Livro(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
        case "anx" => Anexo(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)), getEAlteraNivel("anx"))
        case _ => throw new IllegalArgumentException(s"Invalid urn: $parteUrn")
      }

      def removeUltimo: List[ParteDispositivoGrupo] => List[ParteDispositivoGrupo] = { partes =>
        partes.dropRight(1)
      }

      def inverteFragmentosAgrupadores: List[ParteDispositivoGrupo] => List[ParteDispositivoGrupo] = { partes =>
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

      def criaGrupos(iniComum: String, dispPrincipal: String, numeros: List[Numero]): List[Grupo] = {
        println(s"==> iniComum: $iniComum")
        println(s"==> dispPrincipal: $dispPrincipal")
        println(s"==> numeros: $numeros")
        var currNumeros = mutable.ListBuffer[Int]()
        var numeracoes = mutable.ListBuffer[Numeracao]()
        numeros.zipWithIndex.foreach { case (n, idx) =>
          n match {
            case u@Numero.Unico => numeracoes += Numeracao.NumUnico(u)
            case s: Numero.StrNumero =>
              if (currNumeros.nonEmpty) {
                if (currNumeros.size == 1) {
                  numeracoes += NumUnico(Numero.IntNumero(currNumeros.head))
                  currNumeros = mutable.ListBuffer[Int]()
                } else if (currNumeros.size == 2) {
                  numeracoes += Numeros(currNumeros.toList)
                  currNumeros = mutable.ListBuffer[Int]()
                } else {
                  numeracoes += IntervaloContinuo(currNumeros.head, currNumeros.last)
                  currNumeros = mutable.ListBuffer[Int]()
                }
              }
              numeracoes += Numeracao.NumUnico(s)
            case sn@Numero.SemNumero => numeracoes += Numeracao.NumUnico(sn)
            case Numero.IntNumero(nInt) =>
              if (currNumeros.isEmpty) {
                currNumeros += nInt
              } else {
                if (currNumeros.last + 1 == nInt) {
                  currNumeros += nInt
                } else {
                  if (currNumeros.size == 1) {
                    if (idx + 1 < numeros.size) {
                      if (numeros(idx + 1).isInstanceOf[Numero.IntNumero] &&
                        nInt + 1 == numeros(idx + 1).asInstanceOf[Numero.IntNumero].n) {
                        numeracoes += NumUnico(Numero.IntNumero(currNumeros.head))
                        currNumeros = mutable.ListBuffer[Int]()
                        currNumeros += nInt
                      } else {
                        currNumeros += nInt
                      }
                    } else {
                      currNumeros += nInt
                    }
                  } else if (currNumeros.size == 2) {
                    numeracoes += Numeros(currNumeros.toList)
                    currNumeros = mutable.ListBuffer[Int]()
                    currNumeros += nInt
                  } else {
                    numeracoes += IntervaloContinuo(currNumeros.head, currNumeros.last)
                    currNumeros = mutable.ListBuffer[Int]()
                    currNumeros += nInt
                  }
                }
              }
          }
        }
        if (currNumeros.nonEmpty) {
          if (currNumeros.size == 1) {
            numeracoes += NumUnico(Numero.IntNumero(currNumeros.head))
          } else if (currNumeros.size == 2) {
            numeracoes += Numeros(currNumeros.toList)
          } else {
            numeracoes += IntervaloContinuo(currNumeros.head, currNumeros.last)
          }
        }

        var nivelAtualAnexoPorParte = mutable.Map[String, Int]()

        def getEAlteraNivel(p: String): Int = {
          val nivelAtual = nivelAtualAnexoPorParte.getOrElse(p, 1)
          nivelAtualAnexoPorParte.put(p, nivelAtual + 1)

          nivelAtual
        }

        val partesComum = (iniComum.concat("1")).split("_").map(parse(_, getEAlteraNivel))
        val partes = (removeUltimo andThen inverteFragmentosAgrupadores) (partesComum.toList)

        //atualiza nivel anexo
        //        val partesA = partes.map { _ match {
        //          case a: Anexo => Anexo(a.numeracao, getEAlteraNivel("anx"))
        //          case p => p
        //        }}

        numeracoes.map { num =>
          Grupo(dispPrincipal, partes, num)
        }.toList
      }

      var iniComum = urnsGrupo.head.inicioComum
      var dispPrincipal = urnsGrupo.head.disPrincipal
      var numeros = mutable.ListBuffer[Numero]()
      numeros += urnsGrupo.head.numero
      val acc = mutable.ListBuffer[Grupo]()

      urnsGrupo.tail.foreach { ug =>
        if (ug.inicioComum.equals(iniComum)) {
          numeros += ug.numero
        } else {
          acc ++= criaGrupos(iniComum, dispPrincipal, numeros.toList)
          numeros = mutable.ListBuffer[Numero]()
          numeros += ug.numero
          iniComum = ug.inicioComum
          dispPrincipal = ug.disPrincipal
        }
      }
      acc ++= criaGrupos(iniComum, dispPrincipal, numeros.toList)

      println("==> urnsGrupo")
      println(urnsGrupo)
      println("==> grupos")
      println(acc.foreach(println))

      nomearGrupos(acc.toList)
    }
  }

  private def nomearGrupos(grupos: List[Grupo]): String = {
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
    } else {
      ???
    }
    res
  }
}

object Urn2NomeCompacto_New {

  //TODO: Urn
  //TODO: Fragmento ao inves de Parte
  //TODO: Precisa recursao nos nomear
  def format(urn: String): String = {
    format(List(urn))
  }

  def format(urns: List[String]): String = {
    Urn2NomeComposto.format(urns)
  }

}
