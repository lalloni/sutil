package sutil.version

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionSpec extends Spec with ShouldMatchers {

  describe("A Version") {

    it("should sort appropriately") {

      val versions = Seq[Version]("1", "1.0", "1.1", "2", "0.1", "2-snapshot", "3", "3-sp4")

      val expected = Seq(V(N(0, 1)), V(N(1)), V(N(1, 0)), V(N(1, 1)), V(N(2), M("snapshot")), V(N(2)), V(N(3)), V(N(3), M("sp", Some(N(4)))))

      versions.sorted should be (expected)

    }

  }

}
