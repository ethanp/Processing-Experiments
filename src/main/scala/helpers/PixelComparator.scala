package helpers
import processing.core.PImage

/** Created 4/5/20 5:40 PM
 */
object PixelComparator {
  // LowPriorityTodo can we write unit test for this somehow?
  //
  //  The problem is that we probably need to have a PApplet context available
  //  to be able to create the PImages. Though I could dig into what the PApplet
  //  method actually does and maybe it calls something static? But probs not.
  def areEquivalent(
    aImg: PImage,
    bImg: PImage,
  )(
    implicit myPApplet: MyPApplet
  ): Boolean = {
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
