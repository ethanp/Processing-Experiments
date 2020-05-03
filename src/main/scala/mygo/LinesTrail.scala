package mygo
import colors.Rgb
import helpers.{MyPApplet, Runner}
import processing.core.PConstants

/** Created 3/29/20 11:24 AM
 */
class LinesTrail extends MyPApplet {
  override def drawFrame(): Unit = {
    blackBackground()
    translate(width / 2, height / 2)
    withPushedMatrix {
      strokeWeight(2)
      Rgb(10, 90, 10, 90).stroke()
      for (i <- -1000 to 1000 by 10)
        line(i, height / 2, 0, -height / 2)
      Rgb(90, 10, 10, 90).stroke()
      for (i <- -1000 to 1000 by 10)
        line(-i, -height / 2, 0, height / 2)
    }
  }

  override def settings(): Unit = {
    pixelDensity = 2 // This is better when displaying on retina
    fullScreen(PConstants.P2D)
  }
}

object LinesTrail extends Runner {
  override def pAppletClass: Class[_] = classOf[LinesTrail]
}
