package br.gov.lexml.urnformatter

import junit.framework.Assert.assertEquals
import junit.framework.TestCase

class Uren2NomeCompactoTest extends TestCase {
  
  def testLabel_art1() {
    assertEquals("art. 1º", Uren2NomeCompactoTest.format("art1"))
  }

  
}
