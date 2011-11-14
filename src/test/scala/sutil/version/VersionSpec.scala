package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionSpec extends Spec with ShouldMatchers {

  describe("A Version") {

    it("should build an exclusive range") {
      val range = V(N(1)) until V(N(2))
      range contains V(N(0, 999)) should be (false)
      range contains V(N(1)) should be (true)
      range contains V(N(2)) should be (false)
      range contains V(N(1, 999, 99)) should be (true)
      range contains V(N(2, 0, 0, 0, 1)) should be (false)
    }

    it("should build an inclusive range") {
      val range = V(N(1)) to V(N(2))
      range contains V(N(0, 999)) should be (false)
      range contains V(N(1)) should be (true)
      range contains V(N(2)) should be (true)
      range contains V(N(1, 999, 99)) should be (true)
      range contains V(N(2, 0, 0, 0, 1)) should be (false)
    }

    it("should sort appropriately") {

      val versions = Seq[Version]("1", "1.0", "1.1", "2", "0.1", "2-snapshot", "3", "3-sp4")

      val expected = Seq(V(N(0, 1)), V(N(1)), V(N(1, 0)), V(N(1, 1)), V(N(2), M("snapshot")), V(N(2)), V(N(3)), V(N(3), M("sp", Some(N(4)))))

      versions.sorted should be (expected)

    }

  }

}
