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
        V()
      } should produce[IllegalArgumentException]
    }

    it("should print nice version number string") {
      V(1, 2, 3).toString should be ("1.2.3")
      V(1).toString should be ("1")
      V(5, 2).toString should be ("5.2")

    }

    it("should sort in correct order") {
      Seq(V(1, 2, 3), V(2, 1, 1), V(1), V(5, 2), V(1, 2)).sorted should be (Seq(V(1), V(1, 2), V(1, 2, 3), V(2, 1, 1), V(5, 2)))
    }

    it("should not allow negative numbres") {
      evaluating {
        V(-10)
      } should produce[IllegalArgumentException]
    }

    it("should be possible to pattern match on all numbers") {
      val matches = V(1, 2, 3) match {
        case V(1, 2, 3) ⇒ true
        case _          ⇒ false
      }
      matches should be (true)
    }

    it("should be possible to pattern match on some head") {
      (V(1, 2, 3) match {
        case V(1, 2, _*) ⇒ true
        case _           ⇒ false
      }) should be (true)
    }

    it("should increment correctly last number") {
      V(1, 2, 3).incrementLast should be (V(1, 2, 4))
    }

    it("should increment correctly major number") {
      V(1, 2, 3).incrementAt(VersionNumberPosition.Major) should be (V(2, 2, 3))
    }

    it("should increment correctly minor number") {
      V(1, 2, 3).incrementAt(VersionNumberPosition.Minor) should be (V(1, 3, 3))
    }

    it("should increment correctly fix number") {
      V(1, 2, 3).incrementAt(VersionNumberPosition.Fix) should be (V(1, 2, 4))
    }

    it("should increment correctly some deep number") {
      V(1, 2, 3, 3, 3, 3, 3, 3).incrementAt(5) should be (V(1, 2, 3, 3, 3, 4, 3, 3))
    }

    it("should increment correctly  by another version number") {
      V(1, 2, 3, 3, 3, 3, 3, 3).incrementBy(V(0, 1, 2)) should be (V(1, 3, 5, 3, 3, 3, 3, 3))
    }

    it("should build an exclusive range") {
      val range = V(1) until V(2)
      range contains V(0, 999) should be (false)
      range contains V(1) should be (true)
      range contains V(2) should be (false)
      range contains V(1, 999, 99) should be (true)
      range contains V(2, 0, 0, 0, 1) should be (false)
    }

    it("should build an inclusive range") {
      val range = V(1) to V(2)
      range contains V(0, 999) should be (false)
      range contains V(1) should be (true)
      range contains V(2) should be (true)
      range contains V(1, 999, 99) should be (true)
      range contains V(2, 0, 0, 0, 1) should be (false)
    }

  }

}
