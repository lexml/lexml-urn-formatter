package br.gov.lexml.urnformatter.compacto

sealed trait Genero

object Genero {
  case object Masculino extends Genero
  case object Feminino extends Genero
}

case class GrupoNomeCompacto(texto: String, genero: Genero, plural: Boolean)

/** Artigos de um mesmo caminho de anexos, na ordem da remissão. */
case class BlocoNomeCompacto(grupos: List[GrupoNomeCompacto], complemento: String)

case class ResultadoNomeCompacto(grupos: List[GrupoNomeCompacto], complemento: String,
                                blocos: List[BlocoNomeCompacto] = Nil) {

  def formatar(prefixo: GrupoNomeCompacto => String): String = {
    def comPrefixo(grupo: GrupoNomeCompacto): String = {
      val valor = Option(prefixo(grupo)).getOrElse("").trim
      if (valor.isEmpty) grupo.texto else valor + " " + grupo.texto
    }
    val texto = if (blocos.nonEmpty) {
      blocos.map { bloco =>
        val nomes = bloco.grupos.headOption.map(comPrefixo).toList ++ bloco.grupos.drop(1).map(_.texto)
        val referencia = nomes.mkString(", ")
        if (bloco.complemento.isEmpty) referencia else referencia + " " + bloco.complemento
      }.mkString(" e ")
    } else grupos
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
