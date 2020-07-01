package br.gov.lexml.urnformatter.compacto

import scala.util.Try
import org.slf4j.LoggerFactory;

object Urn2NomeCompacto {

  val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.Urn2NomeCompacto")

  def format(urn: String): String = {
    format(List(urn))
  }

  def format(urns: List[String]): String = {
    if (urns.isEmpty) {
      ""
    } else {
      Try {
        (UrnParser.parse _ andThen AgrupadorUrn.agrupar andThen Nomeador.nomearGrupos) (urns)
      }.recover {
        case t: Throwable =>
          logger.warn(s"Erro ao gerar urn compacta: $urns - ${t.getMessage}")
          ""
      }.get
    }
  }

}
