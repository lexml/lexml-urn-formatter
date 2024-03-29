package br.gov.lexml.urnformatter.compacto

import scala.util.{Success, Try}
import org.slf4j.LoggerFactory
import scala.annotation.tailrec
import br.gov.lexml.urnformatter.compacto.TipoUrnFragmento._

private[compacto] object UrnParser {

  type Urn = String
  type Agrupador = String
  private case class ContextReponseListOpt(urns: List[Option[Urn]], agrupador: Agrupador, referenciaMesmoArtigo: Boolean)
  private case class ContextReponseOpt(urn: Option[Urn], agrupador: Agrupador, referenciaMesmoArtigo: Boolean)
  case class ContextReponse(urns: List[Urn], agrupador: Agrupador, referenciaMesmoArtigo: Boolean)

  private val logger = LoggerFactory.getLogger("br.gov.lexml.urnformatter.compacto.UrnParser")

  def parse(urns: List[String]): List[ParsedUrn] = urns.map { urn =>
    val fragmentos = (trataArtigo andThen trataCaputNoMeio andThen removeRaizECppEAtc) (urn.split("_").toList)
    val ultimoFragmento = fragmentos.last
    val dispPrincipal = parseTipoDispositivo(ultimoFragmento)
    // Inicio Comum contém os fragmentos + o tipo do dispositivo principal, sem numeracao
    val inicioComum = fragmentos.dropRight(1) :+ dispPrincipal
    val numero = parseNumeroFragmento(ultimoFragmento)
    ParsedUrn(inicioComum.mkString("_"), dispPrincipal, numero)
  }

  private[compacto] def hasCommonContext(urn: String, context: String): Boolean = {
    val urnSpplited = urn.split("_")
    val commonContext = extractCommonContext(urn, context)
    logger.info(s"hasCommonContext: urn $urn - context $context - commonContext $commonContext")
    urnSpplited.size > 1 && commonContext != ""
  }

  def extractContext(urns: List[String], context: String): ContextReponse = {
    @tailrec
    def extract(urns: List[String], acc: List[Option[Urn]]): ContextReponseListOpt = urns match {
      case head :: Nil =>
        val result = extractContext(head, context)
        ContextReponseListOpt(acc :+ result.urn, result.agrupador, result.referenciaMesmoArtigo)

      case head :: tail =>
        val result = extractContext(head, context)
        extract(tail, acc :+ result.urn)

      case Nil => ContextReponseListOpt(acc, "", false)
    }

    val result = extract(urns, List())
    ContextReponse(result.urns.flatten, result.agrupador, result.referenciaMesmoArtigo)
  }

  private def extractContext(urn: String, context: String): ContextReponseOpt = {
    if (!hasCommonContext(urn, context)) throw new IllegalArgumentException("Sem contexto em comum.")
    val urnSpplited = urn.split("_")
    val commonContext = extractCommonContext(urn, context)

    if (urn == commonContext) {
      ContextReponseOpt(if (ehArtigo(urnSpplited.last)) Some(Caput.sigla) else Some(urnSpplited.last), "", false)
    } else {
      val commonContextSplit = commonContext.split("_")
      val commonContextSize = commonContextSplit.size

      val posArt = urnSpplited.indexWhere(ehArtigo)
      val isArt = posArt == (urnSpplited.length - 1)
      val isFilhoDeAnx = urnSpplited.head.startsWith(Anexo.sigla)

      if (isArt) {
        if (commonContext == urn) {
          ContextReponseOpt(None, Artigo.sigla, false)
        } else {
          ContextReponseOpt(Some(urnSpplited.last), if (isFilhoDeAnx) Anexo.sigla else "", false)
        }
      } else if (urnSpplited.exists(ehArtigo)) {
        val artUrn = urnSpplited(posArt)
        val artContext = context.split("_").find(ehArtigo)
        val referenciaMesmoArtigo = artContext.map(_ == artUrn).contains(true)

        if (isFilhoDeAnx) {
          if (urnSpplited.head == commonContextSplit.head && referenciaMesmoArtigo) {
            ContextReponseOpt(Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_")), "", referenciaMesmoArtigo)
          } else {
            ContextReponseOpt(Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_")), Anexo.sigla, referenciaMesmoArtigo)
          }
        } else {
          ContextReponseOpt(Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_")), "", referenciaMesmoArtigo)
        }
      } else {
        val extractContextRegex = s"""^($urn)(_(.*)|$$)""".r
        val maybePrefix = extractContextRegex.findFirstIn(context)

        val (urnWithoutContext, agrupador) = maybePrefix match {
          case Some(_) => (None, urnSpplited.last.take(3))
          case None => (Some(urnSpplited.takeRight(urnSpplited.size - commonContextSize).mkString("_")), commonContextSplit.last.take(3))
        }
        if (commonContext == urn) {
          ContextReponseOpt(Some(urnSpplited.last), "", false)
        } else {
          val isAnexoSameContext = isFilhoDeAnx && urnSpplited.head == commonContextSplit.head
          if (isAnexoSameContext) {
            ContextReponseOpt(Some(urnSpplited.last), commonContextSplit.head.take(3), false)
          } else {
            ContextReponseOpt(urnWithoutContext, agrupador, false)
          }
        }
      }
    }
  }

  /**
   * Extract the deepest common context
   * urn: cpp_tit3_cap3_sec2, context: cpp_tit3_cap4_sec1_art76_cpt_inc4, result => cpp_tit3
   * urn: cpp_tit3_cap4_art72_par1, context: cpp_tit3_cap4_art72_cpt, result => cpp_tit3_cap4_art72
   * urn: cpp_tit3_cap4_art72, context: cpp_tit3_cap4_art72_inc1, result => cpp_tit3_cap4_art72
   * urn: cpp_tit3_cap4_art72_par1_inc3, context: cpp_tit3_cap4_art72_par1, result => cpp_tit3_cap4_art72_par1
   */
  private def extractCommonContext(urn: String, context: String): String = {
    val commonContext = urn.split("_").zip(context.split("_")).takeWhile(Function.tupled(_ == _)).map(_._1).mkString("_")
    val commonContextSpplited = commonContext.split("_")
    val considerarUltimoFragmento = urn.split("_").contains(commonContextSpplited.last)
    if (commonContext.endsWith("_") || commonContext == urn || considerarUltimoFragmento) commonContextSpplited.mkString("_")
    else commonContextSpplited.init.mkString("_")
  }

  /**
   * Tratamento especial para caso de artigos, caso exista um.
   * Se existir um artigo, remove todos fragmentos antecessores à ele, exceto se for anexo.
   */
  private def trataArtigo: List[String] => List[String] = { fragmentos =>
    val posArtigo = fragmentos.indexWhere(ehArtigo)

    if (posArtigo != -1) {
      fragmentos.filter(p => ehArtigo(p) || p.startsWith(Anexo.sigla)) ++
        fragmentos.takeRight(fragmentos.size - posArtigo - 1)
    } else {
      fragmentos
    }
  }

  /**
   * Se existe um caput E esse não é o último fragmento da urn E existe um artigo, o mesmo é removido.
   */
  private def trataCaputNoMeio: List[String] => List[String] = { fragmentos =>
    val contemCaputAntesDoFim = fragmentos.dropRight(1).exists(_.startsWith(Caput.sigla))
    val posArtigo = fragmentos.exists(ehArtigo)

    if (contemCaputAntesDoFim && posArtigo) {
      fragmentos.filter(!_.startsWith(Caput.sigla))
    } else {
      fragmentos
    }
  }

  private def ehArtigo(fragmento: String): Boolean = fragmento.startsWith(Artigo.sigla)

  private[compacto] def removeRaizECppEAtc: List[String] => List[String] = { fragmentos =>
    fragmentos.filterNot(p => p.startsWith(Raiz.sigla) || p.startsWith(Cpp.sigla) || p.startsWith(Articulacao.sigla))
  }

  private def parseTipoDispositivo(fragmento: String): String =
    if (!fragmento.contains(ParagrafoUnico.sigla)) fragmento.take(3) else ParagrafoUnico.sigla

  private def parseNumeroFragmento(fragmento: String): Numero =
    if (fragmento.size == 3) {
      Numero.SemNumero
    } else if (fragmento.endsWith("1u")) {
      Numero.Unico
    } else {
      val numeroStr = fragmento.drop(3)
      Try(numeroStr.toInt) match {
        case Success(i) => Numero.IntNumero(i)
        case _ => Numero.StrNumero(numeroStr)
      }
    }

}
