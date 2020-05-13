package br.gov.lexml.urnformatter.compacto

object Urn2NomeCompacto_New {

  def format(urn: String): String = {
    format(List(urn))
  }

  def format(urns: List[String]): String = {
    if (urns.isEmpty) {
      ""
    } else {
      (UrnParser.parse _ andThen AgrupadorUrn.agrupar andThen Nomeador.nomearGrupos) (urns)
    }
  }

}
