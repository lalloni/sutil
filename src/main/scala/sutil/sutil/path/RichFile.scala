package sutil.path

import java.io.File

trait RichFileImports {
  implicit def fileAsRichFile(file: File): RichFile = new RichFile(file)
  implicit def richFileAsFile(file: RichFile): File = file.file
  implicit def filesAsRichFiles(files: Seq[File]) = new RichFiles(files)
  implicit def richFilesAsFiles(files: RichFiles) = files.files
}

object RichFile extends RichFileImports

case class RichFile(val file: File) {
  import RichFile._
  def /(name: String): File = new File(file, name)
  def /(glob: Glob): Seq[File] = ls.filter(f ⇒ glob.matches(f.getName))
  def :+(suffix: String): File = new File(file.getPath + suffix)
  def +:(prefix: String): File = new File(file.getParent, prefix + file.getName)
  def ls: Seq[File] = Option(file.list).getOrElse(Array.empty).map(file / _).toSeq
  def lsr: Seq[File] = ls.flatMap(name ⇒ name +: name.lsr)
  def parent: Option[File] = Option(file.getParentFile)
  override def toString = file.toString
  def toFile = file
}

class RichFiles(val files: Seq[File]) {
  import RichFile._
  def /(glob: Glob): Seq[File] = files.flatMap(_ / glob)
  def /(name: String): Seq[File] = files.map(_ / name)
}
