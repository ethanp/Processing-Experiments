package mygo
import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {

  // Create the rings at different z-values.
  gameObjects ++= (0 until 30 map (new Ring(_)))

  // TODO move this elsewhere.
  def lookAt(
    vantagePoint: geometry.Vector,
    focusPoint: geometry.Vector,
    upDir: geometry.Vector
  ): Unit = {
    camera(
      vantagePoint.x, vantagePoint.y, vantagePoint.z,
      focusPoint.x, focusPoint.y, focusPoint.z,
      upDir.x, upDir.y, upDir.z
    )
  }

  override def moveCamera(): Unit = {
    lookAt(
      vantagePoint = geometry.Vector.Z * -10,
      focusPoint = geometry.Vector.Zero,
      upDir = geometry.Vector.Y
    )
    //    rotateY(frameCount)
  }

  class Ring(idx: Int) extends GameObject {
    override def draw(): Unit = {
      colors.Current.update(
        fill = colors.Hsb(h = frameCount / 4f % 100),
        stroke = colors.Solarized.White
      )
      translate(
        x = 0,
        y = 0,
        z = idx * 10
      )
      ellipseMode(PConstants.RADIUS)
      // Center and radius
      ellipse(0, 0, idx * 10, idx * 10)
    }
  }
}

object RingTunnel extends Runner {
  override def pAppletClass: Class[_] = classOf[RingTunnel]
}
