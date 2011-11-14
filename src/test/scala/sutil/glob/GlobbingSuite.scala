package sutil.glob

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import java.io.File

@RunWith(classOf[JUnitRunner])
class GlobbingSuite extends FunSuite with ShouldMatchers {

  test("Build Glob from String") {
    Glob("*.txt") should be (Glob("*.txt"))
    "*.txt".g should be (Glob("*.txt"))
  }

  test("Support * any string") {
    "*.txt".g ~ "banana.txt" should be (true)
    "*.*".g ~ "banana.pp" should be (true)
    "*.*".g ~ "banana" should be (false)
    "a*".g ~ "algo" should be (true)
    "a*".g ~ "xalgo" should be (false)
  }

  test("Support ? any char") {
    "?.txt".g ~ "a.txt" should be (true)
    "?.txt".g ~ "ma.txt" should be (false)
    "?.?".g ~ "a.x" should be (true)
    "?.?".g ~ "a.xx" should be (false)
    "a?bla*".g ~ "albla" should be (true)
    "a?bla*".g ~ "alblablabla" should be (true)
  }

  test("Support [] chars") {
    "x[ab]x".g ~ "xbx" should be (true)
    "x[ab]x".g ~ "xcx" should be (false)
    "x[!ab]x".g ~ "xcx" should be (true)
    "x[!ab]x".g ~ "xax" should be (false)
    "[ab]x".g ~ "ax" should be (true)
  }

  test("Support {} alternatives") {
    "x{ab,cd}x".g ~ "xabx" should be (true)
    "x{ab,cd}x".g ~ "xcdx" should be (true)
    "x{ab,cd}x".g ~ "xefx" should be (false)
    "x{ab,cd}x".g ~ "xzx" should be (false)
    "{ab,cd}x".g ~ "cdx" should be (true)
  }

  test("Function1[String,Boolean] support") {
    Seq("aalbla", "bla", "blo", "cla", "xlbasj") filter Glob("?l[ab]*") should be (Seq("bla", "cla", "xlbasj"))
    Seq("aalbla", "bla", "blo", "cla", "xlbasj") find Glob("*j") should be (Some("xlbasj"))
  }

}
