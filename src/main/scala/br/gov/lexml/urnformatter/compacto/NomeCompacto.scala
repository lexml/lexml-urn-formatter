package br.gov.lexml.urnformatter.compacto

sealed trait Genero

object Genero {
  case object Masculino extends Genero
  case object Feminino extends Genero
}

case class GrupoNomeCompacto(texto: String, genero: Genero, plural: Boolean)

case class ResultadoNomeCompacto(grupos: List[GrupoNomeCompacto], complemento: String) {

  def formatar(prefixo: GrupoNomeCompacto => String): String = {
    val texto = grupos
      .map { grupo =>
        val prefixoGrupo = prefixo(grupo)
        if (prefixoGrupo == null || prefixoGrupo.trim.isEmpty) grupo.texto
        else prefixoGrupo.trim + " " + grupo.texto
      }
      .mkString(" e ")
    if (complemento.isEmpty) texto
    else if (texto.isEmpty) complemento
    else texto + " " + complemento
  }
}
