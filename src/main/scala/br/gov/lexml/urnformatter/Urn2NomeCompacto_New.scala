package br.gov.lexml.urnformatter

import br.gov.lexml.urnformatter.Urn2NomeCompacto_New.Dispositivo

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
  }

  private def nomear(dispositivo: Dispositivo): String = {

    def nomearSimples(dispositivo: Dispositivo): String = {
      def go(acc: List[String], partes: List[ParteDispositivo]): List[String] = {
        if (partes.isEmpty) {
          acc
        } else {
          go(acc :+ nomear(partes.head), partes.tail)
        }
      }

      // se tem caput no meio do artigo, remove ele
      val partes = dispositivo.partes.head match {
        case _: Artigo =>
          val contemCaput = dispositivo.partes.dropRight(1).exists {
            case Caput => true
            case _ => false
          }
          if (contemCaput) {
            dispositivo.partes.filter {
              case Caput => false
              case _ => true
            }
          } else {
            dispositivo.partes
          }
        case _ => dispositivo.partes
      }

      go(Nil, partes).mkString(", ")
    }

    def nomearAgrupador(dispositivo: Dispositivo): String = {

      def go(acc: String, partes: List[ParteDispositivo]): String = {
        if (partes.isEmpty) {
          acc
        } else {
          if (acc.isEmpty) {
            go(nomear(partes.head), partes.tail)
          } else {
            partes.head match {
              case _: Capitulo | _: Titulo | _: Livro | _: Anexo => go(acc + " do " + nomear(partes.head), partes.tail)
              case _: Secao | _: SubSecao =>go(acc + " da " + nomear(partes.head), partes.tail)
              case _ => throw new IllegalArgumentException(s"ParteDispositivo ${partes.head} nao tratada")
            }

          }
        }
      }

      val partes = dispositivo.partes.filter {
          case _:Artigo => false
          case _ => true
      }.reverse
      val res = go("", partes)

      val maybeArtigo = dispositivo.partes.find {
        case _:Artigo => true
        case _ => false
      }

      maybeArtigo match {
        case Some(a) => res + s", ${nomear(a)}"
        case None => res
      }
    }

    val ehAgrupador = dispositivo.partes.exists {
        case _:Capitulo | _:Livro => true
        case _ => false
    }
    if (ehAgrupador) nomearAgrupador(dispositivo) else nomearSimples(dispositivo)
  }

  object ParteDispositivo {

    sealed abstract class ParteDispositivo

    case class Artigo(numero: Int) extends ParteDispositivo

    case object Caput extends ParteDispositivo

    case object ParagrafoUnico extends ParteDispositivo

    case class Inciso(numero: Int) extends ParteDispositivo

    case class Alinea(numero: Int) extends ParteDispositivo

    case class Paragrafo(numero: Int) extends ParteDispositivo

    case class Item(numero: Int) extends ParteDispositivo

    case class Titulo(numero: Int) extends ParteDispositivo

    case class Capitulo(numero: Int) extends ParteDispositivo

    case class Secao(numero: Int) extends ParteDispositivo

    case class SubSecao(numero: Int) extends ParteDispositivo

    case class Livro(numero: Int) extends ParteDispositivo

    case class Anexo(numero: Int) extends ParteDispositivo

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
      case _ => throw new IllegalArgumentException(s"Invalid urn: $parteUrn")
    }

  }

  case class Dispositivo(partes: List[ParteDispositivo])


}
