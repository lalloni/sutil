package sutil.math

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

@RunWith(classOf[JUnitRunner])
class DigitSuite extends FunSuite with ShouldMatchers with PropertyChecks {

  test("Any Integral can be converted to Seq[Digit] in LSF order") {

    forAll { (n: BigInt) ⇒

      val digits = n.digits
      
      val expected = n.abs.toString.reverse.map(c ⇒ Digit(c.asDigit))

      (digits zip expected) foreach { case (digit, e) ⇒ digit should be (e) }

      n.unitsDigit should be (expected(0))

      whenever (n.abs >= 10) {
        n.tensDigit should be (expected(1))
      }

      whenever (n.abs >= 100) {
        n.hundredsDigit should be (expected(2))
      }

      whenever (n.abs >= 1000) {
        n.thousandsDigit should be (expected(3))
      }

      whenever (n.abs >= 1000000) {
        n.millionsDigit should be (expected(6))
      }

      whenever (n.abs >= 1000000000) {
        n.billionsDigit should be (expected(9))
      }

      whenever (n.abs >= 1000000000000l) {
        n.trillionsDigit should be (expected(12))
      }

    }

  }

}
