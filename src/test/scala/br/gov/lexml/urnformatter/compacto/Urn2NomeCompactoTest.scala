package br.gov.lexml.urnformatter.compacto

import junit.framework.Assert.assertEquals
import junit.framework.TestCase

class Urn2NomeCompactoTest extends TestCase {

  def testLabel_art1() {
    assertEquals("art. 1º", Urn2NomeCompacto.format("art1"))
  }

  def testLabel_art1_cpt() {
    assertEquals("art. 1º, caput", Urn2NomeCompacto.format("art1_cpt"))
  }

  def testLabel_art1_par1u() {
    assertEquals("art. 1º, parágrafo único", Urn2NomeCompacto.format("art1_par1u"))
  }

  def testLabel_art1_par1u_ali3() {
    assertEquals("art. 1º, parágrafo único, inciso III", Urn2NomeCompacto.format("art1_par1u_inc3"))
  }

  def testLabel_art1_par1u_ali3_inc2() {
    assertEquals("art. 1º, parágrafo único, inciso III, alínea 'b'", Urn2NomeCompacto.format("art1_par1u_inc3_ali2"))
  }

  def testLabel_art2() {
    assertEquals("art. 2º", Urn2NomeCompacto.format("art2"))
  }

  def testLabel_art2_cpt() {
    assertEquals("art. 2º, caput", Urn2NomeCompacto.format("art2_cpt"))
  }

  def testLabel_art2_cpt_inc1() {
    assertEquals("art. 2º, inciso I", Urn2NomeCompacto.format("art2_cpt_inc1"))
  }

  def testLabel_art2_cpt_inc2() {
    assertEquals("art. 2º, inciso II", Urn2NomeCompacto.format("art2_cpt_inc2"))
  }

  def testLabel_art2_par1u() {
    assertEquals("art. 2º, parágrafo único", Urn2NomeCompacto.format("art2_par1u"))
  }

  def testLabel_art3() {
    assertEquals("art. 3º", Urn2NomeCompacto.format("art3"))
  }

  def testLabel_art3_cpt() {
    assertEquals("art. 3º, caput", Urn2NomeCompacto.format("art3_cpt"))
  }

  def testLabel_art3_cpt_inc1() {
    assertEquals("art. 3º, inciso I", Urn2NomeCompacto.format("art3_cpt_inc1"))
  }

  def testLabel_art3_cpt_inc2() {
    assertEquals("art. 3º, inciso II", Urn2NomeCompacto.format("art3_cpt_inc2"))
  }

  def testLabel_art3_cpt_inc3() {
    assertEquals("art. 3º, inciso III", Urn2NomeCompacto.format("art3_cpt_inc3"))
  }

  def testLabel_art3_cpt_inc4() {
    assertEquals("art. 3º, inciso IV", Urn2NomeCompacto.format("art3_cpt_inc4"))
  }

  def testLabel_art3_cpt_inc4_ali1() {
    assertEquals("art. 3º, inciso IV, alínea 'a'", Urn2NomeCompacto.format("art3_cpt_inc4_ali1"))
  }

  def testLabel_art3_cpt_inc4_ali2() {
    assertEquals("art. 3º, inciso IV, alínea 'b'", Urn2NomeCompacto.format("art3_cpt_inc4_ali2"))
  }

  def testLabel_art3_cpt_inc5() {
    assertEquals("art. 3º, inciso V", Urn2NomeCompacto.format("art3_cpt_inc5"))
  }

  def testLabel_art3_cpt_inc6() {
    assertEquals("art. 3º, inciso VI", Urn2NomeCompacto.format("art3_cpt_inc6"))
  }

  def testLabel_art3_cpt_inc7() {
    assertEquals("art. 3º, inciso VII", Urn2NomeCompacto.format("art3_cpt_inc7"))
  }

  def testLabel_art4() {
    assertEquals("art. 4º", Urn2NomeCompacto.format("art4"))
  }

  def testLabel_art4_cpt() {
    assertEquals("art. 4º, caput", Urn2NomeCompacto.format("art4_cpt"))
  }

  def testLabel_art4_par1() {
    assertEquals("art. 4º, § 1º", Urn2NomeCompacto.format("art4_par1"))
  }

  def testLabel_art4_par2() {
    assertEquals("art. 4º, § 2º", Urn2NomeCompacto.format("art4_par2"))
  }

  def testLabel_art4_par3() {
    assertEquals("art. 4º, § 3º", Urn2NomeCompacto.format("art4_par3"))
  }

  def testLabel_art4_par4() {
    assertEquals("art. 4º, § 4º", Urn2NomeCompacto.format("art4_par4"))
  }

  def testLabel_art4_par5() {
    assertEquals("art. 4º, § 5º", Urn2NomeCompacto.format("art4_par5"))
  }

  def testLabel_art4_par6() {
    assertEquals("art. 4º, § 6º", Urn2NomeCompacto.format("art4_par6"))
  }

  def testLabel_art5() {
    assertEquals("art. 5º", Urn2NomeCompacto.format("art5"))
  }

  def testLabel_art5_cpt() {
    assertEquals("art. 5º, caput", Urn2NomeCompacto.format("art5_cpt"))
  }

  def testLabel_art5_par1() {
    assertEquals("art. 5º, § 1º", Urn2NomeCompacto.format("art5_par1"))
  }

  def testLabel_art5_par2() {
    assertEquals("art. 5º, § 2º", Urn2NomeCompacto.format("art5_par2"))
  }

  def testLabel_art6() {
    assertEquals("art. 6º", Urn2NomeCompacto.format("art6"))
  }

  def testLabel_art6_cpt() {
    assertEquals("art. 6º, caput", Urn2NomeCompacto.format("art6_cpt"))
  }

  def testLabel_art7() {
    assertEquals("art. 7º", Urn2NomeCompacto.format("art7"))
  }

  def testLabel_art7_cpt() {
    assertEquals("art. 7º, caput", Urn2NomeCompacto.format("art7_cpt"))
  }

  def testLabel_art7_par1() {
    assertEquals("art. 7º, § 1º", Urn2NomeCompacto.format("art7_par1"))
  }

  def testLabel_art7_par2() {
    assertEquals("art. 7º, § 2º", Urn2NomeCompacto.format("art7_par2"))
  }

  def testLabel_art8() {
    assertEquals("art. 8º", Urn2NomeCompacto.format("art8"))
  }

  def testLabel_art8_cpt() {
    assertEquals("art. 8º, caput", Urn2NomeCompacto.format("art8_cpt"))
  }

  def testLabel_art8_cpt_inc1() {
    assertEquals("art. 8º, inciso I", Urn2NomeCompacto.format("art8_cpt_inc1"))
  }

  def testLabel_art8_cpt_inc2() {
    assertEquals("art. 8º, inciso II", Urn2NomeCompacto.format("art8_cpt_inc2"))
  }

  def testLabel_art8_cpt_inc3() {
    assertEquals("art. 8º, inciso III", Urn2NomeCompacto.format("art8_cpt_inc3"))
  }

  def testLabel_art9() {
    assertEquals("art. 9º", Urn2NomeCompacto.format("art9"))
  }

  def testLabel_art9_cpt() {
    assertEquals("art. 9º, caput", Urn2NomeCompacto.format("art9_cpt"))
  }

  def testLabel_art9_cpt_inc1() {
    assertEquals("art. 9º, inciso I", Urn2NomeCompacto.format("art9_cpt_inc1"))
  }

  def testLabel_art9_cpt_inc2() {
    assertEquals("art. 9º, inciso II", Urn2NomeCompacto.format("art9_cpt_inc2"))
  }

  def testLabel_art9_cpt_inc3() {
    assertEquals("art. 9º, inciso III", Urn2NomeCompacto.format("art9_cpt_inc3"))
  }

  def testLabel_art9_cpt_inc4() {
    assertEquals("art. 9º, inciso IV", Urn2NomeCompacto.format("art9_cpt_inc4"))
  }

  def testLabel_art9_cpt_inc5() {
    assertEquals("art. 9º, inciso V", Urn2NomeCompacto.format("art9_cpt_inc5"))
  }

  def testLabel_art9_cpt_inc6() {
    assertEquals("art. 9º, inciso VI", Urn2NomeCompacto.format("art9_cpt_inc6"))
  }

  def testLabel_art9_par1u() {
    assertEquals("art. 9º, parágrafo único", Urn2NomeCompacto.format("art9_par1u"))
  }

  def testLabel_art10() {
    assertEquals("art. 10", Urn2NomeCompacto.format("art10"))
  }

  def testLabel_art10_cpt() {
    assertEquals("art. 10, caput", Urn2NomeCompacto.format("art10_cpt"))
  }

  def testLabel_art11() {
    assertEquals("art. 11", Urn2NomeCompacto.format("art11"))
  }

  def testLabel_art11_cpt() {
    assertEquals("art. 11, caput", Urn2NomeCompacto.format("art11_cpt"))
  }

  def testLabel_art12() {
    assertEquals("art. 12", Urn2NomeCompacto.format("art12"))
  }

  def testLabel_art12_cpt() {
    assertEquals("art. 12, caput", Urn2NomeCompacto.format("art12_cpt"))
  }

  def testLabel_art12_cpt_inc1() {
    assertEquals("art. 12, inciso I", Urn2NomeCompacto.format("art12_cpt_inc1"))
  }

  def testLabel_art12_cpt_inc2() {
    assertEquals("art. 12, inciso II", Urn2NomeCompacto.format("art12_cpt_inc2"))
  }

