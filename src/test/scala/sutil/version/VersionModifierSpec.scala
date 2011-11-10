package sutil.version

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionModifierSpec extends Spec with ShouldMatchers {

  describe("A VersionModifier") {

    it("should sort appropriately") {
      val mods = Seq[VersionModifier]("Final", "ALPHA3", "snapshot", "cr1", "beta", "beta2")
      println(mods)
      println(mods.sorted)
      mods.sorted should be (Seq(M("snapshot"), M("ALPHA", V(3)), M("beta"), M("beta", V(2)), M("cr", V(1)), M("Final")))
    }

  }

}