package mygo
import geometry.V
import helpers.{GameObject, Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {

  // Create the rings at different z-values.
  gameObjects ++= 0 until 100 map (new Ring(_))

  // For creating the gif:
  //  override val gifLength = 100
  //  override def settings(): Unit = size(500, 500, PConstants.P3)

  override protected def vantagePoint = geometry.Vector(
    x = 0,
    y = 0,
    z = -width * 16 + frameCount * 5
  )

  // NB: Ellipses (and other 2D shapes when in 3D mode)
  // are only drawn on the x-y plane (at z=0).
  // (At least that seems to be the case, plus it makes sense.)
  def ellipse(center: geometry.Vector, radii: geometry.Vector) {
    ellipseMode(PConstants.RADIUS)
    ellipse(center.x, center.y, radii.x, radii.y)
  }

  class Ring(idx: Int) extends GameObject {
    override def drawFromCenter(): Unit = {
      colors.Current.update(
        fill = colors.Empty,
        stroke = colors.Hsb(h = (frameCount + idx) / 4f % 100),
        strokeWeight = 10
      )
      translate(
        x = 0,
        y = 0,
        z = -idx * width / 6
      )
      ellipse(
        center = V(x = 0, y = 0),
        radii = V(x = width / 2, y = width / 2)
      )
    }
  }
}

object RingTunnel extends Runner[RingTunnel]
