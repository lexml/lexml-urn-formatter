package br.gov.lexml.urnformatter.compacto

import UrnFragmento._

private[compacto] case class ParsedUrn(inicioComum: String, disPrincipal: String, numero: Numero)

private[compacto] case class GrupoUrns(dispPrincipal: String, partesComum: List[UrnFragmento], numeracao: Numeracao)

private[compacto] object UrnFragmento {

  trait DispositivoAgrupador {
    val conector: String
  }

  sealed abstract class UrnFragmento

  case class Artigo(numeracao: Numeracao) extends UrnFragmento

  case object Caput extends UrnFragmento

  case object ParagrafoUnico extends UrnFragmento

  case class Inciso(numeracao: Numeracao) extends UrnFragmento

  case class Alinea(numeracao: Numeracao) extends UrnFragmento

  case class Paragrafo(numeracao: Numeracao) extends UrnFragmento

  case class Item(numeracao: Numeracao) extends UrnFragmento

  case class Parte(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case class Titulo(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case class Capitulo(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case class Secao(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case class SubSecao(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case class Livro(numeracao: Numeracao) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case class Anexo(numeracao: Numeracao, nivel: Int) extends UrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

}
