package br.gov.lexml.urnformatter
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object Urn2NomeCompacto {

  import Urn2Format._

  type Comp = (String, List[Numero], List[Numero])

  def format(urnsFrag: List[String]): List[String] = {
    var i = 0
    var indexs: Map[String, Int] = Map[String, Int]()
    var nodes: ListBuffer[String] = ListBuffer[String]()
    var edges: ListBuffer[ListBuffer[Int]] = ListBuffer[ListBuffer[Int]]()

    urnsFrag.foreach(e => {
        val frags = e.split("_").toList
        var acc: String = ""

        for(str <- frags) {
            var aux = acc + (if(acc.size > 0) "_" else "") + str
            if (!(indexs contains aux)){
                val i = nodes.size
                nodes += aux
                edges += ListBuffer[Int]()
                indexs(aux) = i
                
                if (indexs contains acc) edges(indexs(acc)) += i
            }
            acc = aux
        }
    })

    nodes.zipWithIndex
        .map(e => (e._1 contains '_', e._2))
        .filter(e => !e._1)
        .map(_._2)
        .toList
        .flatMap(i => nomear(i, nodes.toList, edges.toList).toList)
  }

  def nomear(i: Int, nodes: List[String], edges: List[ListBuffer[Int]]): ListBuffer[String] = {
      val childs = nodes.zipWithIndex.filter(edges(i) contains _._2).sortWith(_._1 < _._1)
        .map(e => {
          var aux = e._1.split("_").toList
            .flatMap(compRe.findFirstMatchIn(_))
            .map(m => m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_)))
            .last

          (e._1, e._2, aux(0).n)
        })

      var outChilds = childs
      var result: ListBuffer[String] = ListBuffer[String]()
      outChilds.foreach(e => {
          var aux = nomear(e._2, nodes, edges)
          if (aux.size > 0) {
            aux.foreach(result append _) 
            outChilds = outChilds diff List(e)
          }
      })

      var x = -1
      var finalChilds = outChilds.map(e => (e._1, e._3)).sortWith(_._2 < _._2)
      val fchilds = finalChilds
      finalChilds.zipWithIndex.foreach{ case (y, i) => {
        finalChilds = finalChilds diff List(y)
        if (finalChilds.size > 0) {
            var next = finalChilds(0)
            if (x >= 0 && next._2 - y._2 > 1) {
              result append format((fchilds(x)._1, y._1))
              x = -1
            } else if (next._2 - y._2 > 1) result append format(y._1)
            else if (x == -1) x = i
        } else {
          if (x == -1) result append format(y._1)
          else result append format(fchilds(x)._1, y._1)
        }
      }}
      return result.sortWith(_ < _)
  }

  def format(urnsFrag: (String, String)): String = {
    var i = 0
    val aux = urnsFrag._2
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_)), List[Numero]()))
      .last

    var comps = urnsFrag._1
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_)), List[Numero]()))
      .map(m => {
          if (agregadores contains m._1) i += 1; m
      })
      .to[ListBuffer]

    comps(comps.size - 1) = (comps.last._1, comps.last._2, aux._2)
    var e = comps.toList.flatMap(formatComp(_))

    val size = comps.size;
    var elements = e.zipWithIndex
        .filter {
            case (e, i) => !(e == (",","caput") && i < size - 1)
            case _ => false
        }
        .map {
            case (e, i) => e
        }

    var (left, right) = elements.splitAt(i)
    elements = left.reverse ++ right
    
    elements match {
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => g + " " + txt }).mkString("", "", "")).trim()
      case _ => ""
    }
  }

  def format(urnFrag: String) = {
    var i = 0
    val comps = urnFrag
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_)), List()))
      .map(m => {
          if (agregadores contains m._1) i += 1; m
      })
      .flatMap(formatComp(_))

    val size = comps.size;
    var aux = comps.zipWithIndex
        .filter {
            case (e, i) => !(e == (",","caput") && i < size - 1)
            case _ => false
        }
        .map {
            case (e, i) => e
        }

    var (left, right) = aux.splitAt(i)
    aux = left.reverse ++ right
    
    aux match {
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => g + " " + txt }).mkString("", "", "")).trim()
      case _ => ""
    }
  }

  type FormattedComp = (String, String)

  val agregadores: Map[String, (String, String)] = Map(
    "prt" -> (",", "parte"),
    "liv" -> (",", "livro"),
    "cap" -> (",", "capítulo"),
    "tit" -> (",", "título"),
    "sec" -> (",", "seção"),
    "sub" -> (",", "subseção"),
    "anx" -> (",", "anexo"))

  def formatComp: Comp => Option[FormattedComp] = {
    case ("alt", _, _) => Some((",", "alteração"))
    case ("omi", _, _) => Some((",", "omissis"))
    case ("cpp", _, _) => Some((",", "componente principal"))
    case ("lex", _, _) => Some((",", "raiz"))
    
    case ("art", Unico :: _, _) =>
      Some((",", "art. único"))
    case ("art", Algum(n) :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        val txt = e match {
            case m::ts => {
                m match {
                    case Algum(m) => formatOrdinal(m) + formatComplementos(ts)
                    case _ => ""
                }
            }
            case _ => ""
        }
        mtxt = " ao " + txt
      } 
      Some((",", "art. " + formatOrdinal(n) + formatComplementos(cs) + mtxt))
    }
    case ("cpt", _, _) =>
      Some((",", "caput"))
    case ("par", Unico :: _, _) => Some((",", "parágrafo único"))
    case ("par", Algum(n) :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        val txt = e match {
            case m::ts => {
                m match {
                    case Algum(m) => formatOrdinal(m) + formatComplementos(ts)
                    case _ => ""
                }
            }
            case _ => ""
        }
        mtxt = " ao " + txt
      } 
      Some((", §", formatOrdinal(n) + formatComplementos(cs) + mtxt))
    }
    case ("inc", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        val txt = e match {
            case m::ts => {
                m match {
                    case Algum(m) => formatRomano(m).toUpperCase + formatComplementos(ts)
                    case _ => ""
                }
            }
            case _ => ""
        }
        mtxt = " a " + txt
      } 
      Some((",", formatRomano(n.n).toUpperCase + formatComplementos(cs) + mtxt))
    }
    case ("ali", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        val txt = e match {
            case m::ts => {
                m match {
                    case Algum(m) => formatAlfa(m).toLowerCase + formatComplementos(ts)
                    case _ => ""
                }
            }
            case _ => ""
        }
        mtxt = " a " + txt
      } 
      Some((",", formatAlfa(n.n).toLowerCase + formatComplementos(cs) + mtxt))
    }
    case ("ite", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        val txt = e match {
            case m::ts => {
                m match {
                    case Algum(m) => m.toString + formatComplementos(ts)
                    case _ => ""
                }
            }
            case _ => ""
        }
        mtxt = " a " + txt
      } 
      Some((",", n.n.toString + formatComplementos(cs) + mtxt))
    }
    case (tip, n :: cs, e) if agregadores contains tip => {
      val (g, t) = agregadores(tip)
      val ntxt = n match {
        case Unico => "único"
        case Algum(n) => if(t == "anexo") formatAlfa(n).toLowerCase else formatRomano(n).toUpperCase
      }
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        val txt = m match {
            case Algum(m) => if(t == "anexo") formatAlfa(m).toLowerCase else formatRomano(m).toUpperCase
            case _ => ""
        }
        mtxt = " a " + txt + formatComplementos(ts)
      }
      Some((g, t + " " + ntxt + formatComplementos(cs) + mtxt))
    }
    case _ => None
  }

}
