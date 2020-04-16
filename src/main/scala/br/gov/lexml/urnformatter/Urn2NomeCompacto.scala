package br.gov.lexml.urnformatter
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object Urn2NomeCompacto {

  import Urn2Format._

  type Comp = (String, List[Numero], List[Numero])

  def format(urnsFrag: List[String]): String = {
    var i = 0
    var indexs: Map[String, Int] = Map[String, Int]()
    var nodes: ListBuffer[String] = ListBuffer[String]()
    var edges: ListBuffer[ListBuffer[Int]] = ListBuffer[ListBuffer[Int]]()

    nodes += "root"
    edges += ListBuffer[Int]()
    indexs("root") = 0

    /* Montagem da árvore de urns */
    urnsFrag.foreach(e => {
        var frag = e
        var art_acc = "*"
        while (frag contains "anx") {
          frag = frag.replaceFirst("anx", "anz" + art_acc)
          art_acc += "*"
        }
        val frags = frag.split("_").toList
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
        .filter(_ > 0)
        .toList
        .map(edges(0) += _)
        .filter(_.size > 0)

    nomear(0, nodes.toList, edges.toList)
  }

  def nomear(i: Int, nodes: List[String], edges: List[ListBuffer[Int]]): String = {
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
            result append aux
            outChilds = outChilds diff List(e)
          }
      })

      var x = -1
      var finalChilds = outChilds.map(e => (e._1, e._3)).sortWith(_._2 < _._2)
      val fchilds = finalChilds
      finalChilds.zipWithIndex.foreach{ case (y, i) => {
        finalChilds = finalChilds diff List(y)
        var (urn_start, urn_end) = ((if (x >= 0 && x < fchilds.size) fchilds(x)._1 else ""), y._1)
        if (urn_start contains "art")
          urn_start = urn_start.substring(urn_start.indexOf("art"))
        if (urn_end contains "art")
          urn_end = urn_end.substring(urn_end.indexOf("art"))
        if (finalChilds.size > 0) {
            var next = finalChilds(0)
            if (x >= 0 && next._2 - y._2 > 1) {
              result append format((urn_start, urn_end))
              x = -1
            } else if (next._2 - y._2 > 1) result append format(urn_end)
            else if (x == -1) x = i
        } else {
          if (x == -1) result append format(urn_end)
          else result append format((urn_start, urn_end))
        }
      }}
      
      return (if(result.size > 0) (if (result.size > 1) result.init.mkString(", ") + " e " else "") + result.last else "")
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
          if (agregadores contains m._1.replace("anz", "anx").replace("*", "")) i += 1; m
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
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => (if (g == ",") "" else " ")  + g + " " + txt }).mkString("", "", "")).trim()
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
          if (agregadores contains m._1.replace("anz", "anx").replace("*", "")) i += 1; m
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
      case ((_, t) :: r) => (t + r.map({ case (g, txt) => (if (g == ",") "" else " ") + g + " " + txt }).mkString("", "", "")).trim()
      case _ => ""
    }
  }

  type FormattedComp = (String, String)

  val agregadores: Map[String, (String, String, String)] = Map(
    "prt" -> ("do", "Parte", "Partes"),
    "liv" -> ("do", "Livro", "Livros"),
    "cap" -> ("do", "Capítulo", "Capítulos"),
    "tit" -> ("do", "Título", "Títulos"),
    "sec" -> ("da", "Seção", "Seções"),
    "sub" -> ("da", "Subseção", "Subseções"),
    "anx" -> ("do", "Anexo", "Anexos"))

  def formatComp: Comp => Option[FormattedComp] = {
    case ("alt", _, _) => Some((",", "alteração"))
    case ("omi", _, _) => Some((",", "omissis"))
    case ("cpp", _, _) => Some((",", "componente principal"))
    case ("lex", _, _) => Some((",", "raiz"))
    
    case ("art", Unico :: _, _) =>
      Some((",", "art. único"))
    case ("art", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "a"
          case _ => "a"
        }) + " " + (m match {
          case Algum(m) => formatOrdinal(m) + formatComplementos(ts)
          case _ => ""
        })
      } 
      Some((",", (if (mtxt =="") "art. " else "arts. ") + formatOrdinal(n.n) + formatComplementos(cs) + mtxt))
    }
    case ("cpt", _, _) =>
      Some((",", "caput"))
    case ("par", Unico :: _, _) => Some((",", "parágrafo único"))
    case ("par", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "ao"
          case _ => "ao"
        }) + " " + (m match {
          case Algum(m) => formatOrdinal(m) + formatComplementos(ts)
          case _ => ""
        })
      } 
      Some((",", "§ " + formatOrdinal(n.n) + formatComplementos(cs) + mtxt))
    }
    case ("inc", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "a"
          case _ => "a"
        }) + " " + (m match {
          case Algum(m) => formatRomano(m).toUpperCase + formatComplementos(ts)
          case _ => ""
        })
      } 
      Some((",", formatRomano(n.n).toUpperCase + formatComplementos(cs) + mtxt))
    }
    case ("ali", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "a"
          case _ => "a"
        }) + " " + (m match {
          case Algum(m) => formatAlfa(m).toLowerCase + formatComplementos(ts)
          case _ => ""
        })
      } 
      Some((",", formatAlfa(n.n).toLowerCase + formatComplementos(cs) + mtxt))
    }
    case ("ite", n :: cs, e) => {
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "a"
          case _ => "a"
        }) + " " + (m match {
          case Algum(m) => m.toString + formatComplementos(ts)
          case _ => ""
        })
      } 
      Some((",", n.n.toString + formatComplementos(cs) + mtxt))
    }
    case (tip, n :: cs, e) if (agregadores contains tip.replace("anz", "anx").replace("*", "")) => {
      val (g, t, p) = agregadores(tip.replace("anz", "anx").replace("*", ""))
            
      var mtxt = ""
      if (e.size > 0) {
        var m::ts = e
        mtxt = " " + ((n, m) match {
          case (Algum(n), Algum(m)) => if((m - n).abs == 1) "e" else "a"
          case _ => "a"
        }) + " " + (m match {
            case Algum(m) => if(t == "Anexo") {
              if (tip == "anz***") formatAlfa(m).toUpperCase
              else if (tip == "anz**") m.toString
              else formatRomano(m).toUpperCase
            } else formatRomano(m).toUpperCase
            case _ => ""
        }) + formatComplementos(ts)
      }
      Some((g, (if (e.size > 0) p else t) + " " + (n match {
        case Unico => "único"
        case Algum(n) => if(t == "Anexo") {
            if (tip == "anz***") formatAlfa(n).toUpperCase
              else if (tip == "anz**") n.toString
              else formatRomano(n).toUpperCase
        } else formatRomano(n).toUpperCase
      }) + formatComplementos(cs) + mtxt))
    }
    case _ => None
  }

}
