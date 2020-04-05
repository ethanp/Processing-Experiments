package mygo

import colors.Solarized
import helpers.{MyPApplet, Runner}
import processing.core.PConstants

/** Created 3/29/20 11:24 AM
 */
class LinesTrail2 extends MyPApplet {
  override def draw(): Unit = {
    blackBackground()
    fromTheCenter {
      strokeWeight(5)
      Solarized.White.stroke()
      val lineLeft = -pixelWidth / 6f
      val lineRight = -lineLeft
      line(lineLeft, 0, lineRight, 0)
      val lineWidth: Float = lineRight - lineLeft
      val steps = 1000
      val stepWidth = lineWidth / steps
      for (step <- 0 to steps) {
        Solarized.Yellow.stroke() // LowPriorityTodo lerp this color
        val x = lineLeft + step * stepWidth
        val rate = 110f
        val scale = pixelHeight / 10f
        val y = (geometry.sinRad(x / rate) - 1) * scale
        val y2 = (geometry.cosRad(x / rate) + 1) * scale
        line(x, 0, x, y)
        line(x, 0, x, y2)
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
