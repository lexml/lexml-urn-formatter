package urn2label

object App extends App {

  val l = List(
    "art1"
      , "art1-27"
      , "art4_par1u"
      , "art4_par1u_ite7"
      , "art4_par1u_ite7_ali3"
      , "art4_par1u_ite7_ali3_inc2"
      , "art1-1"
      , "art1-2")

  l.foreach { x => println(x + "=>" + Fragment2Label.format(x)) }

}