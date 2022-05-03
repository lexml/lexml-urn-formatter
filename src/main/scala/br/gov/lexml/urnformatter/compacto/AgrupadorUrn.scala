package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.Numeracao._
import br.gov.lexml.urnformatter.compacto.NumeracaoMultipla._
import br.gov.lexml.urnformatter.compacto.Numero.IntNumero
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento.DispositivoAgrupador
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.collection.mutable
import scala.util.Try

private[compacto] object AgrupadorUrn {

  def agrupar(parsedUrns: List[ParsedUrn]): List[GrupoUrns] = {
    println("\n\n\n")
    println(s"==> parsedUrns: ${parsedUrns.mkString(",")}")
    case class ValueAux(iniComum: String, dispPrincipal: String, numeros: List[Numero], grupos: List[GrupoUrns])

    val v = parsedUrns.tail.foldLeft(
      ValueAux(parsedUrns.head.inicioComum, parsedUrns.head.disPrincipal, List(parsedUrns.head.numero), Nil)
    ) {
      case (value, parsedUrn) =>
        println(s"==> value: $value - ParsedUrn: $parsedUrn")
        if (parsedUrn.inicioComum.equals(value.iniComum)) {
          println("==> iniComun")
          value.copy(numeros = value.numeros :+ parsedUrn.numero)
        } else {
          println("==> !iniComun")
          value.copy(
            iniComum = parsedUrn.inicioComum,
            dispPrincipal = parsedUrn.disPrincipal,
            numeros = List(parsedUrn.numero),
            grupos = value.grupos ++ criaGrupos(value.iniComum, value.dispPrincipal, value.numeros)
          )
        }
    }
    println(s"==>v ${v}")
    val ret = v.grupos ++ criaGrupos(v.iniComum, v.dispPrincipal, v.numeros)
    println(s"==> ret: ${ret.mkString(",")}")
    println("\n\n\n")
    ret
  }

  def urnFragmento(fragmentoUrn: String): UrnFragmento =
    parse(fragmentoUrn, (unused: String) => unused {
      0
    })

  private def criaGrupos(iniComum: String, dispPrincipal: String, numeros: List[Numero]): List[GrupoUrns] = {
    val nivelAtualAnexoPorFragmento = mutable.Map[String, Int]()

    val getEAlteraNivel = { p: String =>
      val nivelAtual = nivelAtualAnexoPorFragmento.getOrElse(p, 1)
      nivelAtualAnexoPorFragmento.put(p, nivelAtual + 1)

      nivelAtual
    }

    // 1 aqui nao importa, apenas precisamos adicionar um numero para o parse nao falhar
    val fragmentosComum = (iniComum.concat("1")).split("_").map(parse(_, getEAlteraNivel))
    val fragmentos = (removeUltimoFragmento andThen inverteFragmentosAgrupadores) (fragmentosComum.toList)
    println(s"==> fragmentosComum: ${fragmentosComum.mkString(",")}")
    println(s"==> fragmentos: ${fragmentos.mkString(",")}")

    criaNumeracoes(iniComum, numeros).map { num =>
      println(s"==> num: $num")
      GrupoUrns(fragmentosComum.last.tipo, fragmentos, num)
    }
  }

  //TODO: Rename?? volta só um
  private def criaNumeracoes(iniComum: String, numeros: List[Numero]): List[Numeracao] = {
    //case class ValueAux(numeros: List[Int], numeracoes: List[Numeracao])
    case class ValueAux(currNumeracao: Option[Numeracao], accNumeracoes: List[Numeracao])

    val accValue: ValueAux = numeros.zipWithIndex.foldLeft(ValueAux(None, Nil)) { case (v, (n, idx)) =>
      println(s"==> v: $v - n: $n - idx: $idx")
      n match {
        // case u@Numero.Unico => v.copy(numeracoes = v.numeracoes :+ Numeracao.UmNumero(u))
        // numero str transforma o que tem no buffer em Numeracao e cria uma nova com o numero str
        //        case s: Numero.StrNumero =>
        //          val numeracoes = v.numeros.size match {
        //            case 0 => Option.empty
        //            case 1 => Some(UmNumero(Numero.IntNumero(v.numeros.head)))
        //            case 2 => Some(DoisNumeros(v.numeros.head, v.numeros.last))
        //            // case _ => Some(IntervaloContinuo(v.numeros.head, v.numeros.last))
        //          }
        //          v.copy(
        //            numeracoes = v.numeracoes ++ numeracoes ++ List(Some(Numeracao.UmNumero(s))).flatten,
        //            numeros = Nil
        //          )
        //        case sn@Numero.SemNumero =>
        //          v.copy(
        //            numeracoes = v.numeracoes :+ Numeracao.UmNumero(sn)
        //          )
        case Numero.IntNumero(nInt) =>
          println(s"==> IntNumero: $nInt")
          v.currNumeracao match {
            // 1, 2, 3
            // 1, 2, 4
            // 1, 3, 4, 6, 7
            // 1, 3, 5, 6
            case None => v.copy(
              currNumeracao = Some(UmNumero(IntNumero(nInt)))
            )
            case Some(curr) => curr match {
              case UmNumero(IntNumero(n)) if n + 1 == nInt => v.copy(
                currNumeracao = Some(MultiplosNumeros(List(IntervaloContinuo(n, nInt))))
              )
              case UmNumero(IntNumero(n)) => v.copy(
                currNumeracao = Some(MultiplosNumeros(List(Numeros(List(n, nInt)))))
              )
              case MultiplosNumeros(multiplos) => multiplos.last match {
                case IntervaloContinuo(ini, fim) if fim + 1 == nInt => v.copy(
                  currNumeracao = Some(MultiplosNumeros(multiplos.dropRight(1) :+ IntervaloContinuo(ini, nInt)))
                )
                case IntervaloContinuo(_, _) => v.copy(
                  currNumeracao = Some(UmNumero(IntNumero(nInt))),
                  accNumeracoes = v.accNumeracoes :+ curr
                )
                case n@Numeros(values) if values.last + 1 == nInt => v.copy(
                  currNumeracao = Some(MultiplosNumeros(List(IntervaloContinuo(values.last, nInt)))),
                  accNumeracoes = v.accNumeracoes :+ MultiplosNumeros(multiplos.dropRight(1) :+ Numeros(n.values.dropRight(1)))
                )
                case n@Numeros(values) => v.copy(
                  currNumeracao = Some(MultiplosNumeros(List(Numeros(n.values :+ nInt))))
                )
              }

            }
          }

        //          val numeracaoRestante = accValue.currNumeracao.map(curr => a
        //            case 0 => Option.empty
        //            case 1 => Some(UmNumero(Numero.IntNumero(accValue.numeros.head)))
        //            // case _ => Some(MultiplosNumeros(accValue.numeros))
        //            //      case 2 => Some(DoisNumeros(accValue.numeros.head, accValue.numeros.last))
        //            //      case _ => Some(IntervaloContinuo(accValue.numeros.head, accValue.numeros.last))
      }
    }
//          accValue.numeracoes ++ List(numeracaoRestante).flatten
      val restante = accValue.currNumeracao.fold(List.empty[Numeracao])(List(_))
      accValue.accNumeracoes ++ restante
//
//
//
//          v.numeros.size match {
//            case 0 =>
//              println("==> caso 1")
//              v.copy(numeros = v.numeros :+ nInt)
//            case _ if v.numeros.last + 1 == nInt =>
//              println("==> caso 2")
//              v.copy(numeros = v.numeros :+ nInt)
//            case 1 =>
//              println("==> caso 3")
//              // se for penultimo elemento ou anterior, verifica se ele e o próximo sao sequenciais
//              numeros.lift(idx + 1) match {
//                case Some(Numero.IntNumero(nIntProximo)) if nInt + 1 == nIntProximo =>
//                  println("==> caso 3.1")
//                  v.copy(
//                    numeracoes = v.numeracoes :+ UmNumero(Numero.IntNumero(v.numeros.head)),
//                    numeros = List(nInt)
//                  )
//                case _ =>
//                  println("==> caso 3.2")
//                  v.copy(
//                    numeros = v.numeros :+ nInt
//                  )
//              }
//            case _ =>
//              println("==> caso 4")
//              v.copy(
////                 numeracoes = v.numeracoes :+ DoisNumeros(v.numeros.head, v.numeros.last),
////                 numeros = List(nInt)
//                numeros = v.numeros :+ nInt
//              )
////            case _ =>
////              println("==> caso 5")
////              v.copy(
////                numeracoes = v.numeracoes :+ IntervaloContinuo(v.numeros.head, v.numeros.last),
////                numeros = List(nInt)
//////                numeros = v.numeros :+ nInt
////              )
//          }
//      }
//    }
//
//    println(s"==> accValue: ${accValue}")
//
//    val numeracaoRestante = accValue.numeros.size match {
//      case 0 => Option.empty
//      case 1 => Some(UmNumero(Numero.IntNumero(accValue.numeros.head)))
//      // case _ => Some(MultiplosNumeros(accValue.numeros))
////      case 2 => Some(DoisNumeros(accValue.numeros.head, accValue.numeros.last))
////      case _ => Some(IntervaloContinuo(accValue.numeros.head, accValue.numeros.last))
//    }
//    accValue.numeracoes ++ List(numeracaoRestante).flatten
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
      Artigo(UmNumero(num))
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

  private def unicoIntNumero(fragmento: String) = {
    val numero = fragmento.substring(3)
    Try(UmNumero(Numero.IntNumero(numero.toInt))).getOrElse {
      if (fragmento.contains("anx")) {
        UmNumero(Numero.StrNumero(numero))
      } else {
        Numeracao.SemNumero
      }
    }
  }

}
