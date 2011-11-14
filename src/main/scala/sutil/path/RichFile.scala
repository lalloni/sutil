package sutil.path

import java.io.File

import scala.Array.canBuildFrom

import sutil.glob.Glob

case class RichFile(val file: File) {
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
  def /(glob: Glob): Seq[File] = files.flatMap(_ / glob)
  def /(name: String): Seq[File] = files.map(_ / name)
}
