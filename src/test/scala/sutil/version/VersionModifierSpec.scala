package sutil.version

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionModifierSpec extends Spec with ShouldMatchers {

  describe("A VersionModifier") {

    it("should sort appropriately") {
      Seq[VersionModifier]("beta2", "Final", "ALPHA3", "sp2", "snapshot", "cr1", "beta")
        .sorted should be (Seq[VersionModifier]("snapshot", "ALPHA3", "beta", "beta2", "cr1", "Final", "sp2"))
    }

  }

}
