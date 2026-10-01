package br.gov.lexml.urnformatter.compacto

import org.slf4j.LoggerFactory
import br.gov.lexml.urnformatter.compacto.UrnFragmento.Artigo

import scala.util.Try;

object Urn2NomeCompacto {

  private val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Urn2NomeCompacto")

  def format(urn: String): String = format(List(urn), false)

  /**
   * Nomeia uma ou mais normas representadas por URN a partir de um norma contexto.
   *
   * Padrão de nomeação muda dependendo se existe uma referência ao próprio artigo ou não.
   *
   * urns: uma ou mais normas nomeadas
   * context: uma norma que menciona ou nomeia a lista em urns
   */
  def format(urns: List[String], context: String = ""): String =
    if (urns.isEmpty) {
      ""
    } else {
      if (urns.forall(UrnParser.hasCommonContext(_, context))) {
        val contextResponse = UrnParser.extractContext(urns, context)
        logger.info(s"formating with context. urnsWithoutContext: ${contextResponse.urns} - agrupador: ${contextResponse.agrupador}")
        val nome = if (contextResponse.urns.isEmpty) None else Some(format(contextResponse.urns, contextResponse.referenciaMesmoArtigo, contextResponse.nivelAnexoContexto))
        logger.info(s"nome: $nome")
        new Nomeador(Nil, contextResponse.referenciaMesmoArtigo).nomearDispositivo(nome, contextResponse.agrupador)
      } else {
        format(urns, false)
      }
    }

  /**
    * Retorna a mesma referência compacta de [[format]], preservando os grupos
    * que precisam de um prefixo gramatical independente (por exemplo, `no`
    * antes de cada capítulo cujo título é diferente).
    *
    * Esta entrada expõe nomes e propriedades gramaticais, mantendo o parsing
    * e o agrupamento das URNs privados ao formatador.
    */
  def formatarGrupos(urns: List[String], context: String): ResultadoNomeCompacto = {
    if (urns.isEmpty) {
      ResultadoNomeCompacto(Nil, "")
    } else if (urns.forall(UrnParser.hasCommonContext(_, context))) {
      val contextResponse = UrnParser.extractContext(urns, context)
      val grupos = AgrupadorUrn.agrupar(UrnParser.parse(contextResponse.urns))
      val nomeador = new Nomeador(grupos, false, contextResponse.nivelAnexoContexto)
      val blocos = nomeador.nomearBlocos
      val textos = nomeador.nomearGruposIndividuais
      val nomes = grupos.zip(textos).map { case (grupo, texto) => criarGrupoNomeCompacto(grupo, texto) }
      val complemento = if (contextResponse.agrupador.isEmpty) "" else {
        new Nomeador(Nil, false).nomearDispositivo(Some(""), contextResponse.agrupador).trim
      }
      ResultadoNomeCompacto(nomes, complemento, blocos)
    } else {
      val grupos = AgrupadorUrn.agrupar(UrnParser.parse(urns))
      val nomeador = new Nomeador(grupos, false)
      val blocos = nomeador.nomearBlocos
      val textos = nomeador.nomearGruposIndividuais
      val nomes = grupos.zip(textos).map { case (grupo, texto) => criarGrupoNomeCompacto(grupo, texto) }
      ResultadoNomeCompacto(nomes, "", blocos)
    }
  }

  private def criarGrupoNomeCompacto(grupo: GrupoUrns, texto: String): GrupoNomeCompacto = {
    // O prefixo concorda com o artigo que inicia a referência, mesmo quando
    // o destino é uma alínea ou um conjunto de dispositivos desse artigo.
    val artigo = grupo.fragmentosComum.collectFirst { case a: Artigo => a }
    artigo match {
      case Some(a) => GrupoNomeCompacto(texto, a.tipo.genero, a.numeros.size > 1)
      case None => GrupoNomeCompacto(texto, grupo.dispPrincipal.genero, grupo.numeros.size > 1)
    }
  }

  def format(urns: List[String]): String = format(urns, false)

  private def format(urns: List[String], referenciaMesmoArtigo: Boolean): String =
    format(urns, referenciaMesmoArtigo, 0)

  private def format(urns: List[String], referenciaMesmoArtigo: Boolean, nivelAnexoContexto: Int): String =
    if (urns.isEmpty) ""
    else {
      Try {
        val grupos = (UrnParser.parse _ andThen AgrupadorUrn.agrupar) (urns)
        new Nomeador(grupos, referenciaMesmoArtigo, nivelAnexoContexto).nomearGrupos
      }.recover {
        case t: Throwable =>
          logger.warn(s"Erro ao gerar urn compacta: $urns - ${t.getMessage}")
          ""
      }.get
    }
}
