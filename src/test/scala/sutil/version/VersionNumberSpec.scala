package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionNumberSpec extends Spec with ShouldMatchers {

  describe("Numbers") {

    it("should not allow to be built without numbers") {

      intercept[IllegalArgumentException] {
        V()
      }

    }

    it("should print nice version number string") {

      V(1, 2, 3).toString should be === "1.2.3"

      V(1).toString should be === "1"

      V(5, 2).toString should be === "5.2"

    }

    it("should sort in correct order") {

      val seq = Seq(V(1, 2, 3), V(2, 1, 1), V(1), V(5, 2), V(1, 2))

      seq.sorted should be === Seq(V(1), V(1, 2), V(1, 2, 3), V(2, 1, 1), V(5, 2))

    }

    it("should not allow negative numbres") {
      intercept[IllegalArgumentException] {
        V(-10)
      }
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
        case VP(1 :: 2 :: _) ⇒ true
        case _               ⇒ false
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

    it("should build an inclusive range") {
      V(1) to V(2) should be (VersionRange.Inclusive(V(1), V(2)))
    }

    it("should build an exclusive range") {
      V(1) until V(2) should be (VersionRange.Exclusive(V(1), V(2)))
    }

  }

}
