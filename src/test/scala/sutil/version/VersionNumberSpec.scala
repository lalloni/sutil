package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionNumberSpec extends Spec with ShouldMatchers {

  describe("VersionNumber") {

    it("should not allow to be built without numbers") {
      evaluating {
        N()
      } should produce[IllegalArgumentException]
    }

    it("should print nice version number string") {
      N(1, 2, 3).toString should be ("1.2.3")
      N(1).toString should be ("1")
      N(5, 2).toString should be ("5.2")

    }

    it("should sort in correct order") {
      Seq(N(1, 2, 3), N(2, 1, 1), N(1), N(5, 2), N(1, 2)).sorted should be (Seq(N(1), N(1, 2), N(1, 2, 3), N(2, 1, 1), N(5, 2)))
    }

    it("should not allow negative numbres") {
      evaluating {
        N(-10)
      } should produce[IllegalArgumentException]
    }

    it("should be possible to pattern match on all numbers") {
      val matches = N(1, 2, 3) match {
        case N(1, 2, 3) ⇒ true
        case _          ⇒ false
      }
      matches should be (true)
    }

    it("should be possible to pattern match on some head") {
      (N(1, 2, 3) match {
        case N(1, 2, _*) ⇒ true
        case _           ⇒ false
      }) should be (true)
    }

    it("should increment correctly last number") {
      N(1, 2, 3).incrementLast should be (N(1, 2, 4))
    }

    it("should increment correctly major number") {
      N(1, 2, 3).incrementAt(VersionNumberPosition.Major) should be (N(2, 2, 3))
    }

    it("should increment correctly minor number") {
      N(1, 2, 3).incrementAt(VersionNumberPosition.Minor) should be (N(1, 3, 3))
    }

    it("should increment correctly fix number") {
      N(1, 2, 3).incrementAt(VersionNumberPosition.Fix) should be (N(1, 2, 4))
    }

    it("should increment correctly some deep number") {
      N(1, 2, 3, 3, 3, 3, 3, 3).incrementAt(5) should be (N(1, 2, 3, 3, 3, 4, 3, 3))
    }

    it("should increment correctly  by another version number") {
      N(1, 2, 3, 3, 3, 3, 3, 3).incrementBy(N(0, 1, 2)) should be (N(1, 3, 5, 3, 3, 3, 3, 3))
    }

  }

}
