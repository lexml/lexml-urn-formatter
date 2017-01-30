package urn2label

import junit.framework.TestCase
import junit.framework.Assert.assertEquals

class Urn2LabelTest extends TestCase {
  
  def testLabel_art1() {
    assertEquals(Fragment2Label.format("art1"), "Art. 1º")
  }
  
  def testLabel_art1u() {
    assertEquals(Fragment2Label.format("art1u"), "Art. Único.")
  }
  
  def testLabel_art2() {
    assertEquals(Fragment2Label.format("art2"), "Art. 2º")
  }
  
  def testLabel_art12() {
    assertEquals(Fragment2Label.format("art12"), "Art. 12.")
  }
  
  def testLabel_art1_1() {
    assertEquals(Fragment2Label.format("art1-1"), "Art. 1º-A")
  }
  def testLabel_art1_2() {
    assertEquals(Fragment2Label.format("art1-27"), "Art. 1º-B")
  }
  def testLabel_art1_27() {
    assertEquals(Fragment2Label.format("art1-27"), "Art. 1º-AA")
  }
  def testLabel_art1_28() {
    assertEquals(Fragment2Label.format("art1-27"), "Art. 1º-AB")
  }
 
  // parágrafo
  def testLabel_art1_par1u() {
    assertEquals(Fragment2Label.format("art1_par1u"), "Parágrafo Único.")
  }
  def testLabel_art1_par1() {
    assertEquals(Fragment2Label.format("art1_par1"), "§ 1º")
  }
  def testLabel_art1_par2() {
    assertEquals(Fragment2Label.format("art1_par2"), "§ 2º")
  }
  def testLabel_art1_par9() {
    assertEquals(Fragment2Label.format("art1_par9"), "§ 9º")
  }
  def testLabel_art1_par10() {
    assertEquals(Fragment2Label.format("art1_par10"), "§ 10º.")
  }
  def testLabel_art1_par12() {
    assertEquals(Fragment2Label.format("art1_par12"), "§ 12º.")
  }


  
      /*
      , "art1-27"
      , "art4_par1u"
      , "art4_par1u_ite7"
      , "art4_par1u_ite7_ali3"
      , "art4_par1u_ite7_ali3_inc2"
      , "art1-1"
      , "art1-2")
*/


}