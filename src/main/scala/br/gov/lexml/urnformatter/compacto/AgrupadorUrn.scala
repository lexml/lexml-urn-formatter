package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.Numeracao._
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


    val numeracoes = criaNumeracoes(iniComum, numeros)
    println(s"==> numeracoes: $numeracoes")
    List(GrupoUrns(fragmentosComum.last.tipo, fragmentos, numeracoes)) //TODO:
//    criaNumeracoes(iniComum, numeros).map { num =>
//      println(s"==> num: $num")
//      GrupoUrns(fragmentosComum.last.tipo, fragmentos, num)
//    }
  }

  //TODO: Rename?? volta só um
  private def criaNumeracoes(iniComum: String, numeros: List[Numero]): List[Numeracao] = {
    //case class ValueAux(numeros: List[Int], numeracoes: List[Numeracao])
    case class ValueAux(currNumeracao: Option[Numeracao], numeracoes: List[Numeracao])
    // val currNumeracao = Option[Numeracao]

    val accValue: ValueAux = numeros.zipWithIndex.foldLeft(ValueAux(None, Nil)) { case (v, (n, idx)) =>
      println(s"==> v: $v - n: $n - idx: $idx")
      // assertEquals("arts. 1º a 3º, art. 3º-A e arts. 4º a 6º", Urn2NomeCompacto.format(List("art1", "art2", "art3", "art3-A", "art4", "art5", "art6")))
      // art56_cpt_inc2", "art56_cpt_inc4", "art56_cpt_inc8", "art56_cpt_inc9", "art56_cpt_inc10", "art56_cpt_inc11
      n match {
        case u@Numero.Unico => v.copy(currNumeracao = Some(UmNumero(u)))
        // numero str transforma o que tem no buffer em Numeracao e cria uma nova com o numero str
        case s: Numero.StrNumero =>
          v.copy(
            currNumeracao = Some(UmNumero(s)),
            numeracoes = v.numeracoes ++ v.currNumeracao.fold(List.empty[Numeracao])(List(_))
          )
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
        case sn@Numero.SemNumero => v.copy(currNumeracao = Some(Numeracao.UmNumero(sn)))
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
                currNumeracao = Some(IntervaloContinuo(n, nInt))
              )
              case UmNumero(IntNumero(n)) => v.copy(
                currNumeracao = Some(Numeros(List(n, nInt)))

                // currNumeracao = Some(MultiplosNumeros(List(Numeros(List(n, nInt)))))
              )
              case UmNumero(_) => v.copy(
                currNumeracao = Some(UmNumero(IntNumero(nInt))),
                numeracoes = v.numeracoes :+ curr

                // currNumeracao = Some(MultiplosNumeros(List(IntervaloContinuo(n, nInt))))
              )
              case IntervaloContinuo(ini, fim) if fim + 1 == nInt => v.copy(
                currNumeracao = Some(IntervaloContinuo(ini,  nInt))
              )
              case IntervaloContinuo(_, _) => v.copy(
                currNumeracao = Some(UmNumero(IntNumero(nInt))),
                numeracoes = v.numeracoes :+ curr
              )
              case n@Numeros(values) if values.last + 1 == nInt => v.copy(
                currNumeracao = Some(IntervaloContinuo(values.last, nInt)), //multiplos.dropRight(1) :+ Numeros(n.values.dropRight(1)) :+  IntervaloContinuo(values.last, nInt)))
                numeracoes = v.numeracoes :+ (if (values.size == 2) UmNumero(IntNumero(values.head)) else Numeros(values.dropRight(1)))
              )
              case Numeros(values) => v.copy(
                currNumeracao = Some(Numeros(values :+ nInt))
              )

//              case MultiplosNumeros(multiplos) => multiplos.last match {
//                case IntervaloContinuo(ini, fim) if fim + 1 == nInt => v.copy(
//                  currNumeracao = Some(MultiplosNumeros(multiplos.dropRight(1) :+ IntervaloContinuo(ini, nInt)))
//                )
//                case IntervaloContinuo(_, _) => v.copy(
//                  currNumeracao = Some(MultiplosNumeros(multiplos :+ Numeros(List(nInt))))
//                  // accNumeracoes = v.accNumeracoes :+ curr
//                )
//                case n@Numeros(values) if values.last + 1 == nInt => v.copy(
//                  currNumeracao = Some(MultiplosNumeros(multiplos.dropRight(1) :+ Numeros(n.values.dropRight(1)) :+  IntervaloContinuo(values.last, nInt)))
//                  // accNumeracoes = v.accNumeracoes :+ MultiplosNumeros())
//                )
//                case n@Numeros(values) => v.copy(
//                  currNumeracao = Some(MultiplosNumeros(List(Numeros(n.values :+ nInt))))
//                )
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
    //          accValue.numeracoes ++ List(numeracaoRestante).flatten
      val restante = accValue.currNumeracao.fold(List.empty[Numeracao])(List(_))
      println(s"==> numeracoes: ${accValue.numeracoes}")
      println(s"==> restante: ${restante}")
      accValue.numeracoes ++ List(restante).flatten
    }
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
  // }

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
      Artigo(List(UmNumero(num)))
    case "cpt" => Caput
    case "par" if fragmentoUrn.contains("par1u") => ParagrafoUnico
    case "par" => Paragrafo(List(unicoIntNumero(fragmentoUrn)))
    case "inc" => Inciso(List(unicoIntNumero(fragmentoUrn)))
    case "ali" => Alinea(List(unicoIntNumero(fragmentoUrn)))
    case "ite" => Item(List(unicoIntNumero(fragmentoUrn)))
    case "tit" => Titulo(List(unicoIntNumero(fragmentoUrn)))
    case "cap" => Capitulo(List(unicoIntNumero(fragmentoUrn)))
    case "sec" => Secao(List(unicoIntNumero(fragmentoUrn)))
    case "sub" => SubSecao(List(unicoIntNumero(fragmentoUrn)))
    case "liv" => Livro(List(unicoIntNumero(fragmentoUrn)))
    case "anx" => Anexo(List(unicoIntNumero(fragmentoUrn)), getEAlteraNivel("anx"))
    case "prt" => Parte(List(unicoIntNumero(fragmentoUrn)))
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
