package br.gov.lexml.urnformatter.compacto

import scala.util.{Success, Try}
import org.slf4j.LoggerFactory;

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
    logger.info(s"hasCommonContext: urn $urn - context $context - commonContext $commonContext")
    urnSpplited.size > 1 && commonContext != ""
  }

  type Urn = String
  type Agrupador = String
  def extractContext(urn: String, context: String): (Option[Urn], Agrupador) = {
    if (!hasCommonContext(urn, context)) throw new IllegalArgumentException("Sem contexto em comum.")

    val urnSpplited = urn.split("_")
    val isArt = urnSpplited.last.contains("art")
    if (isArt) return (Some(urnSpplited.last), "")

    val extractContextRegex = s"""^($urn){0,1}(.*)""".r
    val extractContextRegex(prefix, suffix) = context
    val sameContext = prefix != null

    val commonContext = extractCommonContext(urn, context)
    val commonContextSpplited = commonContext.split("_")
    val commonContextSize = commonContextSpplited.size

    val urnWithoutContext = if (sameContext) None else Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_"))
    val agrupador = if (sameContext) urnSpplited.last.take(3) else commonContextSpplited.last.take(3)
    logger.info(s"extractContext: urn $urn - context $context - urnWithoutContext $urnWithoutContext - commonContext $commonContext - agrupador $agrupador")

    val isAnexoSameContext = urnSpplited.head.contains("anx") && urnSpplited.head == commonContextSpplited.head
    if (isAnexoSameContext) (Some(urnSpplited.last), commonContextSpplited.head.take(3))
    else (urnWithoutContext, agrupador)
  }

  /**
  * Extract the deepest common context
  * urn: cpp_tit3_cap3_sec2, context: cpp_tit3_cap4_sec1_art76_cpt_inc4, result => cpp_tit3
  * urn: cpp_tit3_cap4_art72_par1, context: cpp_tit3_cap4_art72_cpt, result => cpp_tit3_cap4_art72
  */
  private def extractCommonContext(urn: String, context: String): String = {
    val commonContext = urn.zip(context).takeWhile(Function.tupled(_ == _)).map(_._1).mkString
    if (commonContext.endsWith("_")) commonContext.split("_").mkString("_")
    else commonContext.split("_").init.mkString("_")
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
