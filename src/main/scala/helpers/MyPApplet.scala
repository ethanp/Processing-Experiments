package helpers
import geometry.Vector
import processing.core.PApplet

import scala.reflect.io.{Directory, Path}

/** Created 3/29/20 11:28 AM
 */
trait MyPApplet extends PApplet {
  implicit val app: MyPApplet = this


  /* ************** RENAMINGS ***************/

  def blackBackground(): Unit = background(0f)

  final def is3D: Boolean = sketchRenderer() contains "3D"

  def mouse: Vector = geometry.Vector(mouseX, mouseY)


  /* ************** SAVING ***************/

  // TODO the implementation here should be moved to a different class.
  //  Though we should still leave the mouseClicked() hook here.
  override def mouseClicked(): Unit = {
    val className = getClass.getSimpleName

    val stillsDir: Directory =
      reflect.io
        .Directory(s"stills/$className")
        .createDirectory()

    val preExistingFiles = stillsDir.list
    val nextStillIdx: Int =
      preExistingFiles
        .map(_.name)
        .filter(_ endsWith ".jpg")
        .map(_.filter(_.isDigit).mkString)
        .map(_.toInt)
        .maxOption
        .getOrElse(0) + 1

    val newFileName = f"$stillsDir/$nextStillIdx%02d.png"
    saveFrame(newFileName)
    deduplicateImages(preExistingFiles, newFileName)
  }

  private def deduplicateImages(preExistingFiles: Iterator[Path], newFileName: String): Unit = {
    // Thought: we could make this more async in several different ways and that
    //  might make it faster. But I haven't had any performance issues with it.
    val newImg = loadImage(newFileName)
    for (file <- preExistingFiles) {
      if (PixelComparator.areEquivalent(aImg = newImg, bImg = loadImage(file.path))) {
        reflect.io.File(newFileName).delete()
        return
      }
    }
  }


  /* ************** GEOMETRY ***************/

  final def withPushedMatrix(block: => Unit): Unit = {
    pushMatrix()
    block
    popMatrix()
  }

  final protected def fromTheCenter(block: => Unit): Unit = {
    withPushedMatrix {
      if (is3D) translate(width / 2, height / 2, 0)
      else translate(width / 2, height / 2)
      block
    }
  }
}
