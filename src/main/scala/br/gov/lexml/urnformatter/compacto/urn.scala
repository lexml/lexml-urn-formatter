package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.UrnFragmento._

private[compacto] case class ParsedUrn(inicioComum: String, disPrincipal: String, numero: Numero)

private[compacto] case class GrupoUrns(dispPrincipal: TipoUrnFragmento, fragmentosComum: List[UrnFragmento], numeracao: Numeracao)

private[compacto] sealed abstract class TipoUrnFragmento

private[compacto] object TipoUrnFragmento {

  trait DispositivoAgrupador {
    val conector: String
  }

  case object Artigo extends TipoUrnFragmento

  case object Caput extends TipoUrnFragmento

  case object ParagrafoUnico extends TipoUrnFragmento

  case object Inciso extends TipoUrnFragmento

  case object Alinea extends TipoUrnFragmento

  case object Paragrafo extends TipoUrnFragmento

  case object Item extends TipoUrnFragmento

  case object Parte extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case object Titulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case object Capitulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case object Secao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case object SubSecao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
  }

  case object Livro extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

  case object Anexo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
  }

}

private[compacto] object UrnFragmento {

  sealed abstract class UrnFragmento(val tipo: TipoUrnFragmento)

  case class Artigo(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Artigo)

  case object Caput extends UrnFragmento(TipoUrnFragmento.Caput)

  case object ParagrafoUnico extends UrnFragmento(TipoUrnFragmento.ParagrafoUnico)

  case class Inciso(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Inciso)

  case class Alinea(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Alinea)

  case class Paragrafo(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Paragrafo)

  case class Item(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Item)

  case class Parte(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Parte)

  case class Titulo(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Titulo)

  case class Capitulo(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Capitulo)

  case class Secao(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Secao)

  case class SubSecao(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.SubSecao)

  case class Livro(numeracao: Numeracao) extends UrnFragmento(TipoUrnFragmento.Livro)

  case class Anexo(numeracao: Numeracao, nivel: Int) extends UrnFragmento(TipoUrnFragmento.Anexo)

}
