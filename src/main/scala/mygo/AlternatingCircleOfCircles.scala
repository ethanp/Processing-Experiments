package mygo

import helpers.{MyPApplet, Runner}
import processing.core.PApplet.{cos, sin}
import processing.core.PConstants

/** Created 6/7/20 8:18 AM
 */
class AlternatingCircleOfCircles extends MyPApplet {
  override def settings(): Unit = size(700, 700)
  override protected def drawFrame(): Unit = {
    blackBackground()
    withPushedMatrix {
      translate(width / 2, height / 2)
      val NumCircles = 50
      val angleWidth = 2 * PConstants.PI / NumCircles
      val radius = width / 5
      for (idx <- 0 until NumCircles) {
        val angle = angleWidth * idx
        colors.Current.update(
          fill = idx % 2 match {
            case 1 => colors.Solarized.Black.copy(a = 30)
            case 0 => colors.Solarized.Red.copy(a = 30)
          },
          stroke = colors.Empty
        )
        ellipseMode(PConstants.CENTER)
        ellipse(
          /*x*/ cos(angle) * radius,
          /*y*/ sin(angle) * radius,
          /*width*/ radius,
          /*height*/ radius
        )
      }
    }
  }
}

object AlternatingCircleOfCircles extends Runner[AlternatingCircleOfCircles]
