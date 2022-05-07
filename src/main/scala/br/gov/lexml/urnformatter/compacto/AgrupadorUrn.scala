package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.collection.mutable
import scala.util.Try

private[compacto] object AgrupadorUrn {

  def agrupar(parsedUrns: List[ParsedUrn]): List[GrupoUrns] = {
    case class ValueAux(iniComum: String, dispPrincipal: String, numeros: List[Numero], grupos: List[GrupoUrns])

    val v = parsedUrns.tail.foldLeft(
      ValueAux(parsedUrns.head.inicioComum, parsedUrns.head.disPrincipal, List(parsedUrns.head.numero), Nil)
    ) {
      case (value, parsedUrn) =>
        if (parsedUrn.inicioComum.equals(value.iniComum)) {
          value.copy(numeros = value.numeros :+ parsedUrn.numero)
        } else {
          value.copy(
            iniComum = parsedUrn.inicioComum,
            dispPrincipal = parsedUrn.disPrincipal,
            numeros = List(parsedUrn.numero),
            grupos = value.grupos :+ criaGrupo(value.iniComum, value.dispPrincipal, value.numeros)
          )
        }
    }
    v.grupos :+ criaGrupo(v.iniComum, v.dispPrincipal, v.numeros)
  }

  def urnFragmento(fragmentoUrn: String): UrnFragmento =
    parse(fragmentoUrn, (unused: String) => unused {
      0
    })

  private def criaGrupo(iniComum: String, dispPrincipal: String, numeros: List[Numero]): GrupoUrns = {
    val nivelAtualAnexoPorFragmento = mutable.Map[String, Int]()

    val getEAlteraNivel = { p: String =>
      val nivelAtual = nivelAtualAnexoPorFragmento.getOrElse(p, 1)
      nivelAtualAnexoPorFragmento.put(p, nivelAtual + 1)

      nivelAtual
    }

    // 1 aqui nao importa, apenas precisamos adicionar um numero para o parse nao falhar
    val fragmentosComum = (iniComum.concat("1")).split("_").map(parse(_, getEAlteraNivel))
    val fragmentos = (removeUltimoFragmento andThen inverteFragmentosAgrupadores) (fragmentosComum.toList)

    GrupoUrns(fragmentosComum.last.tipo, fragmentos, numeros)
  }

  private def removeUltimoFragmento: List[UrnFragmento] => List[UrnFragmento] = _.dropRight(1)

  // fragmentos agrupadores sao lidos ao contrario
  private def inverteFragmentosAgrupadores: List[UrnFragmento] => List[UrnFragmento] = { fragmentos =>
    var posInicio = Option.empty[Int]
    var posFim = Option.empty[Int]
    fragmentos.zipWithIndex.foreach { case (fragmento, idx) =>
      if (fragmento.tipo.isInstanceOf[DispositivoAgrupador]) {
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
      posFim = Option(fragmentos.length - 1)
    }
    (posInicio, posFim) match {
      case (Some(ini), Some(fim)) =>
        fragmentos.take(ini) ++ fragmentos.slice(ini, fim + 1).reverse ++ fragmentos.slice(fim + 1, fragmentos.length)
      case _ => fragmentos
    }
  }

  private def parse(fragmentoUrn: String, getEAlteraNivel: String => Int): UrnFragmento = fragmentoUrn.take(3) match {
    case "art" =>
      val numStr = fragmentoUrn.substring(3)
      val num = Try(numStr.toInt).map(Numero.IntNumero).getOrElse(Numero.StrNumero(numStr))
      Artigo(List(num))
    case "cpt" => Caput
    case "par" if fragmentoUrn.contains("par1u") => ParagrafoUnico
    case "par" => Paragrafo(List(parseNumero(fragmentoUrn)))
    case "inc" => Inciso(List(parseNumero(fragmentoUrn)))
    case "ali" => Alinea(List(parseNumero(fragmentoUrn)))
    case "ite" => Item(List(parseNumero(fragmentoUrn)))
    case "tit" => Titulo(List(parseNumero(fragmentoUrn)))
    case "cap" => Capitulo(List(parseNumero(fragmentoUrn)))
    case "sec" => Secao(List(parseNumero(fragmentoUrn)))
    case "sub" => SubSecao(List(parseNumero(fragmentoUrn)))
    case "liv" => Livro(List(parseNumero(fragmentoUrn)))
    case "anx" => Anexo(List(parseNumero(fragmentoUrn)), getEAlteraNivel("anx"))
    case "prt" => Parte(List(parseNumero(fragmentoUrn)))
    case _ => throw new IllegalArgumentException(s"Urn Invalida: $fragmentoUrn")
  }

  private def parseNumero(fragmento: String): Numero = {
    val numero = fragmento.substring(3)
    Try(Numero.IntNumero(numero.toInt)).getOrElse {
      if (fragmento.contains("anx")) {
        Numero.StrNumero(numero)
      } else {
        Numero.SemNumero
      }
    }
  }

}
