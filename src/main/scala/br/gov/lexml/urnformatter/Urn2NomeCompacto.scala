package br.gov.lexml.urnformatter
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object Urn2NomeCompacto {

  import Urn2Format._

  type Comp = (String, List[Numero])

  def format(urnsFrag: List[String]) {
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
        .foreach(i => {
            println(nodes(i))
            println(nomear(i, nodes.toList, edges.toList))
        })

  }

  def nomear(i: Int, nodes: List[String], edges: List[ListBuffer[Int]]): ListBuffer[String] = {
      val childs = nodes.zipWithIndex.filter(edges(i) contains _._2).sortWith(_._1 < _._1)
      var outChilds = childs
      var result: ListBuffer[String] = ListBuffer[String]()
      outChilds.foreach(e => {
          var aux = nomear(e._2, nodes, edges)
          if (aux.size > 0) {
            aux.foreach(result append _) 
            outChilds = outChilds diff List(e)
          }
      })

      var x = 0
      println(childs)
      (childs.indices diff outChilds.indices).foreach(y => {
        if (y - x - 1 > 0)
          result append format((childs(x)._1, childs(y)._1))
        else
          result append format(childs(x)._1)
        x = y + 1
      })
      childs.map(e => format(e._1)).foreach(result append _)
      return result.sortWith(_ < _)
  }

  def format(urnsFrag: (String, String)): String = {
      format(urnsFrag._1) + "<==>" + format(urnsFrag._2)
  }

  def format(urnFrag: String) = {
    //var i = 0
    val comps = urnFrag
      .split("_").toList
      .flatMap(compRe.findFirstMatchIn(_))
      .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
      /* .map(m => {
          if (agregadores contains m._1) i += 1; m
      }) */
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

    /* var (left, right) = aux.splitAt(i)
    aux = left.reverse ++ right */
    
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
    "tit" -> (",", "titulo"),
    "sec" -> (",", "seção"),
    "sub" -> (",", "subseção"))

  def formatComp: Comp => Option[FormattedComp] = {
    case ("alt", _) => Some(("a", "alteração"))
    case ("omi", _) => Some(("o", "omissis"))

    case ("art", Unico :: _) =>
      Some((",", "art. único"))
    case ("art", Algum(n) :: cs) =>
      Some((",", "art. " + formatOrdinal(n) + formatComplementos(cs)))
    case ("cpt", _) =>
      Some((",", "caput"))
    case ("par", Unico :: _) => Some((",", "parágrafo único"))
    case ("par", Algum(n) :: cs) =>
      Some((", §", formatOrdinal(n) + formatComplementos(cs)))
    case ("inc", n :: cs) =>
      Some((",", formatRomano(n.n).toUpperCase + formatComplementos(cs)))
    case ("ali", n :: cs) =>
      Some((",", formatAlfa(n.n).toLowerCase + formatComplementos(cs)))
    case ("ite", n :: cs) =>
      Some((",", n.n.toString + formatComplementos(cs)))
    case (tip, n :: cs) if agregadores contains tip => {
      val (g, t) = agregadores(tip)
      val ntxt = n match {
        case Unico => "único"
        case Algum(n) => n
      }
      Some((g, t + " " + ntxt + formatComplementos(cs)))
    }
    case _ => None
  }

}
