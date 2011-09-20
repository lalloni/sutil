package sutil.afip

import sutil.math._
import sutil.math.Digit._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

class CISpec extends FunSuite with ShouldMatchers {

  test("Verifier computation") {
    CI.verifier(CI("20242643772")) should equal(Digit(2))
  }

  test("CI building") {

    def check(ci: CI, pre: Byte, id: Int, ver: Digit) = {
      ci.prefix should equal(pre)
      ci.id should equal(id)
      ci.verifier should equal(ver)
    }

    val cuit1 = CI(20242643772l)
    check(cuit1, 20, 24264377, Digit(2))

    val cuit2 = CI("20242643772")
    check(cuit2, 20, 24264377, Digit(2))

    val cuit3 = CI("20-24264377-2")
    check(cuit3, 20, 24264377, Digit(2))

    val cuit4 = CI(20, 24264377, Digit(2))
    check(cuit4, 20, 24264377, Digit(2))

    //println(Seq(cuit1, cuit2, cuit3, cuit4, CI(20, 11111111, 2.toDigit), CI(20, 00000000, 1.toDigit)))

  }

}
