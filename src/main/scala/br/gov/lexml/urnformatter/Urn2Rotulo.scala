package br.gov.lexml.urnformatter

import br.gov.lexml.parser.pl.output.LexmlRenderer

/**
 * Formata uma URN como um rótulo
 */
object Urn2Rotulo {

  abstract sealed class Numero {
    val n: Int
  }
  final case object Unico extends Numero {
    override val n = 1
  }
  final case class Algum(n: Int) extends Numero

  val compRe = "^([a-z]+)((?:1u|[0-9-])*)$".r
  val compDropCpt = "_cpt$".r
  
  type Comp = (String, List[Numero])

  def readInt: String => Numero = {
    case "1u" => Unico
    case x => Algum(x.toInt)
  }

  def format(urnFrag: String): String = {
    
    
    val urnFinal =
      compDropCpt
        .replaceFirstIn(urnFrag, "")
        .split("_").toList
        .flatMap(compRe.findFirstMatchIn(_))
        .map(m => (m.group(1), m.group(2).split("-").toList.filter(!_.isEmpty).map(readInt(_))))
        .last

    formatComp(urnFinal) match {
      case Some(x) => x
      case None => ""
    }
  }


  type FormattedComp = String

  val agregadores: Map[String, String] = Map(
    "prt" -> "PARTE",
    "liv" -> "LIVRO",
    "cap" -> "CAPÍTULO",
    "tit" -> "TÍTULO",
    "sec" -> "Seção",
    "sub" -> "Subseção")

  def formatComp: Comp => Option[FormattedComp] = {
    case ("cpt", _) => None
    case ("alt", _) => None
    case ("omi", _) => None
    case ("art", Unico :: _) => Some("Art. Único.")
    case ("art", Algum(n) :: cs) => 
        Some("Art. " 
              +  (if (n< 10){formatOrdinal(n)}else{n}) 
              + formatComplementos(cs) 
              + (if (n> 9) {"."} else {""}))
    case ("par", Unico :: _) => Some("Parágrafo Único.")
    case ("par", Algum(n) :: cs) =>
      Some("§ " 
            + (if (n< 10){formatOrdinal(n)} else {n}) 
            + formatComplementos(cs)
            + (if (n> 9) {"."} else {""}))
    case ("inc", n :: cs) =>
      Some(formatRomano(n.n).toUpperCase + formatComplementos(cs) + " –")
    case ("ali", n :: cs) =>
      Some(formatAlfa(n.n).toLowerCase + formatComplementos(cs) + ")")
    case ("ite", n :: cs) =>
      Some(n.n.toString + formatComplementos(cs) + ".")
    case (tip, n :: cs) if agregadores contains tip => {
      val t = agregadores(tip)
      val ntxt = n match {
        case Unico => "único"
        case Algum(n) => formatRomano(n).toUpperCase
      }
      Some(t + " " + ntxt + formatComplementos(cs))
    }
    case _ => None
  }

  def formatOrdinal(num: Int): String = LexmlRenderer.renderOrdinal(num)

  def formatRomano(n: Int): String = LexmlRenderer.renderRomano(n)

  def formatAlfa(n: Int): String = LexmlRenderer.renderAlphaSeq(n - 1)

  def formatComplementos(cs: List[Numero]): String = cs.map(c => formatComplemento(c.n+1)).map("-" + _).mkString("")
  def formatComplemento(n: Int): String = formatAlfa(n - 1).toUpperCase
  
  def lastNth[A](n: Int, l:List[A]): A = l match {
    case tail if (tail.length == n) => tail.head
    case _ :: tail => lastNth(n, tail)
    case _ => throw new NoSuchElementException
}

}