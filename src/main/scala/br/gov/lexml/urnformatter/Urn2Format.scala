package br.gov.lexml.urnformatter

object Urn2Format {

  val compReN = "^([a-z]+\\*+?|[a-z]+)_((?:1u|[0-9-])*)$".r
  val compRe = "^([a-z]+\\*+?|[a-z]+)((?:1u|[0-9-])*)$".r
  val artRe = "^anx((?:1u|[0-9-])*)$".r

  abstract sealed class Numero {
    val n: Int
  }

  final case object Unico extends Numero {
    override val n = 1
  }

  final case class Algum(n: Int) extends Numero

  def readInt: String => Numero = {
    case "1u" => Unico
    case x => Algum(x.toInt)
  }

  def formatOrdinal(num: Int): String = 
    renderNumeral(num) + (if (num < 10) "º" else "")

  def renderNumeral(num : Int) : String = {
        if (num > 1000) { f"${renderNumeral(num / 1000)}.${num % 1000}%03d" }
        else { num.toString }
  }

  def formatRomano(num: Int): String = {
     def rom(cX: String, cV: String, cI: String, d: Int): String = d match {
        case 0 ⇒ ""
        case 9 ⇒ cI + cX
        case 4 ⇒ cI + cV
        case _ if d >= 5 ⇒ cV + (cI * (d - 5))
        case _ ⇒ cI * d
     }
     ("M" * (num / 1000)) + rom("M", "D", "C", (num / 100) % 10) + rom("C", "L", "X", (num / 10) % 10) + rom("X", "V", "I", num % 10)
  }

  def formatAlfa(num: Int): String = {
     def rend(n: Int): String = n match {
         case 0 ⇒ ""
         case _ ⇒ {
           val nn = n - 1
           rend(nn / 26) + ('a' + (nn % 26)).asInstanceOf[Char]
         }
     }
     rend(num)
  }

  def formatComplementos(cs: List[Numero]): String = cs.map(c => formatComplemento(c.n + 1)).map("-" + _).mkString("")
  def formatComplemento(n: Int): String = formatAlfa(n - 1).toUpperCase

}
