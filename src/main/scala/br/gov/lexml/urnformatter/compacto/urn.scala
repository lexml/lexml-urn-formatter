package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.UrnFragmento._

private[compacto] case class ParsedUrn(inicioComum: String, disPrincipal: String, numero: Numero)

private[compacto] case class GrupoUrns(dispPrincipal: TipoUrnFragmento, fragmentosComum: List[UrnFragmento], numeros: List[Numero]) {

  def posAnexo: Int = fragmentosComum.indexWhere {
    case _: Anexo => true
    case _ => false
  }

}

private[compacto] sealed abstract class TipoUrnFragmento {
  val pronomeDemostrativo: String
  val conector: String
}

private[compacto] object TipoUrnFragmento {

  trait DispositivoAgrupador

  case object Artigo extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Caput extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object ParagrafoUnico extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Inciso extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Alinea extends TipoUrnFragmento {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
  }

  case object Paragrafo extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Item extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Parte extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
  }

  case object Titulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Capitulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Secao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
  }

  case object SubSecao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
  }

  case object Livro extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

  case object Anexo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
  }

}

private[compacto] object UrnFragmento {

  sealed abstract class UrnFragmento(val tipo: TipoUrnFragmento)

  case class Artigo(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Artigo)

  case object Caput extends UrnFragmento(TipoUrnFragmento.Caput)

  case object ParagrafoUnico extends UrnFragmento(TipoUrnFragmento.ParagrafoUnico)

  case class Inciso(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Inciso)

  case class Alinea(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Alinea)

  case class Paragrafo(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Paragrafo)

  case class Item(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Item)

  case class Parte(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Parte)

  case class Titulo(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Titulo)

  case class Capitulo(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Capitulo)

  case class Secao(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Secao)

  case class SubSecao(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.SubSecao)

  case class Livro(numeros: List[Numero]) extends UrnFragmento(TipoUrnFragmento.Livro)

  case class Anexo(numeros: List[Numero], nivel: Int) extends UrnFragmento(TipoUrnFragmento.Anexo)
}

