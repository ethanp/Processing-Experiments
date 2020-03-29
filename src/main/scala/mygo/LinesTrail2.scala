package mygo

import helpers.{MyPApplet, Runner}
import processing.core.PConstants

/** Created 3/29/20 11:24 AM
 */
class LinesTrail2 extends MyPApplet {
  override def draw(): Unit = {
    darkBackground()
    fromTheCenter {
      strokeWeight(4)
      White.stroke()
      val lineLeft = -width / 3f
      val lineRight = -lineLeft
      line(lineLeft, 0, lineRight, 0)
      val lineWidth: Float = lineRight - lineLeft
      val steps = 100
      val stepWidth = lineWidth / steps
      for (step <- 0 to steps) {
        val x = lineLeft + step * stepWidth
        val y = sin(x / 100f) * height / 3f
        Green.stroke() // TODO lerp this color
        line(x, 0, x, y)
      }
    }
  }

  override def settings(): Unit = {
    pixelDensity = 2 // This is better when displaying on retina
    fullScreen(PConstants.P2D)
  }
}

object LinesTrail2 extends Runner {
  override def pAppletClass: Class[_] = classOf[LinesTrail2]
}