  def testLabel_art12_cpt_inc3() {
    assertEquals("art. 12, inciso III", Urn2NomeCompacto.format("art12_cpt_inc3"))
  }

  def testLabel_art12_par1u() {
    assertEquals("art. 12, parágrafo único", Urn2NomeCompacto.format("art12_par1u"))
  }

  def testLabel_art13() {
    assertEquals("art. 13", Urn2NomeCompacto.format("art13"))
  }

  def testLabel_art13_cpt() {
    assertEquals("art. 13, caput", Urn2NomeCompacto.format("art13_cpt"))
  }

  def testLabel_art13_par1() {
    assertEquals("art. 13, § 1º", Urn2NomeCompacto.format("art13_par1"))
  }

  def testLabel_art13_par2() {
    assertEquals("art. 13, § 2º", Urn2NomeCompacto.format("art13_par2"))
  }

  def testLabel_art14() {
    assertEquals("art. 14", Urn2NomeCompacto.format("art14"))
  }

  def testLabel_art14_cpt() {
    assertEquals("art. 14, caput", Urn2NomeCompacto.format("art14_cpt"))
  }

  def testLabel_art14_cpt_inc1() {
    assertEquals("art. 14, inciso I", Urn2NomeCompacto.format("art14_cpt_inc1"))
  }

  def testLabel_art14_cpt_inc2() {
    assertEquals("art. 14, inciso II", Urn2NomeCompacto.format("art14_cpt_inc2"))
  }

  def testLabel_art14_cpt_inc2_ali1() {
    assertEquals("art. 14, inciso II, alínea 'a'", Urn2NomeCompacto.format("art14_cpt_inc2_ali1"))
  }

  def testLabel_art14_cpt_inc2_ali2() {
    assertEquals("art. 14, inciso II, alínea 'b'", Urn2NomeCompacto.format("art14_cpt_inc2_ali2"))
  }

  def testLabel_art14_cpt_inc3() {
    assertEquals("art. 14, inciso III", Urn2NomeCompacto.format("art14_cpt_inc3"))
  }

  def testLabel_art14_cpt_inc4() {
    assertEquals("art. 14, inciso IV", Urn2NomeCompacto.format("art14_cpt_inc4"))
  }

  def testLabel_art14_cpt_inc5() {
    assertEquals("art. 14, inciso V", Urn2NomeCompacto.format("art14_cpt_inc5"))
  }

  def testLabel_art14_cpt_inc6() {
    assertEquals("art. 14, inciso VI", Urn2NomeCompacto.format("art14_cpt_inc6"))
  }

  def testLabel_art14_cpt_inc7() {
    assertEquals("art. 14, inciso VII", Urn2NomeCompacto.format("art14_cpt_inc7"))
  }

  def testLabel_art14_cpt_inc8() {
    assertEquals("art. 14, inciso VIII", Urn2NomeCompacto.format("art14_cpt_inc8"))
  }

  def testLabel_art14_cpt_inc9() {
    assertEquals("art. 14, inciso IX", Urn2NomeCompacto.format("art14_cpt_inc9"))
  }

  def testLabel_art14_cpt_inc10() {
    assertEquals("art. 14, inciso X", Urn2NomeCompacto.format("art14_cpt_inc10"))
  }

  def testLabel_art14_cpt_inc10_ali1() {
    assertEquals("art. 14, inciso X, alínea 'a'", Urn2NomeCompacto.format("art14_cpt_inc10_ali1"))
  }

  def testLabel_art14_cpt_inc10_ali2() {
    assertEquals("art. 14, inciso X, alínea 'b'", Urn2NomeCompacto.format("art14_cpt_inc10_ali2"))
  }

  def testLabel_art14_cpt_inc10_ali3() {
    assertEquals("art. 14, inciso X, alínea 'c'", Urn2NomeCompacto.format("art14_cpt_inc10_ali3"))
  }

  def testLabel_art14_cpt_inc11() {
    assertEquals("art. 14, inciso XI", Urn2NomeCompacto.format("art14_cpt_inc11"))
  }

  def testLabel_art14_cpt_inc12() {
    assertEquals("art. 14, inciso XII", Urn2NomeCompacto.format("art14_cpt_inc12"))
  }

  def testLabel_art14_cpt_inc12_ali1() {
    assertEquals("art. 14, inciso XII, alínea 'a'", Urn2NomeCompacto.format("art14_cpt_inc12_ali1"))
  }

