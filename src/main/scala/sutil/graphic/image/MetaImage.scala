package sutil.graphic.image

import java.io.InputStream
import javax.imageio.ImageIO

case class MetaImage(val name: String, val format: String, val size: (Int, Int))

object MetaImage {
  
  def apply(name: String, is: InputStream): MetaImage = {
    val iis = ImageIO.createImageInputStream(is)
    try {
      val readers = ImageIO.getImageReaders(iis)
      if (readers.hasNext) {
        val reader = readers.next
        reader.setInput(iis)
        val i = reader.getMinIndex
        MetaImage(name, reader.getFormatName, (reader.getWidth(i), reader.getHeight(i)))
      } else MetaImage(name, "N/A", (0, 0))
    } finally iis.close
  }
  
}
