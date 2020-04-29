package br.gov.lexml.urnformatter

import br.gov.lexml.urnformatter.Urn2NomeCompacto_New.Dispositivo

import scala.annotation.tailrec

object Urn2NomeCompacto_New {

  import ParteDispositivo._
  import Urn2Format._


  //  def format(urnsFrag: List[String]): String = {
  //    ???
  //  }

  //TODO: Urn
  //TODO: Fragmento ao inves de Parte
  //TODO: Precisa recursao nos nomear
  def format(urnFrag: String): String = {
    val partes = urnFrag.split("_").map(ParteDispositivo.parse).toList
    val dispositivo = Dispositivo(partes)
    println(s"==> $dispositivo")
    nomear(dispositivo)
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

    def trataArtigo: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
      val contemArtigo = partes.exists {
        case _: Artigo => true
        case _ => false
      }

      if (contemArtigo) {
        partes.dropWhile {
          case _: Artigo => false
          case _ => true
        }
      } else {
        partes
      }
    }

    // se tem caput antes do final, remove ele
    def trataCaputNoMeio: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
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

    def inverteFragmentosAgrupadores: List[ParteDispositivo] => List[ParteDispositivo] = { partes =>
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


}
