package br.gov.lexml.urnformatter.compacto

import scala.util.Try

object Urn2NomeCompacto {

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
          println(s"Erro ao gerar urn compacta: ${t.getMessage}")
          t.printStackTrace
          ""
      }.get
    }
  }

}
