package helpers
import geometry.Vector
import processing.core.{PApplet, PImage}

import scala.reflect.io.Directory

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

    val nextStillIdx: Int =
      stillsDir
        .list
        .map(_.name)
        .filter(_ endsWith ".jpg")
        .map(_.filter(_.isDigit).mkString)
        .map(_.toInt)
        .maxOption
        .getOrElse(0) + 1

    // TODO first we should check if the new frame is gonna just be the same
    //  as one of the ones we've already saved and show a warning instead of saving.
    //  We can do this using the comparePixels(f1, f2) method below.
    saveFrame(f"$stillsDir/$nextStillIdx%02d.png")
  }

  private def comparePixels(filename1: String, filename2: String): Boolean = {
    // LowPriorityTodo should we offload this to an async thread?
    val aImg: PImage = loadImage(filename1)
    val bImg: PImage = loadImage(filename2)
    aImg.loadPixels()
    bImg.loadPixels()
    if (aImg.pixels.length != bImg.pixels.length) {
      return false
    }
    for ((a, b) <- aImg.pixels.zip(bImg.pixels)) {
      if (a != b) {
        return false
      }
    }
    true
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
