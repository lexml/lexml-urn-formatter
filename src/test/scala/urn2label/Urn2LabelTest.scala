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

  //incisos
  def testLabel_art3_par2_inc3() {
    assertEquals(Fragment2Label.format("art3_par2_inc3"), "III –")
  }
  def testLabel_art3_par2_inc3_1() {
    assertEquals(Fragment2Label.format("art3_par2_inc3-1"), "III-A –")
  }

  //alíneas
  def testLabel_art3_par2_inc3_ali4() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4"), "d)")
  }
  def testLabel_art3_par2_inc3_ali5_1() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali5-1"), "e-A)")
  }
  def testLabel_art3_par2_inc3_ali5_2() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali5-2"), "e-B)")
  }
  def testLabel_art3_par2_inc3_ali5_26() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali5-2"), "e-Z)")
  }
  def testLabel_art3_par2_inc3_ali5_27() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali5-2"), "e-AA)")
  }
  
  //item
  def testLabel_art3_par2_inc3_ali4_ite8() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4_ite8"), "8.")
  }
  def testLabel_art3_par2_inc3_ali4_ite8_1() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4_ite8-1"), "8-A.")
  }
  def testLabel_art3_par2_inc3_ali4_ite8_2() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4_ite8-2"), "8-B.")
  }
  def testLabel_art3_par2_inc3_ali4_ite8_26() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4_ite8-26"), "8-Z.")
  }
  def testLabel_art3_par2_inc3_ali4_ite8_28() {
    assertEquals(Fragment2Label.format("art3_par2_inc3_ali4_ite8-26"), "8-AB.")
  }
  
  //agrupadores
  def testLabel_tit3_cap3() {
    assertEquals(Fragment2Label.format("tit3_cap3"), "CAPÍTULO III")
  }

  def testLabel_tit3_cap3_1() {
    assertEquals(Fragment2Label.format("tit3_cap3-1"), "CAPÍTULO III-A")
  }
  def testLabel_tit3_cap3_27() {
    assertEquals(Fragment2Label.format("tit3_cap3-27"), "CAPÍTULO III-AA")
  }

  def testLabel_tit3() {
    assertEquals(Fragment2Label.format("tit3"), "TÍTULO III")
  }
  def testLabel_liv1() {
    assertEquals(Fragment2Label.format("liv1"), "LIVRO I")
  }
  
  def testLabel_sec1() {
    assertEquals(Fragment2Label.format("sec1"), "Seção I")
  }
  
  def testLabel_cap2_sec3_sub1() {
    assertEquals(Fragment2Label.format("cap2_sec3_sub1"), "Subseção I")
  }
  
  // Bloco de alteração
  def testLabel_art1_par1u_alt1_art32() {
    assertEquals(Fragment2Label.format("art1_par1u_alt1_art32"), "Art. 32.")
  }
  def testLabel_art3_cpt_alt1_art4_par2() {
    assertEquals(Fragment2Label.format("art3_cpt_alt1_art4_par2"), "§ 2º")
  }
  def testLabel_art3_cpt_alt1_tit3() {
    assertEquals(Fragment2Label.format("art3_cpt_alt1_tit3"), "TÍTULO III")
  }


}