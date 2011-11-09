package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionRangeSpec extends Spec with ShouldMatchers {

  describe("InclusiveVersionRange") {

    it("should contain its limits") {
      val range = VersionNumber(1) to VersionNumber(2, 1)
      range contains VersionNumber(1) should be (true)
      range contains VersionNumber(2, 1) should be (true)
    }

  }

  describe("ExclusiveVersionRange") {
    it("should contain it start limit but not it end limit") {
      val range = VersionNumber(1) until VersionNumber(2, 1)
      range contains VersionNumber(1) should be (true)
      range contains VersionNumber(2, 1) should be (false)
    }
  }

}
