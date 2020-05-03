package mygo
import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {

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

  class Ring(idx: Int) extends GameObject {
    override def drawFromCenter(): Unit = {
      colors.Current.update(
        fill = colors.Hsb(h = (frameCount + idx) / 4f % 100),
        stroke = colors.Solarized.White
      )
      translate(
        x = 0,
        y = idx * 5,
        z = 0 // For some reason this appears to have no effect?
      )
      rotateY(geometry degreesToRadians (degrees = frameCount))
      ellipseMode(PConstants.RADIUS)
      // Center and radius
      ellipse(0, 0, 2 * idx, 2 * idx)
    }
  }
}

object RingTunnel extends Runner {
  override def pAppletClass: Class[_] = classOf[RingTunnel]
}
