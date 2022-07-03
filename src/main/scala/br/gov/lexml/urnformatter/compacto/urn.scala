package br.gov.lexml.urnformatter.compacto

import br.gov.lexml.urnformatter.compacto.UrnFragmento._

private[compacto] case class ParsedUrn(inicioComum: String, disPrincipal: String, numero: Numero)

private[compacto] case class GrupoUrns(dispPrincipal: TipoUrnFragmento, fragmentosComum: List[UrnFragmento], numeros: List[Numero]) {

  def posAnexo: Int = fragmentosComum.indexWhere {
    case _: Anexo => true
    case _ => false
  }

}

private[urnformatter] sealed abstract class TipoUrnFragmento {
  val pronomeDemostrativo: String
  val conector: String
  val sigla: String
}

private[urnformatter] object TipoUrnFragmento {

  trait DispositivoAgrupador

  case object Artigo extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "art"
  }

  case object Caput extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "cpt"
  }

  case object ParagrafoUnico extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "par1u"
  }

  case object Inciso extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "inc"
  }

  case object Alinea extends TipoUrnFragmento {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "ali"
  }

  case object Paragrafo extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "par"
  }

  case object Item extends TipoUrnFragmento {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "ite"
  }

  case object Parte extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "prt"
  }

  case object Titulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "tit"
  }

  case object Capitulo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "cap"
  }

  case object Secao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "sec"
  }

  case object SubSecao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "sub"
  }

  case object Livro extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "liv"
  }

  case object Anexo extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "anx"
  }

  case object Omissis extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "omi"
  }

  case object Cpp extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "do"
    override val pronomeDemostrativo: String = "deste"
    override val sigla: String = "cpp"
  }

  case object Raiz extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "lex"
  }

  case object Alteracao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "alt"
  }

  case object Articulacao extends TipoUrnFragmento with DispositivoAgrupador {
    override val conector: String = "da"
    override val pronomeDemostrativo: String = "desta"
    override val sigla: String = "atc"
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

