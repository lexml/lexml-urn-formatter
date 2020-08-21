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

  def hasCommomContext(urn: String, context: String): Boolean = {
    val urnSpplited = urn.split("_")
    val isArt = urnSpplited.last.contains("art")
    val commomContext = extractCommomContext(urn, context)
    logger.info(s"hasCommomContext: urn $urn - context $context - commomContext $commomContext")
    !isArt && urnSpplited.size > 1 && commomContext != null
  }

  type Urn = String
  type Agrupador = String
  def extractContext(urn: String, context: String): (Option[Urn], Agrupador) = {
    if (!hasCommomContext(urn, context)) throw new IllegalArgumentException("Sem contexto em comum.")

    val extractContextRegex = s"""^($urn){0,1}(.*)""".r
    val extractContextRegex(prefix, suffix) = context
    val sameContext = prefix != null

    val commomContext = extractCommomContext(urn, context)
    val commomContextSpplited = commomContext.split("_")
    val commomContextSize = commomContextSpplited.size

    val urnSpplited = urn.split("_")
    val urnWithoutContext = if (sameContext) None else Some(urnSpplited.takeRight(urnSpplited.size - commomContextSize).mkString("_"))
    val agrupador = if (sameContext) urnSpplited.last.take(3) else commomContextSpplited.last.take(3)
    logger.info(s"extractContext: $urn $urnSpplited - urnWithoutContext $urnWithoutContext - commomContext $commomContext - agrupador $agrupador")
    (urnWithoutContext, agrupador)
  }

  private def extractCommomContext(urn: String, context: String): String =
    urn.zip(context).takeWhile(Function.tupled(_ == _)).map(_._1).mkString.split("_").init.mkString("_")

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
