package framework_experiments
import helpers.{MyPApplet, Runner}
import processing.core.PImage

/** Created 4/5/20 4:53 PM
 */
class CompareSavedImages extends MyPApplet {
  val OverallSide = 512 // = 2 ** 9

  override def settings(): Unit = size(OverallSide, OverallSide)

  override def draw(): Unit = {
    val filename1 = "PillowCase/00.jpg"
    val filename2 = "PillowCase/01.jpg"
    val filename3 = "PillowCase/02.jpg"
    println(compare(filename1, filename2) + " should be true")
    println(compare(filename2, filename3) + " should be false")
  }
  def compare(filename1: String, filename2: String): Boolean = {
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
}

object CompareSavedImages extends Runner {
  override def pAppletClass: Class[_] = classOf[CompareSavedImages]
}
