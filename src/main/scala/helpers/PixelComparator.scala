package helpers
import processing.core.PImage

/** Created 4/5/20 5:40 PM
 */
object PixelComparator {
  // We'd probably need to have a PApplet context available to be able to create the
  // PImages in a unit test...however whenever I developed this I did a trial run to
  // ensure it did work: https://github.com/ethanp/Processing-Experiments/commit/60367e9c9970bd90f3f48daaa827cc61192b3237
  def areEquivalent(aImg: PImage, bImg: PImage)(implicit myPApplet: MyPApplet): Boolean = {
    aImg.loadPixels()
    bImg.loadPixels()

    if (aImg.pixels.length != bImg.pixels.length)
      return false

    // NB: This is actually efficient since zip is lazy and exists is eager.
    !aImg.pixels.zip(bImg.pixels).exists { case (a, b) => a != b }
  }
}
