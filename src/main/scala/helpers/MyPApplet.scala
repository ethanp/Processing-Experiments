package helpers
import geometry.Vector
import processing.core.PApplet

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

  override def mouseClicked(): Unit = {
    val className = getClass.getSimpleName
    val idx: Int =
      reflect.io.Directory(className)
        .list
        .map(_.name)
        .filter(_ endsWith ".jpg")
        .toSeq
        .maxOption
      match {
        case None => 0
        case Some(path) =>
          "(\\d+)\\.jpg$".r findFirstMatchIn path match {
            case None => throw new Error(s"WARNING: Couldn't parse file path: $path")
            case Some(found) => found.group(1).toInt + 1
          }
      }

    // TODO first we should check if the new frame is gonna just be the same
    //  as one of the ones we've already saved and show a warning instead of saving.
    //  One easy way would be comparing the pixel values or something.
    //  It may be best to offload all this work to a separate thread than the one that
    //  calls draw() (which I've verified /is/ what calls this mouseClicked() callback).
    saveFrame(f"$className/$idx%02d.png")
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

  lazy val framesDir: Directory = {
    val gifsDir =
      reflect.io
        .Directory(s"${ getClass.getSimpleName }/gifs")
        .createDirectory()

    val nextInt: Int = gifsDir
      .list
      .map(_.name)
      .filter(_ contains "Frames")
      .flatMap("(\\d+)".r findFirstMatchIn _)
      .map(_ group 1)
      .map(_.toInt)
      .maxOption
      .getOrElse(0) + 1
    reflect.io
      .Directory(s"${ getClass.getSimpleName }/gifs/Frames-$nextInt")
      .createDirectory()
  }

  final protected def overwritePngsForGifIfEnabled(): Unit = {
    val FramesToSave = 0 // zero means gif-saving is disabled.
    // NB: The first `frameCount` is 1 apparently.
    if (frameCount > FramesToSave) return
    val frameFile = reflect.io.File(framesDir / s"Frame-$frameCount.png")
    println(s"saving frame $frameCount")
    saveFrame(frameFile.toString)
    if (frameCount == FramesToSave - 1) {
      import sys.process._
      val output = s"convert -delay 20 $framesDir/*.png -loop 0 $framesDir/pillow-case.gif".!!
      println(s"output was $output")
    }
  }
}
