package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.Numeracao._
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.collection.mutable
import scala.util.Try

private[compacto] object AgrupadorUrn {

  def agrupar(parsedUrns: List[ParsedUrn]): List[GrupoUrns] = {

    case class Value(iniComum: String, dispPrincipal: String, numeros: List[Numero], grupos: List[GrupoUrns])

    val v = parsedUrns.tail.foldLeft(
      Value(parsedUrns.head.inicioComum, parsedUrns.head.disPrincipal, List(parsedUrns.head.numero), Nil)
    ) {
      case (value, parsedUrn) =>
        if (parsedUrn.inicioComum.equals(value.iniComum)) {
          value.copy(numeros = value.numeros :+ parsedUrn.numero)
        } else {
          value.copy(
            iniComum = parsedUrn.inicioComum,
            dispPrincipal = parsedUrn.disPrincipal,
            numeros = List(parsedUrn.numero),
            grupos = value.grupos ++ criaGrupos(value.iniComum, value.dispPrincipal, value.numeros)
          )
        }
    }
    v.grupos ++ criaGrupos(v.iniComum, v.dispPrincipal, v.numeros)
  }

  private def criaGrupos(iniComum: String, dispPrincipal: String, numeros: List[Numero]): List[GrupoUrns] = {
    println(s"==> iniComum: $iniComum")
    println(s"==> dispPrincipal: $dispPrincipal")
    println(s"==> numeros: $numeros")

    val nivelAtualAnexoPorFragmento = mutable.Map[String, Int]()

    val getEAlteraNivel = { p: String =>
      val nivelAtual = nivelAtualAnexoPorFragmento.getOrElse(p, 1)
      nivelAtualAnexoPorFragmento.put(p, nivelAtual + 1)

      nivelAtual
    }

    // 1 aqui nao importa, apenas precisamos adicionar um numero para o parse nao falhar
    val fragmentosComum = (iniComum.concat("1")).split("_").map(parse(_, getEAlteraNivel))
    val fragmentos = (removeUltimoFragmento andThen inverteFragmentosAgrupadores) (fragmentosComum.toList)

    criaNumeracoes(iniComum, dispPrincipal, numeros).map { num =>
      GrupoUrns(dispPrincipal, fragmentos, num)
    }
  }

  private def criaNumeracoes(iniComum: String, dispPrincipal: String, numeros: List[Numero]): List[Numeracao] = {
    case class Value(currNumeros: List[Int], numeracoes: List[Numeracao])

    val accValue: Value = numeros.zipWithIndex.foldLeft(Value(Nil, Nil)) { case (v, (n, idx)) =>
      n match {
        case u@Numero.Unico => v.copy(numeracoes = v.numeracoes :+ Numeracao.NumUnico(u))
        // numero str transforma o que tem no buffer em Numeracao e cria uma nova com o numero str
        case s: Numero.StrNumero =>
          val numeracoes = v.currNumeros.size match {
            case 0 => Option.empty
            case 1 => Some(NumUnico(Numero.IntNumero(v.currNumeros.head)))
            // embora Numeros receba um List, ele sempre recebe 2 valores
            case 2 => Some(Numeros(v.currNumeros))
            case _ => Some(IntervaloContinuo(v.currNumeros.head, v.currNumeros.last))
          }
          v.copy(
            numeracoes = v.numeracoes ++ List(numeracoes, Some(Numeracao.NumUnico(s))).flatten,
            currNumeros = Nil
          )
        case sn@Numero.SemNumero =>
          v.copy(
            numeracoes = v.numeracoes :+ Numeracao.NumUnico(sn)
          )
        case Numero.IntNumero(nInt) =>
          v.currNumeros.size match {
            case 0 => v.copy(currNumeros = v.currNumeros :+ nInt)
            case _ if v.currNumeros.last + 1 == nInt => v.copy(currNumeros = v.currNumeros :+ nInt)
            case 1 =>
              // se for penultimo elemento ou anterior, verifica se ele e o próximo sao sequenciais
              numeros.lift(idx + 1) match {
                case Some(Numero.IntNumero(nIntProximo)) if nInt + 1 == nIntProximo =>
                  v.copy(
                    numeracoes = v.numeracoes :+ NumUnico(Numero.IntNumero(v.currNumeros.head)),
                    currNumeros = List(nInt)
                  )
                case _ =>
                  v.copy(
                    currNumeros = v.currNumeros :+ nInt
                  )
              }
            case 2 =>
              v.copy(
                numeracoes = v.numeracoes :+ Numeros(v.currNumeros),
                currNumeros = List(nInt)
              )
            case _ =>
              v.copy(
                numeracoes = v.numeracoes :+ IntervaloContinuo(v.currNumeros.head, v.currNumeros.last),
                currNumeros = List(nInt)
              )
          }
      }
    }

    val numeracaoRestante = accValue.currNumeros.size match {
      case 0 => Option.empty
      case 1 => Some(NumUnico(Numero.IntNumero(accValue.currNumeros.head)))
      case 2 => Some(Numeros(accValue.currNumeros))
      case _ => Some(IntervaloContinuo(accValue.currNumeros.head, accValue.currNumeros.last))
    }
    accValue.numeracoes ++ List(numeracaoRestante).flatten
  }

  private def removeUltimoFragmento: List[UrnFragmento] => List[UrnFragmento] = { fragmentos =>
    fragmentos.dropRight(1)
  }

  // fragmentos agrupadores sao lidos ao contrario
  private def inverteFragmentosAgrupadores: List[UrnFragmento] => List[UrnFragmento] = { fragmentos =>
    var posInicio = Option.empty[Int]
    var posFim = Option.empty[Int]
    fragmentos.zipWithIndex.foreach { case (fragmento, idx) =>
      if (fragmento.isInstanceOf[DispositivoAgrupador]) {
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
      Artigo(NumUnico(num))
    case "cpt" => Caput
    case "par" if fragmentoUrn.contains("par1u") => ParagrafoUnico
    case "par" => Paragrafo(unicoIntNumero(fragmentoUrn))
    case "inc" => Inciso(unicoIntNumero(fragmentoUrn))
    case "ali" => Alinea(unicoIntNumero(fragmentoUrn))
    case "ite" => Item(unicoIntNumero(fragmentoUrn))
    case "tit" => Titulo(unicoIntNumero(fragmentoUrn))
    case "cap" => Capitulo(unicoIntNumero(fragmentoUrn))
    case "sec" => Secao(unicoIntNumero(fragmentoUrn))
    case "sub" => SubSecao(unicoIntNumero(fragmentoUrn))
    case "liv" => Livro(unicoIntNumero(fragmentoUrn))
    case "anx" => Anexo((unicoIntNumero(fragmentoUrn)), getEAlteraNivel("anx"))
    case "prt" => Parte(unicoIntNumero(fragmentoUrn))
    case _ => throw new IllegalArgumentException(s"Urn Invalida: $fragmentoUrn")
  }

  private def unicoIntNumero(fragmento: String) = NumUnico(Numero.IntNumero(fragmento.substring(3).toInt))

}
