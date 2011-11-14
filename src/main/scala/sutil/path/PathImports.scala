package sutil.path

import java.io.File

trait PathImports {
  
  implicit def fileAsRichFile(file: File): RichFile = new RichFile(file)
  
  implicit def richFileAsFile(file: RichFile): File = file.file
  
  implicit def filesAsRichFiles(files: Seq[File]) = new RichFiles(files)
  
  implicit def richFilesAsFiles(files: RichFiles) = files.files
  
}
