package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionRangeSpec extends Spec with ShouldMatchers {

  describe("InclusiveVersionRange") {

    it("should contain its limits") {
      val range = Version("1") to Version("2.1")
      range contains Version("1") should be (true)
      range contains Version("2.1") should be (true)
    }

  }

  describe("ExclusiveVersionRange") {

    it("should contain it start limit but not it end limit") {
      val range = Version(N(1)) until Version(N(2, 1))
      range contains Version(N(1)) should be (true)
      range contains Version(N(2, 1)) should be (false)
    }

    it ("should include ending snapshots & prereleases correctly") {
      val r = V("1.0") until V("3.0")
      r contains V("3") should be (false)
      r contains V("3-sp2") should be (false)
      r contains V("3-snapshot") should be (true)
      r contains V("3-rc1") should be (true)
    }

  }

}
