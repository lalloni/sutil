package sutil

import java.io.File

package object fs {

  implicit def file2richFile(file: File): RichFile = new RichFile(file)
  implicit def richFile2file(file: RichFile): File = file.file
  implicit def files2RichFiles(files: Seq[File]) = new RichFiles(files)
  implicit def richFiles2Files(files: RichFiles) = files.files

}