package emulations

import helpers.{GameObject, Runner, ThreeDimPApplet}
import processing.core.PApplet.{cos, sin}

/** Created 6/6/20 12:44 PM
 *
 * Something like Wilson's target.
 *
 * Radial stripes coming out from the back. And then a few 3D rings also
 * coming out from the back. Let's get some lighting in here too.
 */
class WilsonsTarget extends ThreeDimPApplet {
  /* ******************** Add the Stripes ************************* */

  private val NumStripes: Int = 50
  private val widthRadians: Float = (2 * Math.PI / NumStripes).toFloat

  // 0 until NumStripes foreach (idx => gameObjects += new Stripe(idx = idx))

  class Stripe(idx: Int) extends GameObject {
    private val startAngle: Float = widthRadians * idx

    override def drawFromCenter(): Unit = {
      rotateZ(startAngle)
      colors.Current.update(
        fill = idx % 2 match {
          case 0 => colors.Solarized.Black
          case 1 => colors.Solarized.White
        },
        stroke = colors.Empty
      )
    }
  }

  // TODO this is not working (yet!)
  gameObjects += new GameObject {
    override def drawFromCenter(): Unit = {
      colors.Current.update(
        fill = colors.Solarized.Red,
        stroke = colors.Empty
      )
      val radius: Float = 100
      val z: Float = 0
      beginShape()
      vertex(0, 0, z)
      for (idx <- 0 until NumStripes) {
        vertex(
          cos(widthRadians * idx) * radius,
          sin(widthRadians * idx) * radius,
          z
        )
        vertex(
          cos(widthRadians * (idx + 1)) * radius,
          sin(widthRadians * (idx + 1)) * radius,
          z
        )
        vertex(0, 0, z)
      }
      endShape()
    }
  }

  /* ******************** Add the Rings ************************* */

  /* The actual 3D rings are box-shaped, which is a case of
    https://processing.org/examples/toroid.html with few key pts & segments.
   */

  // Not implemented yet....
}

object WilsonsTarget extends Runner[WilsonsTarget]
