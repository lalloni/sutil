package sutil.path

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import sutil.glob.Glob

@RunWith(classOf[JUnitRunner])
class RichFileSuite extends FunSuite with ShouldMatchers {

  test("name construction") {
    val f = new File("xxx.txt")
    "xxx" / "yyy" / "xx.txt" should be (new File("xxx/yyy/xx.txt"))
    f :+ ".gz" should be (new File("xxx.txt.gz"))
    "0_" +: f should be (new File("0_xxx.txt"))
  }

  test("file globbing") {
    val src = new File("src/main/scala/sutil")
    src.ls should not be 'empty
    src.lsr should contain (src.ls(0))
    src / Glob("*at*") should be (Seq(new File("src/main/scala/sutil/path"), new File("src/main/scala/sutil/math")))
  }

}
