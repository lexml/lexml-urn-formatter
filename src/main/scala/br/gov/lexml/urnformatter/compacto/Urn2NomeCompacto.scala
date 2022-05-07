package br.gov.lexml.urnformatter.compacto

import org.slf4j.LoggerFactory

import scala.util.Try;

object Urn2NomeCompacto {

  val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Urn2NomeCompacto")

  def format(urn: String): String = format(List(urn))

  /**
   * Nomeia uma ou mais normas representadas por URN a partir de um norma contexto
   *
   * urns: uma ou mais normas nomeadas
   * context: uma norma que menciona ou nomeia a lista em urns
   */
  def format(urns: List[String], context: String = ""): String =
    if (UrnParser.hasCommonContext(urns.head, context)) {
      val (urnsWithoutContext, agrupador) = UrnParser.extractContext(urns, context)
      logger.info(s"formating with context. urnsWithoutContext: $urnsWithoutContext - agrupador: $agrupador")
      val nome = if (urnsWithoutContext.isEmpty) None else Some(format(urnsWithoutContext))
      Nomeador.nomearDispositivo(nome, agrupador)
    } else {
      format(urns)
    }

  def format(urns: List[String]): String =
    if (urns.isEmpty) ""
    else {
      Try {
        (UrnParser.parse _ andThen AgrupadorUrn.agrupar andThen Nomeador.nomearGrupos) (urns)
      }.recover {
        case t: Throwable =>
          logger.warn(s"Erro ao gerar urn compacta: $urns - ${t.getMessage}")
          ""
      }.get
    }
}