  def testLabel_art14_cpt_inc12_ali2() {
    assertEquals("art. 14, inciso XII, alínea 'b'", Urn2NomeCompacto.format("art14_cpt_inc12_ali2"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite1() {
    assertEquals("art. 14, inciso XII, alínea 'b', 1", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite1"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite2() {
    assertEquals("art. 14, inciso XII, alínea 'b', 2", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite2"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite3() {
    assertEquals("art. 14, inciso XII, alínea 'b', 3", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite3"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite4() {
    assertEquals("art. 14, inciso XII, alínea 'b', 4", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite4"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite5() {
    assertEquals("art. 14, inciso XII, alínea 'b', 5", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite5"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite6() {
    assertEquals("art. 14, inciso XII, alínea 'b', 6", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite6"))
  }

  def testLabel_art14_cpt_inc12_ali2_ite7() {
    assertEquals("art. 14, inciso XII, alínea 'b', 7", Urn2NomeCompacto.format("art14_cpt_inc12_ali2_ite7"))
  }

  def testLabel_art14_cpt_inc12_ali3() {
    assertEquals("art. 14, inciso XII, alínea 'c'", Urn2NomeCompacto.format("art14_cpt_inc12_ali3"))
  }

  def testLabel_art14_cpt_inc12_ali4() {
    assertEquals("art. 14, inciso XII, alínea 'd'", Urn2NomeCompacto.format("art14_cpt_inc12_ali4"))
  }

  def testLabel_art14_cpt_inc12_ali5() {
    assertEquals("art. 14, inciso XII, alínea 'e'", Urn2NomeCompacto.format("art14_cpt_inc12_ali5"))
  }

  def testLabel_art14_cpt_inc13() {
    assertEquals("art. 14, inciso XIII", Urn2NomeCompacto.format("art14_cpt_inc13"))
  }

  def testLabel_art14_cpt_inc14() {
    assertEquals("art. 14, inciso XIV", Urn2NomeCompacto.format("art14_cpt_inc14"))
  }

  def testLabel_art14_par1() {
    assertEquals("art. 14, § 1º", Urn2NomeCompacto.format("art14_par1"))
  }

  def testLabel_art14_par2() {
    assertEquals("art. 14, § 2º", Urn2NomeCompacto.format("art14_par2"))
  }

  def testLabel_art14_par3() {
    assertEquals("art. 14, § 3º", Urn2NomeCompacto.format("art14_par3"))
  }

  def testLabel_art14_par4() {
    assertEquals("art. 14, § 4º", Urn2NomeCompacto.format("art14_par4"))
  }

  def testLabel_art14_par5() {
    assertEquals("art. 14, § 5º", Urn2NomeCompacto.format("art14_par5"))
  }

  def testLabel_art14_par6() {
    assertEquals("art. 14, § 6º", Urn2NomeCompacto.format("art14_par6"))
  }

  def testLabel_art14_par7() {
    assertEquals("art. 14, § 7º", Urn2NomeCompacto.format("art14_par7"))
  }

  def testLabel_art14_par8() {
    assertEquals("art. 14, § 8º", Urn2NomeCompacto.format("art14_par8"))
  }

  def testLabel_art15() {
    assertEquals("art. 15", Urn2NomeCompacto.format("art15"))
  }

  def testLabel_art15_cpt() {
    assertEquals("art. 15, caput", Urn2NomeCompacto.format("art15_cpt"))
  }

  def testLabel_art16() {
    assertEquals("art. 16", Urn2NomeCompacto.format("art16"))
  }

  def testLabel_art16_cpt() {
    assertEquals("art. 16, caput", Urn2NomeCompacto.format("art16_cpt"))
  }

  def testLabel_art17() {
    assertEquals("art. 17", Urn2NomeCompacto.format("art17"))
  }

  def testLabel_art17_cpt() {
    assertEquals("art. 17, caput", Urn2NomeCompacto.format("art17_cpt"))
  }

  def testLabel_art17_par1() {
    assertEquals("art. 17, § 1º", Urn2NomeCompacto.format("art17_par1"))
  }

  def testLabel_art17_par2() {
    assertEquals("art. 17, § 2º", Urn2NomeCompacto.format("art17_par2"))
  }

  def testLabel_art18() {
    assertEquals("art. 18", Urn2NomeCompacto.format("art18"))
  }

  def testLabel_art18_cpt() {
    assertEquals("art. 18, caput", Urn2NomeCompacto.format("art18_cpt"))
  }

  def testLabel_art18_cpt_inc1() {
    assertEquals("art. 18, inciso I", Urn2NomeCompacto.format("art18_cpt_inc1"))
  }

  def testLabel_art18_cpt_inc1_ali1() {
    assertEquals("art. 18, inciso I, alínea 'a'", Urn2NomeCompacto.format("art18_cpt_inc1_ali1"))
  }

  def testLabel_art18_cpt_inc1_ali2() {
    assertEquals("art. 18, inciso I, alínea 'b'", Urn2NomeCompacto.format("art18_cpt_inc1_ali2"))
  }

  def testLabel_art18_cpt_inc1_ali3() {
    assertEquals("art. 18, inciso I, alínea 'c'", Urn2NomeCompacto.format("art18_cpt_inc1_ali3"))
  }

  def testLabel_art18_cpt_inc1_ali4() {
    assertEquals("art. 18, inciso I, alínea 'd'", Urn2NomeCompacto.format("art18_cpt_inc1_ali4"))
  }

  def testLabel_art18_cpt_inc1_ali5() {
    assertEquals("art. 18, inciso I, alínea 'e'", Urn2NomeCompacto.format("art18_cpt_inc1_ali5"))
  }

  def testLabel_art18_cpt_inc1_ali6() {
    assertEquals("art. 18, inciso I, alínea 'f'", Urn2NomeCompacto.format("art18_cpt_inc1_ali6"))
  }

  def testLabel_art18_cpt_inc1_ali7() {
    assertEquals("art. 18, inciso I, alínea 'g'", Urn2NomeCompacto.format("art18_cpt_inc1_ali7"))
  }

  def testLabel_art18_cpt_inc1_ali8() {
    assertEquals("art. 18, inciso I, alínea 'h'", Urn2NomeCompacto.format("art18_cpt_inc1_ali8"))
  }

  def testLabel_art18_cpt_inc2() {
    assertEquals("art. 18, inciso II", Urn2NomeCompacto.format("art18_cpt_inc2"))
  }

  def testLabel_art18_cpt_inc2_ali1() {
    assertEquals("art. 18, inciso II, alínea 'a'", Urn2NomeCompacto.format("art18_cpt_inc2_ali1"))
  }

  def testLabel_art18_cpt_inc2_ali2() {
    assertEquals("art. 18, inciso II, alínea 'b'", Urn2NomeCompacto.format("art18_cpt_inc2_ali2"))
  }

  def testLabel_art18_par1u() {
    assertEquals("art. 18, parágrafo único", Urn2NomeCompacto.format("art18_par1u"))
  }

  def testLabel_art19() {
    assertEquals("art. 19", Urn2NomeCompacto.format("art19"))
  }

  def testLabel_art19_cpt() {
    assertEquals("art. 19, caput", Urn2NomeCompacto.format("art19_cpt"))
  }

  def testLabel_art19_cpt_inc1() {
    assertEquals("art. 19, inciso I", Urn2NomeCompacto.format("art19_cpt_inc1"))
  }

  def testLabel_art19_cpt_inc2() {
    assertEquals("art. 19, inciso II", Urn2NomeCompacto.format("art19_cpt_inc2"))
  }

  def testLabel_art20() {
    assertEquals("art. 20", Urn2NomeCompacto.format("art20"))
  }

  def testLabel_art20_cpt() {
    assertEquals("art. 20, caput", Urn2NomeCompacto.format("art20_cpt"))
  }

  def testLabel_art21() {
    assertEquals("art. 21", Urn2NomeCompacto.format("art21"))
  }

  def testLabel_art21_cpt() {
    assertEquals("art. 21, caput", Urn2NomeCompacto.format("art21_cpt"))
  }

  def testLabel_art22() {
    assertEquals("art. 22", Urn2NomeCompacto.format("art22"))
  }

  def testLabel_art22_cpt() {
    assertEquals("art. 22, caput", Urn2NomeCompacto.format("art22_cpt"))
  }

  def testLabel_art22_cpt_inc1() {
    assertEquals("art. 22, inciso I", Urn2NomeCompacto.format("art22_cpt_inc1"))
  }

  def testLabel_art22_cpt_inc2() {
    assertEquals("art. 22, inciso II", Urn2NomeCompacto.format("art22_cpt_inc2"))
  }

  def testLabel_art22_cpt_inc3() {
    assertEquals("art. 22, inciso III", Urn2NomeCompacto.format("art22_cpt_inc3"))
  }

  def testLabel_art22_cpt_inc4() {
    assertEquals("art. 22, inciso IV", Urn2NomeCompacto.format("art22_cpt_inc4"))
  }

  def testLabel_art22_cpt_inc5() {
    assertEquals("art. 22, inciso V", Urn2NomeCompacto.format("art22_cpt_inc5"))
  }

  def testLabel_art23() {
    assertEquals("art. 23", Urn2NomeCompacto.format("art23"))
  }

  def testLabel_art23_cpt() {
    assertEquals("art. 23, caput", Urn2NomeCompacto.format("art23_cpt"))
  }

  def testLabel_art23_cpt_inc1() {
    assertEquals("art. 23, inciso I", Urn2NomeCompacto.format("art23_cpt_inc1"))
  }

  def testLabel_art23_cpt_inc2() {
    assertEquals("art. 23, inciso II", Urn2NomeCompacto.format("art23_cpt_inc2"))
  }

  def testLabel_art24() {
    assertEquals("art. 24", Urn2NomeCompacto.format("art24"))
  }

  def testLabel_art24_cpt() {
    assertEquals("art. 24, caput", Urn2NomeCompacto.format("art24_cpt"))
  }

  def testLabel_art24_cpt_inc1() {
    assertEquals("art. 24, inciso I", Urn2NomeCompacto.format("art24_cpt_inc1"))
  }

  def testLabel_art24_cpt_inc2() {
    assertEquals("art. 24, inciso II", Urn2NomeCompacto.format("art24_cpt_inc2"))
  }

  def testLabel_art24_cpt_inc2_ali1() {
    assertEquals("art. 24, inciso II, alínea 'a'", Urn2NomeCompacto.format("art24_cpt_inc2_ali1"))
  }

  def testLabel_art24_cpt_inc2_ali2() {
    assertEquals("art. 24, inciso II, alínea 'b'", Urn2NomeCompacto.format("art24_cpt_inc2_ali2"))
  }

  def testLabel_art24_cpt_inc3() {
    assertEquals("art. 24, inciso III", Urn2NomeCompacto.format("art24_cpt_inc3"))
  }

  def testLabel_art24_cpt_inc4() {
    assertEquals("art. 24, inciso IV", Urn2NomeCompacto.format("art24_cpt_inc4"))
  }

  def testLabel_art24_cpt_inc5() {
    assertEquals("art. 24, inciso V", Urn2NomeCompacto.format("art24_cpt_inc5"))
  }

  def testLabel_art24_cpt_inc5_ali1() {
    assertEquals("art. 24, inciso V, alínea 'a'", Urn2NomeCompacto.format("art24_cpt_inc5_ali1"))
  }

  def testLabel_art24_cpt_inc5_ali2() {
    assertEquals("art. 24, inciso V, alínea 'b'", Urn2NomeCompacto.format("art24_cpt_inc5_ali2"))
  }

  def testLabel_art24_cpt_inc6() {
    assertEquals("art. 24, inciso VI", Urn2NomeCompacto.format("art24_cpt_inc6"))
  }

  def testLabel_art25() {
    assertEquals("art. 25", Urn2NomeCompacto.format("art25"))
  }

  def testLabel_art25_cpt() {
    assertEquals("art. 25, caput", Urn2NomeCompacto.format("art25_cpt"))
  }

  def testLabel_art26() {
    assertEquals("art. 26", Urn2NomeCompacto.format("art26"))
  }

  def testLabel_art26_cpt() {
    assertEquals("art. 26, caput", Urn2NomeCompacto.format("art26_cpt"))
  }

  def testLabel_art27() {
    assertEquals("art. 27", Urn2NomeCompacto.format("art27"))
  }

  def testLabel_art27_cpt() {
    assertEquals("art. 27, caput", Urn2NomeCompacto.format("art27_cpt"))
  }

  def testLabel_art27_par1u() {
    assertEquals("art. 27, parágrafo único", Urn2NomeCompacto.format("art27_par1u"))
  }

  def testLabel_art28() {
    assertEquals("art. 28", Urn2NomeCompacto.format("art28"))
  }

  def testLabel_art28_cpt() {
    assertEquals("art. 28, caput", Urn2NomeCompacto.format("art28_cpt"))
  }

  def testLabel_art28_cpt_inc1() {
    assertEquals("art. 28, inciso I", Urn2NomeCompacto.format("art28_cpt_inc1"))
  }

  def testLabel_art28_cpt_inc2() {
    assertEquals("art. 28, inciso II", Urn2NomeCompacto.format("art28_cpt_inc2"))
  }

  def testLabel_art28_cpt_inc3() {
    assertEquals("art. 28, inciso III", Urn2NomeCompacto.format("art28_cpt_inc3"))
  }

  def testLabel_art29() {
    assertEquals("art. 29", Urn2NomeCompacto.format("art29"))
  }

  def testLabel_art29_cpt() {
    assertEquals("art. 29, caput", Urn2NomeCompacto.format("art29_cpt"))
  }

  def testLabel_art29_par1u() {
    assertEquals("art. 29, parágrafo único", Urn2NomeCompacto.format("art29_par1u"))
  }

  def testLabel_art30() {
    assertEquals("art. 30", Urn2NomeCompacto.format("art30"))
  }

  def testLabel_art30_cpt() {
    assertEquals("art. 30, caput", Urn2NomeCompacto.format("art30_cpt"))
  }

  def testLabel_art30_cpt_inc1() {
    assertEquals("art. 30, inciso I", Urn2NomeCompacto.format("art30_cpt_inc1"))
  }

  def testLabel_art30_cpt_inc2() {
    assertEquals("art. 30, inciso II", Urn2NomeCompacto.format("art30_cpt_inc2"))
  }

  def testLabel_art31() {
    assertEquals("art. 31", Urn2NomeCompacto.format("art31"))
  }

  def testLabel_art31_cpt() {
    assertEquals("art. 31, caput", Urn2NomeCompacto.format("art31_cpt"))
  }

  def testLabel_art31_par1u() {
    assertEquals("art. 31, parágrafo único", Urn2NomeCompacto.format("art31_par1u"))
  }

  def testLabel_art32() {
    assertEquals("art. 32", Urn2NomeCompacto.format("art32"))
  }

  def testLabel_art32_cpt() {
    assertEquals("art. 32, caput", Urn2NomeCompacto.format("art32_cpt"))
  }

  def testLabel_art32_cpt_inc1() {
    assertEquals("art. 32, inciso I", Urn2NomeCompacto.format("art32_cpt_inc1"))
  }

  def testLabel_art32_cpt_inc2() {
    assertEquals("art. 32, inciso II", Urn2NomeCompacto.format("art32_cpt_inc2"))
  }

  def testLabel_art32_cpt_inc3() {
    assertEquals("art. 32, inciso III", Urn2NomeCompacto.format("art32_cpt_inc3"))
  }

  def testLabel_art32_cpt_inc4() {
    assertEquals("art. 32, inciso IV", Urn2NomeCompacto.format("art32_cpt_inc4"))
  }

  def testLabel_art32_cpt_inc5() {
    assertEquals("art. 32, inciso V", Urn2NomeCompacto.format("art32_cpt_inc5"))
  }

  def testLabel_art32_cpt_inc6() {
    assertEquals("art. 32, inciso VI", Urn2NomeCompacto.format("art32_cpt_inc6"))
  }

  def testLabel_art32_par1() {
    assertEquals("art. 32, § 1º", Urn2NomeCompacto.format("art32_par1"))
  }

  def testLabel_art32_par2() {
    assertEquals("art. 32, § 2º", Urn2NomeCompacto.format("art32_par2"))
  }

  def testLabel_art32_par3() {
    assertEquals("art. 32, § 3º", Urn2NomeCompacto.format("art32_par3"))
  }

  def testLabel_art32_par4() {
    assertEquals("art. 32, § 4º", Urn2NomeCompacto.format("art32_par4"))
  }

  def testLabel_art32_par4_inc1() {
    assertEquals("art. 32, § 4º, inciso I", Urn2NomeCompacto.format("art32_par4_inc1"))
  }

  def testLabel_art32_par4_inc2() {
    assertEquals("art. 32, § 4º, inciso II", Urn2NomeCompacto.format("art32_par4_inc2"))
  }

  def testLabel_art32_par5() {
    assertEquals("art. 32, § 5º", Urn2NomeCompacto.format("art32_par5"))
  }

  def testLabel_art32_par5_inc1() {
    assertEquals("art. 32, § 5º, inciso I", Urn2NomeCompacto.format("art32_par5_inc1"))
  }

  def testLabel_art32_par5_inc2() {
    assertEquals("art. 32, § 5º, inciso II", Urn2NomeCompacto.format("art32_par5_inc2"))
  }

  def testLabel_art33() {
    assertEquals("art. 33", Urn2NomeCompacto.format("art33"))
  }

  def testLabel_art33_cpt() {
    assertEquals("art. 33, caput", Urn2NomeCompacto.format("art33_cpt"))
  }

  def testLabel_art33_par1() {
    assertEquals("art. 33, § 1º", Urn2NomeCompacto.format("art33_par1"))
  }

  def testLabel_art33_par2() {
    assertEquals("art. 33, § 2º", Urn2NomeCompacto.format("art33_par2"))
  }

  def testLabel_art33_par3() {
    assertEquals("art. 33, § 3º", Urn2NomeCompacto.format("art33_par3"))
  }

  def testLabel_art34() {
    assertEquals("art. 34", Urn2NomeCompacto.format("art34"))
  }

  def testLabel_art34_cpt() {
    assertEquals("art. 34, caput", Urn2NomeCompacto.format("art34_cpt"))
  }

  def testLabel_art35() {
    assertEquals("art. 35", Urn2NomeCompacto.format("art35"))
  }

  def testLabel_art35_cpt() {
    assertEquals("art. 35, caput", Urn2NomeCompacto.format("art35_cpt"))
  }

  def testLabel_art36() {
    assertEquals("art. 36", Urn2NomeCompacto.format("art36"))
  }

  def testLabel_art36_cpt() {
    assertEquals("art. 36, caput", Urn2NomeCompacto.format("art36_cpt"))
  }

  def testLabel_art37() {
    assertEquals("art. 37", Urn2NomeCompacto.format("art37"))
  }

  def testLabel_art37_cpt() {
    assertEquals("art. 37, caput", Urn2NomeCompacto.format("art37_cpt"))
  }

  def testLabel_tit1() = {
    assertEquals("Título I", Urn2NomeCompacto.format("tit1"))
  }

  def testLabel_tit1_cap1() = {
    assertEquals("Capítulo I do Título I", Urn2NomeCompacto.format("tit1_cap1"))
  }

  def testLabel_tit1_cap1_art2() = {
    assertEquals("art. 2º", Urn2NomeCompacto.format("tit1_cap1_art2"))
  }

  def testLabel_tit1_cap1_art10_cpt() = {
    assertEquals("art. 19, caput", Urn2NomeCompacto.format("tit1_cap1_art19_cpt"))
  }

  def testLabel_tit1_cap1_sec2() = {
    assertEquals("Seção II do Capítulo I do Título I", Urn2NomeCompacto.format("tit1_cap1_sec2"))
  }

  def testLabel_tit1_cap1_sec2_sub4() = {
    assertEquals("Subseção IV da Seção II do Capítulo I do Título I", Urn2NomeCompacto.format("tit1_cap1_sec2_sub4"))
  }

  def testLabel_liv3_tit1_cap1_sec2_sub4() = {
    assertEquals("Subseção IV da Seção II do Capítulo I do Título I do Livro III", Urn2NomeCompacto.format("liv3_tit1_cap1_sec2_sub4"))
  }

  def testLabel_anx1_liv3() = {
    assertEquals("Livro III do Anexo I", Urn2NomeCompacto.format("anx1_liv3"))
  }

  def testLabel_raiz_cpp_art1() = {
    assertEquals("art. 1º", Urn2NomeCompacto.format("lex_cpp_art1"))
  }

  def testLabel_raiz_anx1_art1() = {
    assertEquals("art. 1º do Anexo I", Urn2NomeCompacto.format("lex_anx1_art1"))
  }

  def testLabel_raiz_anx1_tit1() = {
    assertEquals("Título I do Anexo I", Urn2NomeCompacto.format("lex_anx1_tit1"))
  }

  def testLabel_anx1_tit1_art1() = {
    assertEquals("art. 1º do Anexo I", Urn2NomeCompacto.format("anx1_tit1_art1"))
  }

  def testLabel_raiz_cpp_anx1() = {
    assertEquals("Anexo I", Urn2NomeCompacto.format("lex_cpp_anx1"))
  }

  def testLabel_tit1_sec1_3() {
    assertEquals("Seções I, II e III do Título I", Urn2NomeCompacto.format(List("tit1_sec1", "tit1_sec2", "tit1_sec3")))
  }

  def testLabel_tit1_sec1_art1_par1_3() {
    assertEquals("art. 1º, §§ 1º, 2º e 3º", Urn2NomeCompacto.format(List("tit1_sec1_art1_par1", "tit1_sec1_art1_par2", "tit1_sec1_art1_par3")))
  }

  def testLabel_tit1_sec1_art1_par1_inc1_3() {
    assertEquals("art. 1º, § 1º, incisos I, II e III", Urn2NomeCompacto.format(List("tit1_sec1_art1_par1_inc1", "tit1_sec1_art1_par1_inc2", "tit1_sec1_art1_par1_inc3")))
  }

  def testLabel_tit1_sec1_art1_par1_inc1_ali1_ite1_3() {
    assertEquals("art. 1º, § 1º, inciso I, alínea 'a', 1, 2 e 3", Urn2NomeCompacto.format(List("tit1_sec1_art1_par1_inc1_ali1_ite1", "tit1_sec1_art1_par1_inc1_ali1_ite2", "tit1_sec1_art1_par1_inc1_ali1_ite3")))
  }

  def testLabel_tit1_sec1_2_sec3_art1_2_art4() {
    assertEquals("Seções I, II e III do Título I e arts. 1º, 2º e 4º", Urn2NomeCompacto.format(List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4")))
  }

  def testLabel_tit1_sec1_sec2_art1() {
    assertEquals("Seções I e II do Título I e art. 1º", Urn2NomeCompacto.format(List("tit1_sec1", "tit1_sec2", "tit1_sec2_art1")))
  }

  def testLabel_tit1_sec1_2_sec3_art1_2_art4_art6_10() {
    assertEquals("Seções I, II e III do Título I e arts. 1º, 2º, 4º, 6º, 7º, 8º, 9º e 10", Urn2NomeCompacto.format(List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4", "tit1_sec3_art6", "tit1_sec3_art7", "tit1_sec3_art8", "tit1_sec3_art9", "tit1_sec3_art10")))
  }

  def testLabel_tit1_sec1_2_sec3_art1_2_art4_art6_10_tit2_sec1_5() {
    assertEquals("Seções I, II e III do Título I e arts. 1º, 2º, 4º, 6º, 7º, 8º, 9º e 10 e Seções I, II, III, IV e V do Título II", Urn2NomeCompacto.format(List("tit1_sec1", "tit1_sec2", "tit1_sec3", "tit1_sec3_art1", "tit1_sec3_art2", "tit1_sec3_art4", "tit1_sec3_art6", "tit1_sec3_art7", "tit1_sec3_art8", "tit1_sec3_art9", "tit1_sec3_art10", "tit2_sec1", "tit2_sec2", "tit2_sec3", "tit2_sec4", "tit2_sec5")))
  }

  def testLabel_art9_inc1_4() {
    assertEquals("art. 9º, incisos I, II, III e IV", Urn2NomeCompacto.format(List("art9_inc1", "art9_inc2", "art9_inc3", "art9_inc4")))
  }

  def test_art56_incI_e_III_e_V() {
    assertEquals("art. 56, incisos I, III e V", Urn2NomeCompacto.format(List("art56_cpt_inc1", "art56_cpt_inc3", "art56_cpt_inc5")));
  }

  def test_art56_incI_a_III_e_V() {
    assertEquals("art. 56, incisos I, II, III e V", Urn2NomeCompacto.format(List("art56_cpt_inc1", "art56_cpt_inc2", "art56_cpt_inc3", "art56_cpt_inc5")));
  }

  def test_art56_incII_e_IV_e_VII_a_XI() {
    assertEquals("art. 56, incisos II, IV, VIII, IX, X e XI", Urn2NomeCompacto.format(List("art56_cpt_inc2", "art56_cpt_inc4", "art56_cpt_inc8", "art56_cpt_inc9", "art56_cpt_inc10", "art56_cpt_inc11")));
  }

  def test_art56_incIaliA() {
    assertEquals("art. 56, inciso I, alínea 'a'", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1")));
  }

  def test_art56_incIaliA_e_B() {
    assertEquals("art. 56, inciso I, alíneas 'a' e 'b'", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1", "art56_cpt_inc1_ali2")));
  }

  def test_art56_incIaliA_a_C() {
    assertEquals("art. 56, inciso I, alíneas 'a', 'b' e 'c'", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1", "art56_cpt_inc1_ali2", "art56_cpt_inc1_ali3")));
  }

  def test_art56_incIaliA_a_C_e_E() {
    assertEquals("art. 56, inciso I, alíneas 'a', 'b', 'c' e 'e'", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1", "art56_cpt_inc1_ali2", "art56_cpt_inc1_ali3", "art56_cpt_inc1_ali5")));
  }

  def test_art56_incIaliAite1() {
    assertEquals("art. 56, inciso I, alínea 'a', 1", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1_ite1")));
  }

  def test_art56_incIaliAite1_e_2() {
    assertEquals("art. 56, inciso I, alínea 'a', 1 e 2", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1_ite1", "art56_cpt_inc1_ali1_ite2")));
  }

  def test_art56_incIaliAite1_a_3() {
    assertEquals("art. 56, inciso I, alínea 'a', 1, 2 e 3", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1_ite1", "art56_cpt_inc1_ali1_ite2", "art56_cpt_inc1_ali1_ite3")));
  }

  def test_art56_incIaliAite1_a_3_e_5() {
    assertEquals("art. 56, inciso I, alínea 'a', 1, 2, 3 e 5", Urn2NomeCompacto.format(List("art56_cpt_inc1_ali1_ite1", "art56_cpt_inc1_ali1_ite2", "art56_cpt_inc1_ali1_ite3", "art56_cpt_inc1_ali1_ite5")));
  }

  def testLabel_anx1_art25_27_anx1_art30() {
    assertEquals("arts. 25, 26, 27 e 30 do Anexo I", Urn2NomeCompacto.format(List("anx1_art25", "anx1_art26", "anx1_art27", "anx1_art30")))
  }

  def testLabel_anx2_tit1_sec1_anx1_art25_27_anx1_art30_anx3_tit5() {
    assertEquals("Seção I do Título I do Anexo II e arts. 25, 26, 27 e 30 do Anexo I e Título V do Anexo III", Urn2NomeCompacto.format(List("anx2_tit1_sec1", "anx1_art25", "anx1_art26", "anx1_art27", "anx1_art30", "anx3_tit5")))
  }

  def testLabel_empty_list() {
    assertEquals("", Urn2NomeCompacto.format(List()))
  }

  def testLabel_anx1_tit1_sec1_2() {
    assertEquals("Seções I e II do Título I do Anexo I", Urn2NomeCompacto.format(List("anx1_tit1_sec1", "anx1_tit1_sec2")))
  }

  def testLabel_sec10_11() {
    assertEquals("Seções X e XI", Urn2NomeCompacto.format(List("sec10", "sec11")))
  }

  def testLabel_sec9_12() {
    assertEquals("Seções IX e XII", Urn2NomeCompacto.format(List("sec9", "sec12")))
  }

  def testLabel_sec10_12() {
    assertEquals("Seções X, XI e XII", Urn2NomeCompacto.format(List("sec10", "sec11", "sec12")))
  }

  def testLabel_sec9_12_15() {
    assertEquals("Seções IX, XII e XV", Urn2NomeCompacto.format(List("sec9", "sec12", "sec15")))
  }

  def testLabel_ite1_2() {
    assertEquals("1 e 2", Urn2NomeCompacto.format(List("ite1", "ite2")))
  }

  def testLabel_ite1_3() {
    assertEquals("1, 2 e 3", Urn2NomeCompacto.format(List("ite1", "ite2", "ite3")))
  }

  def testLabel_ali1_2() {
    assertEquals("alíneas 'a' e 'b'", Urn2NomeCompacto.format(List("ali1", "ali2")))
  }

  def testLabel_ali1_3() {
    assertEquals("alíneas 'a', 'b' e 'c'", Urn2NomeCompacto.format(List("ali1", "ali2", "ali3")))
  }

  def testLabel_inc1_2() {
    assertEquals("incisos I e II", Urn2NomeCompacto.format(List("inc1", "inc2")))
  }

  def testLabel_inc1_3() {
    assertEquals("incisos I, II e III", Urn2NomeCompacto.format(List("inc1", "inc2", "inc3")))
  }

  def testLabel_par1_2() {
    assertEquals("§§ 1º e 2º", Urn2NomeCompacto.format(List("par1", "par2")))
  }

  def testLabel_par1_3() {
    assertEquals("§§ 1º, 2º e 3º", Urn2NomeCompacto.format(List("par1", "par2", "par3")))
  }

  def testLabel_art1_2() {
    assertEquals("arts. 1º e 2º", Urn2NomeCompacto.format(List("art1", "art2")))
  }

  def testLabel_art1_3() {
    assertEquals("arts. 1º, 2º e 3º", Urn2NomeCompacto.format(List("art1", "art2", "art3")))
  }

  def testLabel_anx25_anx3_anx1() {
    assertEquals("Anexo A do Anexo 3 do Anexo XXV", Urn2NomeCompacto.format(List("anx25_anx3_anx1")))
  }

  def testLabel_art69_par1u() {
    assertEquals("art. 69, parágrafo único", Urn2NomeCompacto.format(List("art69_par1u")))
  }

  def testLabel_art69_cpt_inc3() {
    assertEquals("art. 69, inciso III", Urn2NomeCompacto.format(List("art69_cpt_inc3")))
  }

  def testLabel_art69_cpt_inc3_par1u() {
    assertEquals("art. 69, inciso III e art. 69, parágrafo único", Urn2NomeCompacto.format(List("art69_cpt_inc3", "art69_par1u")))
  }

  def testLabel_anx1011() {
    assertEquals("Anexos X e XI", Urn2NomeCompacto.format(List("anx10", "anx11")))
  }

  def testLabel_anx1012() {
    assertEquals("Anexos X, XI e XII", Urn2NomeCompacto.format(List("anx10", "anx11", "anx12")))
  }

  def testLabel_anx10() {
    assertEquals("Anexo X", Urn2NomeCompacto.format(List("anx10")))
  }

  def testLabel_art45() {
    assertEquals("arts. 4º e 5º", Urn2NomeCompacto.format(List("art4", "art5")))
  }

  def testLabel_art1_3I_5() {
    assertEquals("art. 1º e art. 3º, inciso I e art. 5º", Urn2NomeCompacto.format(List("art1", "art3_cpt_inc1", "art5")))
  }

  def testLabel_art1_3I_579_1() {
    assertEquals("art. 1º e art. 3º, inciso I e arts. 5º, 7º e 9º", Urn2NomeCompacto.format(List("art1", "art3_cpt_inc1", "art5", "art7", "art9")))
  }

  def testLabel_art1_3I_579_2() {
    assertEquals("art. 1º e art. 3º, parágrafo único e arts. 5º, 7º e 9º", Urn2NomeCompacto.format(List("art1", "art3_par1u", "art5", "art7", "art9")))
  }

  def testLabel_art1_3I_579_3() {
    assertEquals("art. 1º e art. 3º, § 1º e arts. 5º, 7º e 9º", Urn2NomeCompacto.format(List("art1", "art3_par1", "art5", "art7", "art9")))
  }

  def testLabel_art1_3I_579_4() {
    assertEquals("art. 1º e art. 3º, §§ 1º, 3º e 5º e arts. 5º, 7º e 9º", Urn2NomeCompacto.format(List("art1", "art3_par1", "art3_par3", "art3_par5", "art5", "art7", "art9")))
  }

  def testLabel_art1_43() {
    assertEquals("arts. 1º, 2º, 3º, 4º, 5º, 6º, 7º, 8º, 9º, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 38-A, 39, 39-A, 40-A, 41, 42 e 43", Urn2NomeCompacto.format(List("art1", "art2", "art3", "art4", "art5", "art6", "art7", "art8", "art9", "art10", "art11", "art12", "art13", "art14", "art15", "art16", "art17", "art18", "art19", "art20", "art21", "art28", "art29", "art30", "art31", "art32", "art33", "art34", "art35", "art36", "art37", "art38", "art38-A", "art39", "art39-A", "art40-A", "art41", "art42", "art43")))
  }

  def testLabel_art_A_EPar() {
    assertEquals("art. 38-A, §§ 1º e 2º", Urn2NomeCompacto.format(List("art38-A_par1", "art38-A_par2")))
  }

  def testLabel_art135() {
    assertEquals("arts. 1º, 3º e 5º", Urn2NomeCompacto.format(List("tit1_sec1_art1", "tit1_sec1_art3", "tit1_sec1_art5")))
  }

  def testLabel_art1357() {
    assertEquals("arts. 1º, 3º, 5º e 7º", Urn2NomeCompacto.format(List("tit1_sec1_art1", "tit1_sec1_art3", "tit1_sec1_art5", "art7")))
  }

  def testLabel_art135711() {
    assertEquals("arts. 1º, 3º, 5º, 7º e 11", Urn2NomeCompacto.format(List("tit1_sec1_art1", "tit1_sec1_art3", "tit1_sec1_art5", "art7", "tit50_sec1_art11")))
  }

  def testLabel_art135711anx1() {
    assertEquals("arts. 1º, 3º, 5º, 7º e 11 e art. 11 do Anexo I", Urn2NomeCompacto.format(List("tit1_sec1_art1", "tit1_sec1_art3", "tit1_sec1_art5", "art7", "tit50_sec1_art11", "anx1_art11")))
  }

  def testLabel_anx912() {
    assertEquals("Anexos IX e XII", Urn2NomeCompacto.format(List("anx9", "anx12")))
  }

  def testLabel_art135711anx1tit() {
    assertEquals("arts. 1º, 3º, 5º, 7º e 11 e arts. 11, 15 e 17 do Anexo I e Título X do Anexo I e art. 25 do Anexo I", Urn2NomeCompacto.format(List("tit1_sec1_art1", "tit1_sec1_art3", "tit1_sec1_art5", "art7", "tit50_sec1_art11", "anx1_art11", "anx1_art15", "anx1_art17", "anx1_tit10", "anx1_art25")))
  }

  def testLabel_tit1013() {
    assertEquals("Títulos X e XIII", Urn2NomeCompacto.format(List("tit10", "tit13")))
  }

  def testLabel_art1_3A_6() {
    assertEquals("arts. 1º, 2º, 3º, 3º-A, 4º, 5º e 6º", Urn2NomeCompacto.format(List("art1", "art2", "art3", "art3-A", "art4", "art5", "art6")))
  }

  def testLabel_partes() {
    assertEquals("Partes I, II, III, V, VI e VIII", Urn2NomeCompacto.format(List("prt1", "prt2", "prt3", "prt5", "prt6", "prt8")))
  }

  def testLabel_titulos() {
    assertEquals("Títulos I, II, III, V, VI e VIII", Urn2NomeCompacto.format(List("tit1", "tit2", "tit3", "tit5", "tit6", "tit8")))
  }

  def testLabel_capitulos() {
    assertEquals("Capítulos I, II, III, V, VI e VIII", Urn2NomeCompacto.format(List("cap1", "cap2", "cap3", "cap5", "cap6", "cap8")))
  }

  def testLabel_subsecoes() {
    assertEquals("Subseções I, II, III, V, VI e VIII", Urn2NomeCompacto.format(List("sub1", "sub2", "sub3", "sub5", "sub6", "sub8")))
  }

  def testLabel_livros() {
    assertEquals("Livros I, II, III, V, VI e VIII", Urn2NomeCompacto.format(List("liv1", "liv2", "liv3", "liv5", "liv6", "liv8")))
  }

  def testLabel_anx1_tit1_art1200_1201() = {
    assertEquals("arts. 1.200 e 1.201 do Anexo I", Urn2NomeCompacto.format(List("anx1_tit1_art1200", "anx1_tit1_art1201")))
  }

  def testReturnEmpty_QuandoHaUmErro(): Unit = {
    assertEquals("", Urn2NomeCompacto.format(List("unexpected_1")))
  }

  def testLabel_art10_1_cpt_inc1() = {
    assertEquals("art. 10-A, inciso I", Urn2NomeCompacto.format(List("art10-1_cpt_inc1")))
  }

  def testLabel_art10_a_cpt_inc1() = {
    assertEquals("art. 10-A, inciso I", Urn2NomeCompacto.format(List("art10-a_cpt_inc1")))
  }

  def testLabel_art10_A_cpt_inc1() = {
    assertEquals("art. 10-A, inciso I", Urn2NomeCompacto.format(List("art10-A_cpt_inc1")))
  }

  def testLabel_tit3_cap4_sec2_with_context_tit3_cap4_sec1_art62_inc4() = {
    assertEquals("Seção II deste capítulo", Urn2NomeCompacto.format(List("tit3_cap4_sec2"), "tit3_cap4_sec1_art62_inc4"))
  }

  def testLabel_cpp_tit3_cap4_sec2_with_context_cpp_tit3_cap4_sec1_art76_cpt_inc4() = {
    assertEquals("Seção II deste capítulo", Urn2NomeCompacto.format(List("cpp_tit3_cap4_sec2"), "cpp_tit3_cap4_sec1_art76_cpt_inc4"))
  }

  def testLabel_cpp_tit3_cap3_sec2_with_context_cpp_tit3_cap4_sec1_art76_cpt_inc4() = {
    assertEquals("Seção II do Capítulo III deste título", Urn2NomeCompacto.format(List("cpp_tit3_cap3_sec2"), "cpp_tit3_cap4_sec1_art76_cpt_inc4"))
  }

  def testLabel_anx1_with_context_cpp_prt1_liv1_art2_cpt() = {
    assertEquals("Anexo I", Urn2NomeCompacto.format(List("anx1"), "cpp_prt1_liv1_art2_cpt"))
  }

  def testLabel_cpp_tit3_cap4_art72_with_context_cpp_tit3_cap4_sec3_art80_cpt() = {
    assertEquals("art. 72", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72"), "cpp_tit3_cap4_sec3_art80_cpt"))
  }

  def testLabel_anx1_prt1_cap2_art3_with_context_anx1_prt1_cap7_art15_cpt_inc() = {
    assertEquals("art. 3º deste anexo", Urn2NomeCompacto.format(List("anx1_prt1_cap2_art3"), "anx1_prt1_cap7_art15_cpt_inc"))
  }

  def testLabel_cpp_tit1_cap4_with_context_cpp_tit1_cap4_art35_par1() = {
    assertEquals("Capítulo IV", Urn2NomeCompacto.format(List("cpp_tit1_cap4"), "cpp_tit1_cap4_art35_par1"))
  }

  def testLabel_cpp_tit1_cap4_with_context_cpp_tit2_cap5_art35_par1() = {
    assertEquals("Capítulo IV do Título I", Urn2NomeCompacto.format(List("cpp_tit1_cap4"), "cpp_tit2_cap5_art35_par1"))
  }

  def testLabel_cpp_tit1_cap4_with_context_cpp_tit1_cap5_art36_par1() = {
    assertEquals("Capítulo IV deste título", Urn2NomeCompacto.format(List("cpp_tit1_cap4"), "cpp_tit1_cap5_art36_par1"))
  }

  def testLabel_cpp_cap4_with_context_cpp_cap5_art36_par1() = {
    assertEquals("Capítulo IV", Urn2NomeCompacto.format(List("cpp_cap4"), "cpp_cap5_art36_par1"))
  }

  def testLabel_anx1_prt1_cap7_with_context_anx1_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("Capítulo VII", Urn2NomeCompacto.format(List("anx1_prt1_cap7"), "anx1_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_prt1_cap7_with_context_anx1_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("Anexo II", Urn2NomeCompacto.format(List("anx2"), "anx1_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_tit3_cap3_sec2_with_context_anx1_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("Seção II do Capítulo III do Título III do Anexo II", Urn2NomeCompacto.format(List("anx2_tit3_cap3_sec2"), "anx1_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_tit3_cap3_sec2_art1_with_context_anx1_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("art. 1º do Anexo II", Urn2NomeCompacto.format(List("anx2_tit3_cap3_sec2_art1"), "anx1_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_art1_with_context_anx1_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("art. 1º do Anexo II", Urn2NomeCompacto.format(List("anx2_art1"), "anx1_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_tit3_cap3_sec2_art1_with_context_anx2_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("art. 1º deste anexo", Urn2NomeCompacto.format(List("anx2_tit3_cap3_sec2_art1"), "anx2_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_anx2_art1_with_context_anx2_prt1_cap7_art15_cpt_inc2() = {
    assertEquals("art. 1º deste anexo", Urn2NomeCompacto.format(List("anx2_art1"), "anx2_prt1_cap7_art15_cpt_inc2"))
  }

  def testLabel_liv2_tit1_cap2_with_context_liv2_tit1_cap3() = {
    assertEquals("Capítulo II deste título", Urn2NomeCompacto.format(List("liv2_tit1_cap2"), "liv2_tit1_cap3"))
  }

  def testLabel_liv2_tit1_cap2_with_context_liv2_tit5_cap4_sec1() = {
    assertEquals("Capítulo II do Título I deste livro", Urn2NomeCompacto.format(List("liv2_tit1_cap2"), "liv2_tit5_cap4_sec1"))
  }

  def testLabel_liv2_tit1_cap2_with_context_liv3_tit5_cap4_sec1() = {
    assertEquals("Capítulo II do Título I do Livro II", Urn2NomeCompacto.format(List("liv2_tit1_cap2"), "liv3_tit5_cap4_sec1"))
  }


  /*
   *
   * se houver contexto, e a remissão for para dispositivos filhos do próprio **artigo**
   * do dispositivo contexto, então o que for comum deve ser omitido também.
   * Usa-se, nesse caso, uma mistura da formatação completa com a formatação compacta
   *
   *   - indica-se o nome do tipo do dispositivo, mais o número dele, como nos exemplos abaixo
   *
   */

  def testLabel_cpp_tit3_cap4_art72_context_cpp_tit3_cap4_art72_inc1() = {
    assertEquals("caput", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72"), "cpp_tit3_cap4_art72_inc1"))
  }

  def testLabel_anx1_art25_27_anx1_art30_contexto_art30() {
    assertEquals("arts. 25, 26 e 27 deste anexo", Urn2NomeCompacto.format(List("anx1_art25", "anx1_art26", "anx1_art27"), "anx1_art30"))
  }

  def testLabel_cpp_tit3_cap4_art72_cpt_context_cpp_tit3_cap4_art72_par1() = {
    assertEquals("caput", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72_cpt"), "cpp_tit3_cap4_art72_par1"))
  }

  def testLabel_cpp_tit3_cap4_art72_par1_context_cpp_tit3_cap4_art72_cpt() = {
    assertEquals("§ 1º", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72_par1"), "cpp_tit3_cap4_art72_cpt"))
  }

  def testLabel_cpp_tit3_cap4_art72_inc1_context_cpp_tit3_cap4_art72_cpt() = {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72_cpt_inc1"), "cpp_tit3_cap4_art72_cpt"))
  }

  def testLabel_cap4_art72_par1_context_art72_cpt() = {
    assertEquals("§ 1º", Urn2NomeCompacto.format(List("art72_par1"), "art72_cpt"))
  }

  def testLabel_cap4_art72_par1_inc3_context_art72_cpt() = {
    assertEquals("inciso III do § 1º", Urn2NomeCompacto.format(List("art72_par1_inc3"), "art72_cpt"))
  }

  def testLabel_cap4_art72_par1_inc3_context_art72_par1() = {
    assertEquals("inciso III", Urn2NomeCompacto.format(List("cpp_tit3_cap4_art72_par1_inc3"), "cpp_tit3_cap4_art72_par1"))
  }

  def testLabel_cap4_art72_par1_inc3_context_art72_cpt_par1() = {
    assertEquals("inciso III", Urn2NomeCompacto.format(List("art72_cpt_par1_inc3"), "art72_cpt_par1"))
  }

  def testLabel_anx8_tit3_cap1_sec1_art29_cpt_inc4_context_anx8_tit3_cap1_sec1_art29_par3() = {
    assertEquals("inciso IV do caput", Urn2NomeCompacto.format(List("anx8_tit3_cap1_sec1_art29_cpt_inc4"), "anx8_tit3_cap1_sec1_art29_par3"))
  }

  def testLabel_cpp_tit3_cap1_sec1_art29_cpt_inc4_context_cpp_tit3_cap1_sec1_art29_par3() = {
    assertEquals("inciso IV do caput", Urn2NomeCompacto.format(List("cpp_tit3_cap1_sec1_art29_cpt_inc4"), "cpp_tit3_cap1_sec1_art29_par3"))
  }

  def test_art9_cpt_anx8_art9_cpt() {
    assertEquals("art. 9º, caput", Urn2NomeCompacto.format(List("art9_cpt"), "anx8_art9_cpt"));
  }

  def test_anx8_art9_cpt_anx8_art9_cpt() {
    assertEquals("caput", Urn2NomeCompacto.format(List("anx8_art9_cpt"), "anx8_art9_cpt"));
  }

  def test_anx8_art9_cpt_anx8_art9_inc1() {
    assertEquals("caput", Urn2NomeCompacto.format(List("anx8_art9_cpt"), "anx8_art9_inc1"));
  }

  def test_anx8_art9_anx8_art9_inc1() {
    assertEquals("caput", Urn2NomeCompacto.format(List("anx8_art9"), "anx8_art9_inc1"));
  }

  def test_anx8_art9_anx8_art9_inc1_ali20_ite3020() {
    assertEquals("caput", Urn2NomeCompacto.format(List("anx8_art9"), "anx8_art9_inc1_ali20_ite30-20"));
  }

  def test_anxXPTO_art9_inc1_sem_contexto() {
    assertEquals("art. 9º, inciso I do Anexo XPTO", Urn2NomeCompacto.format(List("anx;XPTO_art9_inc1")));
  }

  def test_anxXPTO_art9_inc1_anx8_art9_cpt() {
    assertEquals("art. 9º, inciso I do Anexo XPTO", Urn2NomeCompacto.format(List("anx;XPTO_art9_inc1"), "anx8_art9_cpt"));
  }

  def test_anxXPTO_art9_inc1_anxXPTO_art9_cpt() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx;XPTO_art9_inc1"), "anx;XPTO_art9_cpt"));
  }

  def test_anxXPTO_art9_cpt_inc1_anxXPTO_art9_par1() {
    assertEquals("inciso I do caput", Urn2NomeCompacto.format(List("anx;XPTO_art9_cpt_inc1"), "anx;XPTO_art9_par1"));
  }

  def test_anxXPTO_art9_cpt_inc1_anxXPTO_art9_cpt() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx;XPTO_art9_cpt_inc1"), "anx;XPTO_art9_cpt"))
  }

  def test_anx8_art9_inc1_anx8_art9_inc2() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx8_art9_inc1"), "anx8_art9_inc2"));
  }

  def test_anx8_art9_cpt_inc1_anx8_art9_cpt_inc2() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx8_art9_cpt_inc1"), "anx8_art9_cpt_inc2"));
  }

  def test_anx8_art9_cpt_inc1_anx8_art9_par1() {
    assertEquals("inciso I do caput", Urn2NomeCompacto.format(List("anx8_art9_cpt_inc1"), "anx8_art9_par1"));
  }

  def test_anxXPTO_art9_inc1_anxXPTO_art9_inc2() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx;XPTO_art9_inc1"), "anx;XPTO_art9_inc2"));
  }

  def test_anx8_art10_cpt_context_anx8_art9_cpt() {
    assertEquals("art. 10, caput deste anexo", Urn2NomeCompacto.format(List("anx8_art10_cpt"), "anx8_art9_cpt"));
  }

  def testLabel_cpp_tit2_art5_par1_context_cpp_tit2_art6_par4() = {
    assertEquals("art. 5º, § 1º", Urn2NomeCompacto.format(List("cpp_tit2_art5_par1"), "cpp_tit2_art6_par4"))
  }

  def test_anx8_art9_cpt_inc1_context_anx8_art9_cpt() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("anx8_art9_cpt_inc1"), "anx8_art9_cpt"));
  }

  def test_anx8_art9_cpt_inc1_context_anx8_art9_par1() {
    assertEquals("inciso I do caput", Urn2NomeCompacto.format(List("anx8_art9_cpt_inc1"), "anx8_art9_par1"));
  }

  def test_anxXPTO_art9_cpt_inc1_context_anx8_art9_par1() {
    assertEquals("inciso I do caput", Urn2NomeCompacto.format(List("anx;XPTO_art9_cpt_inc1"), "anx;XPTO_art9_par1"));
  }

  def test_anx9_art9_par2_inc2_context_anx9_art9_par1() {
    assertEquals("inciso II do § 2º", Urn2NomeCompacto.format(List("anx9_art9_par2_inc2"), "anx9_art9_par1"));
  }

  def test_anx8_art9_cpt_context_anx8_art9_cpt() {
    assertEquals("caput", Urn2NomeCompacto.format(List("anx8_art9_cpt"), "anx8_art9_cpt"));
  }

  def test_art9_inc1_context_art9_cpt() {
    assertEquals("inciso I", Urn2NomeCompacto.format(List("art9_inc1"), "art9_cpt"));
  }

  def testLabel_anx100_tit22_art5_par1_context_anx100_tit22_art6_par4() = {
    assertEquals("art. 5º, § 1º deste anexo", Urn2NomeCompacto.format(List("anx100_tit22_art5_par1"), "anx100_tit22_art6_par4"))
  }

  def testLabel_cpp_tit2_art5_par1_context_cpp_tit200_art6_par4() = {
    assertEquals("art. 5º, § 1º", Urn2NomeCompacto.format(List("cpp_tit2_art5_par1"), "cpp_tit200_art6_par4"))
  }

  def testLabel_cpp_tit2context_cpp_tit200() = {
    assertEquals("Título II", Urn2NomeCompacto.format(List("cpp_tit2"), "cpp_tit200"))
  }

  def testLabel_cpp_tit2_sec2_context_cpp_tit2_sec1() = {
    assertEquals("Seção II deste título", Urn2NomeCompacto.format(List("cpp_tit2_sec2"), "cpp_tit2_sec1"))
  }

  def testLabel_cpp_tit2_sec2_context_cpp_tit200_sec1() = {
    assertEquals("Seção II do Título II", Urn2NomeCompacto.format(List("cpp_tit2_sec2"), "cpp_tit200_sec1"))
  }

  /*
   *
   * se houver contexto, e a remissão for para dispositivos filhos do próprio **artigo**
   * casos múltiplos: usa-se o plural dos tipos múltiplos
   *
   */

  def testLabel_art9_inc1_4_context_art9() {
    assertEquals("incisos I, II, III e IV", Urn2NomeCompacto.format(List("art9_inc1", "art9_inc2", "art9_inc3", "art9_inc4"), "art9"))
  }

  def testLabel_art9_inc1_4_inc5_ali_1_context_art9() {
    assertEquals("incisos I, II, III e IV e inciso V, alínea 'a'", Urn2NomeCompacto.format(List("art9_inc1", "art9_inc2", "art9_inc3", "art9_inc4", "art9_inc5_ali1"), "art9"))
  }

  def testLabel_art9_inc1_4_inc5_ali_1_2context_art9() {
    assertEquals("incisos I, II, III e IV e inciso V, alíneas 'a' e 'b'", Urn2NomeCompacto.format(List("art9_inc1", "art9_inc2", "art9_inc3", "art9_inc4", "art9_inc5_ali1", "art9_inc5_ali2"), "art9"))
  }

  def testLabel_art9_inc1_4_inc5_ali_1_2context_art9_docaput() {
    assertEquals("incisos I, II, III e IV do caput e inciso V, alíneas 'a' e 'b' do caput", Urn2NomeCompacto.format(List("art9_cpt_inc1", "art9_cpt_inc2", "art9_cpt_inc3", "art9_cpt_inc4", "art9_cpt_inc5_ali1", "art9_cpt_inc5_ali2"), "art9"))
  }

  def testLabel_cap4_art72_par124_context_art72_cpt() {
    assertEquals("§§ 1º, 2º e 4º", Urn2NomeCompacto.format(List("art72_par1", "art72_par2", "art72_par4"), "art72_cpt"))
  }

  def testLabel_cap4_art72_par124_context_art75_cpt() {
    assertEquals("art. 72, §§ 1º, 2º e 4º", Urn2NomeCompacto.format(List("art72_par1", "art72_par2", "art72_par4"), "art75_cpt"))
  }

  def testLabel_cap4_art72_par1235_context_art72_cpt() {
    assertEquals("§§ 1º, 2º, 3º e 5º", Urn2NomeCompacto.format(List("art72_par1", "art72_par2", "art72_par3", "art72_par5"), "art72_cpt"))
  }

  def testLabel_cap4_art72_par1235_context_cap1_art72_cpt() {
    assertEquals("§§ 1º, 2º, 3º e 5º", Urn2NomeCompacto.format(List("cap1_art72_par1", "cap1_art72_par2", "cap1_art72_par3", "cap1_art72_par5"), "cap1_art72_cpt"))
  }

  def testLabel_anx2_art3_inc3_context_anx2_art9_par1() = {
    assertEquals("art. 9º, § 1º deste anexo", Urn2NomeCompacto.format(List("anx2_art9_par1"), "anx2_art3_inc3"))
  }

  def testcpp_anx30_anx1() {
    assertEquals("Anexo 1 do Anexo XXX", Urn2NomeCompacto.format(List("cpp_anx30_anx1")))
  }

  def testcpp_anexos_sequencias() {
    assertEquals("Anexos XXIX, XXX e XXXI, Anexo 1 do Anexo XXXI e Anexos XXXII e XXXIII", Urn2NomeCompacto.format(List("cpp_anx29", "cpp_anx30", "cpp_anx31", "cpp_anx31_anx1", "cpp_anx32", "cpp_anx33")))
  }

  def test_cpp_anxIX() = {
    assertEquals("Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX"))
  }

  def test_anxIX() = {
    assertEquals("Anexo IX", Urn2NomeCompacto.format("anx;IX"))
  }

  def test_cpp_anx1() = {
    assertEquals("Anexo 11", Urn2NomeCompacto.format("cpp_anx;11"))
  }

  def test_anx1() = {
    assertEquals("Anexo 1", Urn2NomeCompacto.format("anx;1"))
  }

  def test_cpp_anxI() = {
    assertEquals("Anexo I", Urn2NomeCompacto.format("cpp_anx;I"))
  }

  def test_anxVI() = {
    assertEquals("Anexo VI", Urn2NomeCompacto.format("anx;VI"))
  }

  def test_clausula_revogacao_enorme() = {
    assertEquals("arts. 1º e 2º e art. 2º, caput e arts. 3º, 4º, 6º e 7º e art. 7º, § 3º e Capítulo III e arts. 9º, 10, 11, 12, 13, 14, 15 e 16 e art. 16, caput e arts. 17 e 18 e Anexos 3, 4 e 5",
      Urn2NomeCompacto.format(List("cpp_atc_art1", "cpp_atc_art2", "cpp_atc_art2_cpt", "cpp_atc_cap1_art3",
        "cpp_atc_cap1_art4", "cpp_atc_cap2_art6", "cpp_atc_cap2_art7", "cpp_atc_cap2_art7_par3", "cpp_atc_cap3", "cpp_atc_cap3_art9",
        "cpp_atc_cap4_art10", "cpp_atc_cap4_art11", "cpp_atc_cap4_art12", "cpp_atc_cap4_art13", "cpp_atc_cap4_art14", "cpp_atc_cap4_art15",
        "cpp_atc_cap4_art16", "cpp_atc_cap4_art16_cpt", "cpp_atc_cap4_art17", "cpp_atc_cap4_art18", "cpp_anx;3", "cpp_anx;4", "cpp_anx;5")))
  }

  def test_cpp_atc_art1() = {
    assertEquals("art. 1º", Urn2NomeCompacto.format(List("cpp_atc_art1")))
  }

  def test_cpp_atc_art1_cpp_atc_cap3() = {
    assertEquals("art. 1º e Capítulo III", Urn2NomeCompacto.format(List("cpp_atc_art1", "cpp_atc_cap3")))
  }

  def test_cpp_art1_cpp_cap3() = {
    assertEquals("art. 1º e Capítulo III", Urn2NomeCompacto.format(List("cpp_art1", "cpp_cap3")))
  }

  def test_cpp_anx_CIII_atc_art1_par2() = {
    assertEquals("art. 1º, § 2º do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_art1_par2"))
  }

  def test_cpp_anx_CIII_atc_art1_par1u() = {
    assertEquals("art. 1º, parágrafo único do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_art1_par1u"))
  }

  def test_cpp_anx_CIII_atc_art14_cpt_inc12_ali2_ite1() = {
    assertEquals("art. 14, inciso XII, alínea 'b', 1 do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_art14_cpt_inc12_ali2_ite1"))
  }

  def test_cpp_anx_CIII_atc_art1_par1u_inc3_ali2() = {
    assertEquals("art. 1º, parágrafo único, inciso III, alínea 'b' do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_art1_par1u_inc3_ali2"))
  }

  def test_cpp_anx_CIII_atc_tit1() = {
    assertEquals("Título I do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_tit1"))
  }

  def test_cpp_anx_CIII_atc_tit1_cap1_sec2_sub4() = {
    assertEquals("Subseção IV da Seção II do Capítulo I do Título I do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_tit1_cap1_sec2_sub4"))
  }

  def test_cpp_anx_CIII_atc_tit1_cap1_art19_cpt() = {
    assertEquals("art. 19, caput do Anexo CIII", Urn2NomeCompacto.format("cpp_anx;CIII_atc_tit1_cap1_art19_cpt"))
  }

  def test_cpp_anx_IX_atc_cap5_art28_par2_inc11() = {
    assertEquals("art. 28, § 2º, inciso XI do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_cap5_art28_par2_inc11"))
  }

  def test_cpp_anx_IX_atc_cap5_art29_par2_inc16() = {
    assertEquals("art. 29, § 2º, inciso XVI do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_cap5_art29_par2_inc16"))
  }

  def test_cpp_anx_I_atc_tit1_art1() = {
    assertEquals("art. 1º do Anexo I", Urn2NomeCompacto.format("cpp_anx;I_atc_tit1_art1"))
  }

  def test_cpp_anx_I_atc_tit1_cap1() = {
    assertEquals("Capítulo I do Título I do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_tit1_cap1"))
  }

  def test_cpp_anx_IX_atc_tit1_cap1_sec2() = {
    assertEquals("Seção II do Capítulo I do Título I do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_tit1_cap1_sec2"))
  }

  def test_cpp_anx_IX_atc_tit1_cap1_sec2_sub4() = {
    assertEquals("Subseção IV da Seção II do Capítulo I do Título I do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_tit1_cap1_sec2_sub4"))
  }

  def test_cpp_anx_IX_atc_liv3() = {
    assertEquals("Livro III do Anexo IX", Urn2NomeCompacto.format("cpp_anx;IX_atc_liv3"))
  }

  def test_cpp_anx_XXX_anx_A() {
    assertEquals("Anexo A do Anexo XXX", Urn2NomeCompacto.format(List("cpp_anx;XXX_anx;A")));
  }

  def test_cpp_anx_ZZZZZZ_anx_TTTTT() {
    assertEquals("Anexo TTTTT do Anexo ZZZZZZ", Urn2NomeCompacto.format(List("cpp_anx;ZZZZZZ_anx;TTTTT")));
  }

}
