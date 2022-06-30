package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.collection.mutable
import scala.util.Try

private[compacto] object AgrupadorUrn {

  def agrupar(parsedUrns: List[ParsedUrn]): List[GrupoUrns] = {
    println(s"agrupar: ${parsedUrns.mkString(",")}")
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
            grupos = value.grupos :+ criaGrupo(value.iniComum, value.numeros)
          )
        }
    }
    v.grupos :+ criaGrupo(v.iniComum, v.numeros)
  }

  def urnFragmento(fragmentoUrn: String): UrnFragmento =
    parse(fragmentoUrn, (unused: String) => unused {
      0
    })

  private def criaGrupo(iniComum: String, numeros: List[Numero]): GrupoUrns = {
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
    println(s"inverteFragmentosAgrupadores: $fragmentos")
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
    case TipoUrnFragmento.Artigo.sigla =>
      val numStr = fragmentoUrn.substring(3)
      val num = Try(numStr.toInt).map(Numero.IntNumero).getOrElse(Numero.StrNumero(numStr))
      Artigo(List(num))
    case TipoUrnFragmento.Caput.sigla => Caput
    case TipoUrnFragmento.Paragrafo.sigla if fragmentoUrn.contains("par1u") => ParagrafoUnico
    case TipoUrnFragmento.Paragrafo.sigla => Paragrafo(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Inciso.sigla => Inciso(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Alinea.sigla => Alinea(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Item.sigla => Item(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Titulo.sigla => Titulo(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Capitulo.sigla => Capitulo(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Secao.sigla => Secao(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.SubSecao.sigla => SubSecao(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Livro.sigla => Livro(List(parseNumero(fragmentoUrn)))
    case TipoUrnFragmento.Anexo.sigla => Anexo(List(parseNumero(fragmentoUrn)), getEAlteraNivel(TipoUrnFragmento.Anexo.sigla))
    case TipoUrnFragmento.Parte.sigla => Parte(List(parseNumero(fragmentoUrn)))
    case _ => throw new IllegalArgumentException(s"Urn Invalida: $fragmentoUrn")
  }

  private def parseNumero(fragmento: String): Numero = {
    val numero = fragmento.substring(3)
    Try(Numero.IntNumero(numero.toInt)).getOrElse {
      if (fragmento.contains(TipoUrnFragmento.Anexo.sigla)) {
        Numero.StrNumero(numero)
      } else {
        Numero.SemNumero
      }
    }
  }

}
