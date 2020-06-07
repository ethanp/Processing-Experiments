package helpers
import processing.core.PImage

import scala.reflect.io.Directory

/** Created 4/5/20 5:49 PM
 */
object ImageSaver {
  def save(className: String)(implicit myPApplet: MyPApplet): Unit = {

    val stillsDir: Directory =
      reflect.io
        .Directory(s"stills/$className")
        .createDirectory()

    val preExistingFiles = stillsDir.list
    val nextStillIdx: Int =
      preExistingFiles
        .map(_.name)
        .filter(Seq(".jpg", ".png") exists _.endsWith)
        .map(_.filter(_.isDigit).mkString.toInt)
        .maxOption
        .getOrElse(0) + 1

    val newFileName = f"$stillsDir/$nextStillIdx%02d.png"
    myPApplet.saveFrame(newFileName)
    deduplicate(preExistingFiles.map(_.path), newFileName)
  }

  // TODO this is clearly not working.
  //  One can see this by clicking twice on the same image.
  //   Expected: Only one click actually saves.
  //   Actual:   Both save.
  private def deduplicate(
    bank: Iterator[String],
    newImage: String
  )(
    implicit myPApplet: MyPApplet
  ): Unit = {
    // Thought: we could make this more async in several different ways and that
    //  might make it faster. But I haven't had any performance issues with it.
    val newImg: PImage = myPApplet.loadImage(newImage)
    for (file <- bank) {
      if (PixelComparator.areEquivalent(aImg = newImg, bImg = myPApplet.loadImage(file))) {
        println("WARNING: Deleting new snapshot as duplicate")
        reflect.io.File(newImage).delete()
        println("WARNING: Snapshot was deleted by deduplicator")
        return
      }
    }
  }
}
