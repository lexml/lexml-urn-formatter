package br.gov.lexml.urnformatter.compacto

import scala.util.{Success, Try}
import org.slf4j.LoggerFactory
import scala.annotation.tailrec

private[compacto] object UrnParser {

  val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.UrnParser")

  def parse(urns: List[String]): List[ParsedUrn] = urns.map { urn =>
    val fragmentos = (trataArtigo andThen trataCaputNoMeio andThen removeRaizEComponentePrincipal) (urn.split("_").toList)
    val ultimoFragmento = fragmentos.last
    val dispPrincipal = parseTipoDispositivo(ultimoFragmento)
    // Inicio Comum contém os fragmentos + o tipo do dispositivo principal, sem numeracao
    val inicioComum = fragmentos.dropRight(1) :+ dispPrincipal
    val numero = parseNumeroFragmento(ultimoFragmento)
    ParsedUrn(inicioComum.mkString("_"), dispPrincipal, numero)
  }

  def hasCommonContext(urn: String, context: String): Boolean = {
    val urnSpplited = urn.split("_")
    val commonContext = extractCommonContext(urn, context)
    println(s"hasCommonContext: urn $urn - context $context - commonContext $commonContext")
    urnSpplited.size > 1 && commonContext != ""
  }

  type Urn = String
  type Agrupador = String
  def extractContext(urns: List[String], context: String): (List[Urn], Agrupador) = {
    @tailrec
    def extract(urns: List[String], acc: List[Option[Urn]]): (List[Option[Urn]], Agrupador) = urns match  {
      case head :: Nil =>
        val result: (Option[Urn], Agrupador) = extractContext(head, context)
        (acc :+ result._1, result._2)

      case head :: tail =>
        val result: (Option[Urn], Agrupador) = extractContext(head, context)
        extract(tail, acc :+ result._1)

      case Nil => (acc, "")
    }
    val result = extract(urns, List())
    (result._1.flatten, result._2)
  }

  def extractContext(urn: String, context: String): (Option[Urn], Agrupador) = {
    if (!hasCommonContext(urn, context)) throw new IllegalArgumentException("Sem contexto em comum.")

    val commonContext = extractCommonContext(urn, context)
    println(s"## commonContext: $commonContext")
    val commonContextSpplited = commonContext.split("_")
    println(s"## commonContextSpplited: ${commonContextSpplited.mkString(",")}")
    val commonContextSize = commonContextSpplited.size
    println(s"## commonContextSize: $commonContextSize")

    val urnSpplited = urn.split("_")
    println(s"## urnSpplited: ${urnSpplited.mkString(",")}")
    val isArt = urnSpplited.last.startsWith("art")
    val isFilhoDeAnx = urnSpplited.head.startsWith("anx")
    println(s"## isArt: $isArt - isFilhoDeAnx: $isFilhoDeAnx")
    if (isArt) {
      return if (commonContext == urn) (None, "art")
      else (Some(urnSpplited.last), if (isFilhoDeAnx) "anx" else "")
    }

    ///
    if (urnSpplited.exists(_.startsWith("art"))) {
      println("## Contem art")
      //TODO: remove return
      return (Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_")), "")
    }

    ///

    val extractContextRegex = s"""^($urn)(_(.*)|$$)""".r
//    val extractContextRegex = s"""^($urn){0,1}(.*)""".r

//    println(s"===== ${extractContextRegex.findFirstIn(context)}")
//    val extractContextRegex(prefix, suffix) = context
//    val sameContext = prefix != null
//    println(s"## prefix: $prefix - sameContext: $sameContext")

    val maybePrefix = extractContextRegex.findFirstIn(context)
    println(s"## maybePrefix: $maybePrefix")
    val sameContext = maybePrefix.isDefined

    val urnWithoutContext = if (sameContext) None else Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_"))
    val agrupador = if (sameContext) urnSpplited.last.take(3) else commonContextSpplited.last.take(3)
    println(s"## extractContext: urn $urn - context $context - urnWithoutContext $urnWithoutContext - commonContext $commonContext - agrupador $agrupador")

    val isAnexoSameContext = urnSpplited.head.contains("anx") && urnSpplited.head == commonContextSpplited.head
    if (isAnexoSameContext) (Some(urnSpplited.last), commonContextSpplited.head.take(3))
    else (urnWithoutContext, agrupador)
  }

  /**
  * Extract the deepest common context
  * urn: cpp_tit3_cap3_sec2, context: cpp_tit3_cap4_sec1_art76_cpt_inc4, result => cpp_tit3
  * urn: cpp_tit3_cap4_art72_par1, context: cpp_tit3_cap4_art72_cpt, result => cpp_tit3_cap4_art72
  * urn: cpp_tit3_cap4_art72, context: cpp_tit3_cap4_art72_inc1, result => cpp_tit3_cap4_art72
  * urn: cpp_tit3_cap4_art72_par1_inc3, context: cpp_tit3_cap4_art72_par1, result => cpp_tit3_cap4_art72_par1
  */
  private def extractCommonContext(urn: String, context: String): String = {
    println("-- extractCommonContext")
    val commonContext = urn.split("_").zip(context.split("_")).takeWhile(Function.tupled(_ == _)).map(_._1).mkString("_")
    //val commonContext = urn.zip(context).takeWhile(Function.tupled(_ == _)).map(_._1).mkString
    println(s"-- commonContext: $commonContext")
    val commonContextSpplited = commonContext.split("_")
    val considerarUltimoFragmento = urn.split("_").contains(commonContextSpplited.last)
    if (commonContext.endsWith("_") || commonContext == urn || considerarUltimoFragmento) commonContextSpplited.mkString("_")
    else commonContextSpplited.init.mkString("_")
  }

  /**
   * Tratamento especial para caso de artigos, caso exista um.
   * Se existir um artigo, remove todos fragmentos antecessores à ele, exceto se for anexo.
   */
  private def trataArtigo: List[String] => List[String] = { fragmentos =>
    val posArtigo = fragmentos.indexWhere(_.startsWith("art"))

    if (posArtigo != -1) {
      fragmentos.filter(p => p.startsWith("art") || p.startsWith("anx")) ++
        fragmentos.takeRight(fragmentos.size - posArtigo - 1)
    } else {
      fragmentos
    }
  }

  /**
   * Se existe um caput e esse não é o último fragmento da urn, o mesmo é removido.
   */
  private def trataCaputNoMeio: List[String] => List[String] = { fragmentos =>
    val contemCaputAntesDoFim = fragmentos.dropRight(1).exists(_.startsWith("cpt"))

    if (contemCaputAntesDoFim) {
      fragmentos.filter(!_.startsWith("cpt"))
    } else {
      fragmentos
    }
  }

  private def removeRaizEComponentePrincipal: List[String] => List[String] = { fragmentos =>
    fragmentos.filterNot(p => p.startsWith("lex") || p.startsWith("cpp"))
  }

  private def parseTipoDispositivo(fragmento: String): String =
    if (!fragmento.contains("par1u")) fragmento.take(3) else "par1u"

  private def parseNumeroFragmento(fragmento: String): Numero =
    if (fragmento.size == 3) {
      Numero.SemNumero
    } else if (fragmento.endsWith("1u")) {
      Numero.Unico
    } else {
      val numeroStr = fragmento.drop(3)
      Try(numeroStr.toInt) match {
        case Success(i) => Numero.IntNumero(i)
        case _ => Numero.StrNumero(numeroStr)
      }
    }

}
