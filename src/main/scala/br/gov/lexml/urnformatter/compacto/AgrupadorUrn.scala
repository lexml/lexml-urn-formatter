package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.Numeracao._
import br.gov.lexml.urnformatter.compacto.UrnFragmento._

import scala.collection.mutable
import scala.util.Try

private[compacto] object AgrupadorUrn {

  def agrupar(parsedUrns: List[ParsedUrn]): List[GrupoUrns] = {

    case class Value(iniComum: String, dispPrincipal: String, numeros: List[Numero], grupos: List[GrupoUrns])

    val v = Value(parsedUrns.head.inicioComum, parsedUrns.head.disPrincipal, List(parsedUrns.head.numero), Nil)

    val p = parsedUrns.tail.foldLeft(v){
      case (value, ug) =>
        println("---")
        println(s"==> value: $value")
        println(s"==> ug: $ug")
        println("---")
        if (ug.inicioComum.equals(value.iniComum)) {
          value.copy(numeros = value.numeros :+ ug.numero)
        } else {
          val grupos1 = criaGrupos(value.iniComum, value.dispPrincipal, value.numeros)
          value.copy(
            iniComum = ug.inicioComum,
            dispPrincipal = ug.disPrincipal,
            numeros = List(ug.numero),
            grupos = value.grupos ++ grupos1
          )
        }
    }
    p.grupos ++ criaGrupos(p.iniComum, p.dispPrincipal, p.numeros)

//    var iniComum = parsedUrns.head.inicioComum
//    var dispPrincipal = parsedUrns.head.disPrincipal
//    var numeros = mutable.ListBuffer[Numero]()
//    numeros += parsedUrns.head.numero
//    val acc = mutable.ListBuffer[GrupoUrns]()
//
//    parsedUrns.tail.foreach { ug =>
//      if (ug.inicioComum.equals(iniComum)) {
//        numeros += ug.numero
//      } else {
//        acc ++= criaGrupos(iniComum, dispPrincipal, numeros.toList)
//        numeros = mutable.ListBuffer[Numero]()
//        numeros += ug.numero
//        iniComum = ug.inicioComum
//        dispPrincipal = ug.disPrincipal
//      }
//    }
//    acc ++= criaGrupos(iniComum, dispPrincipal, numeros.toList)
//    acc.toList
  }

  private def removeUltimo: List[UrnFragmento] => List[UrnFragmento] = { partes =>
    partes.dropRight(1)
  }

  private def inverteFragmentosAgrupadores: List[UrnFragmento] => List[UrnFragmento] = { partes =>
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

  private def criaGrupos(iniComum: String, dispPrincipal: String, numeros: List[Numero]): List[GrupoUrns] = {
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

    numeracoes.map { num =>
      GrupoUrns(dispPrincipal, partes, num)
    }.toList
  }

  private def parse(parteUrn: String, getEAlteraNivel: String => Int): UrnFragmento = parteUrn.take(3) match {
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
    case "prt" => Parte(NumUnico(Numero.IntNumero(parteUrn.substring(3).toInt)))
    case _ => throw new IllegalArgumentException(s"Invalid urn: $parteUrn")
  }


}
