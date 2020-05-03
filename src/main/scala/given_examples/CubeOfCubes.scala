package given_examples
import processing.core.PConstants.PI
import processing.core.{PApplet, PConstants}

import scala.math.sin

/** Created 3/22/20 4:52 PM
 */
class CubeOfCubes extends PApplet {
  /* This app came from https://github.com/dcbriccetti/scala-lessons/blob/master/src/proc/CubeOfCubes.scala
   * And has been modified to suit my programming style.
   */

  private val BoxSpacing = 50

  override def draw(): Unit = {
    background(0)
    translate(width / 2, height / 2, -HalfWidth)
    rotateAllAxes(θ = spin(revolutionsPerSecond = 0.1))
    val range = -HalfWidth to HalfWidth by BoxSpacing take 10
    for (x <- range; y <- range; z <- range) {
      withPushedMatrix {
        translate(x, y, z)
        rotateY(spin(revolutionsPerSecond = 0.5))
        fill(
          /*r=*/ mapToGradient(offset = x),
          /*g=*/ mapToGradient(offset = y),
          /*b=*/ mapToGradient(offset = z)
        )
        box(/*size=*/ sinOverBounds(min = 5, max = 30, periodLengthSecs = 2))
      }
    }
  }

  override def settings(): Unit = fullScreen(PConstants.P3D)

  private def HalfWidth = height / 2 - 300

  /**
   * Returns a rotation angle (in radians) given a number of rotations per second,
   * using the elapsed time.
   */
  private def spin(revolutionsPerSecond: Double) =
    (revolutionsPerSecond / 1000 * PI * 2 * millis).toFloat

  /** Runs the given function within a pair of push/popMatrix calls */
  private def withPushedMatrix(fn: => Unit): Unit = {
    pushMatrix()
    fn
    popMatrix()
  }

  /**
   * Returns a value in the range min to max, varied sinusoidally over time with period length periodSecs.
   *
   * @param min              the minimum value to be returned
   * @param max              the maximum value to be returned
   * @param periodLengthSecs the length, in seconds, of one cycle of the sine curve
   * @return a value between min and max
   */
  //noinspection SameParameterValue
  private def sinOverBounds(min: Double, max: Double, periodLengthSecs: Double): Float = {
    val halfRange = (max - min) / 2
    val center = min + halfRange
    val periodLengthMs = (periodLengthSecs * 1000).toLong
    val timeInPeriodMs = millis % periodLengthMs
    val periodFraction = timeInPeriodMs.toFloat / periodLengthMs
    val radians = PI * 2 * periodFraction
    val offset = sin(radians) * halfRange
    (center + offset).toFloat
  }

  //noinspection NonAsciiCharacters
  private def rotateAllAxes(θ: Float): Unit = {
    rotateX(θ)
    rotateY(θ)
    rotateZ(θ)
  }

  private def mapToGradient(offset: Int): Float =
    PApplet.map(offset, -HalfWidth, HalfWidth, 0, 255)
}

object CubeOfCubes {
  def main(args: Array[String]): Unit = {
    PApplet.main(classOf[CubeOfCubes])
  }
}
