package sutil.glob

class GlobBuilder(s: String) {
  def g = Glob(s)
}

trait GlobImports {

  implicit def asGlobBuilder(s: String): GlobBuilder =
    new GlobBuilder(s)

}
