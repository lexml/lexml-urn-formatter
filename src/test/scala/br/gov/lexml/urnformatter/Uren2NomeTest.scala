package br.gov.lexml.urnformatter

import junit.framework.Assert.assertEquals
import junit.framework.TestCase

class Uren2NomeTest extends TestCase {
    
  def testLabel_art1() {
    assertEquals("artigo 1º", Urn2Nome.format("art1"))

    assertEquals("artigo 1º-B", Urn2Nome.format("art1-2"))
    
    assertEquals("caput do artigo 1º", Urn2Nome.format("art1_cpt"))

    assertEquals("caput do artigo 1º do título II", Urn2Nome.format("tit2_art1_cpt"))

    assertEquals("título II", Urn2Nome.format("tit2"))
    
    assertEquals("título II-A", Urn2Nome.format("tit2-1"))
    
    assertEquals("caput do artigo 1º do título II-A", Urn2Nome.format("tit2-1_art1_cpt"))
    
    assertEquals("inciso I", Urn2Nome.format("inc_1"))
    
    assertEquals("alínea a", Urn2Nome.format("ali_1"))
    
    assertEquals("item 1", Urn2Nome.format("ite_1"))


  }

  def testAgrupadores() {
    assertEquals("título I", Urn2Nome.format("tit1"))
    assertEquals("capítulo I do título I", Urn2Nome.format("tit1_cap1"))
    assertEquals("seção II do capítulo I do título I", Urn2Nome.format("tit1_cap1_sec2"))
    assertEquals("subseção IV da seção II do capítulo I do título I", Urn2Nome.format("tit1_cap1_sec2_sub4"))
    assertEquals("subseção IV da seção II do capítulo I do título I do livro III", Urn2Nome.format("liv3_tit1_cap1_sec2_sub4"))
    assertEquals("livro III do anexo a", Urn2Nome.format("anx1_liv3"))

    assertEquals("artigo 1º do componente principal da raiz", Urn2Nome.format("lex_cpp_art1"))
    assertEquals("artigo 1º do anexo a do componente principal da raiz", Urn2Nome.format("lex_cpp_anx1_art1"))
    assertEquals("artigo 2º do anexo a do componente principal da raiz", Urn2Nome.format("lex_cpp_anx1_art2"))
  }

  
}
