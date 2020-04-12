package mygo
import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {
  for (i <- 0 until 30) gameObjects += new Ring(i)

  class Ring(i: Int) extends GameObject {
    override def draw(): Unit = {
      colors.Current.update(
        fill = colors.Solarized.Orange,
        stroke = colors.Solarized.White
      )
      translate(0, 0, i * 10)
      ellipseMode(PConstants.RADIUS)
      // Center and radius
      ellipse(0, 0, i * 10, i * 10)
    }
  }
}

object RingTunnel extends Runner {
  override def pAppletClass: Class[_] = classOf[RingTunnel]
}
