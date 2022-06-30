package br.gov.lexml.urnformatter.compacto

import org.slf4j.LoggerFactory

import scala.util.Try;

object Urn2NomeCompacto {

  private val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Urn2NomeCompacto")

  def format(urn: String): String = format(List(urn), false)

  /**
   * Nomeia uma ou mais normas representadas por URN a partir de um norma contexto
   *
   * urns: uma ou mais normas nomeadas
   * context: uma norma que menciona ou nomeia a lista em urns
   */
  def format(urns: List[String], context: String = ""): String =
    if (urns.isEmpty) {
      ""
    } else {
      if (UrnParser.hasCommonContext(urns.head, context)) {
        val (urnsWithoutContext, agrupador, referenciaMesmoArtigo) = UrnParser.extractContext(urns, context)
        println(s"formating with context. urnsWithoutContext: $urnsWithoutContext - agrupador: $agrupador")
        val nome = if (urnsWithoutContext.isEmpty) None else Some(format(urnsWithoutContext, referenciaMesmoArtigo))
        println(s"nome: $nome")
        new Nomeador(Nil, referenciaMesmoArtigo).nomearDispositivo(nome, agrupador) //TOOD: Why Nil?
      } else {
        format(urns, false)
      }
    }

  def format(urns: List[String], referenciaMesmoArtigo: Boolean): String =
    if (urns.isEmpty) ""
    else {
      Try {
        val grupos = (UrnParser.parse _ andThen AgrupadorUrn.agrupar)(urns)
        new Nomeador(grupos, referenciaMesmoArtigo).nomearGrupos
      }.recover {
        case t: Throwable =>
          logger.warn(s"Erro ao gerar urn compacta: $urns - ${t.getMessage}")
          ""
      }.get
    }
}
