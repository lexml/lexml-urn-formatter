package br.gov.lexml.urnformatter.compacto

//TODO: Teste/verificar se algo quebra com dispositivo com numero grande (1000 e pk por exemplo)
//TODO: teste com multiplo que tem caput
object Urn2NomeCompacto_New {

  def format(urn: String): String = {
    format(List(urn))
  }

  def format(urns: List[String]): String = {
    if (urns.isEmpty) {
      ""
    } else {
      //TODO: Compose functions
      val urnsGrupo = UrnParser.parse(urns)
      val urnsAgrupadas = AgrupadorUrn.agrupar(urnsGrupo)

      println("==> urnsGrupo")
      println(urnsGrupo)
      println("==> grupos")
      println(urnsAgrupadas.foreach(println))

      Nomeador.nomearGrupos(urnsAgrupadas)
    }
  }
}
