package mygo
import geometry.V
import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {

  override protected val gifLength = 120

  // Create the rings at different z-values.
  gameObjects ++= 0 until 30 map (new Ring(_))

  // TODO move this to the base class to create those sliders that move the camera around.
  def lookAt(
    vantagePoint: geometry.Vector,
    focusPoint: geometry.Vector,
    upDir: geometry.Vector = geometry.Vector.Y
  ): Unit = {
    // NB: the camera() method is based on gluLookAt, which has a nice visual description:
    //
    //  https://stackoverflow.com/a/5721110/1959155
    //
    // In particular, note that the up-dir actually used will be "tilted forward or
    // backward" based on the vector from vantage point to focus point.
    camera(
      vantagePoint.x, vantagePoint.y, vantagePoint.z,
      focusPoint.x, focusPoint.y, focusPoint.z,
      upDir.x, upDir.y, upDir.z
    )
  }

  /* TODO this part is not working at all.
  private val vantagePoint = geometry.Vector(
    x = width / 2.0,
    y = height / 2.0,
    z = -1000
  )

  //noinspection RedundantDefaultArgument
  private val focusPoint = geometry.Vector(
    x = width / 2.0,
    y = height / 2.0,
    z = 0
  )

    override def moveCamera(): Unit = lookAt(
      vantagePoint = vantagePoint,
      focusPoint = focusPoint,
    )
  */

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
        fill = colors.Hsb(h = (frameCount + idx * 3) / 4f % 100),
        stroke = colors.Empty
      )
      translate(
        x = 0,
        y = 0,
        z = -idx * 50
      )
      ellipse(
        center = V(x = 0, y = 0),
        radii = V(x = 500, y = 500)
      )
    }
  }
}

object RingTunnel extends Runner {
  override def pAppletClass: Class[_] = classOf[RingTunnel]
}
