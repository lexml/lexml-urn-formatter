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
    
    assertEquals("inciso I", Urn2Nome.format("inc1"))
    
    assertEquals("alínea a", Urn2Nome.format("ali1"))
    
    assertEquals("item 1", Urn2Nome.format("ite1"))


  }

  
}
