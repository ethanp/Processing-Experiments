package mygo
import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {

  // Create the rings at different z-values.
  gameObjects ++= (0 until 30 map (new Ring(_)))

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

  private val defaultVantagePoint = geometry.Vector(
    x = width / 2,
    y = height / 2,

    // TODO wtf is this? I found that it is the default here:
    //  https://processing.org/reference/camera_.html
    z = (height / 2.0) / math.tan(math.Pi * 30.0 / 180.0)
  )

  //noinspection RedundantDefaultArgument
  private val defaultFocusPoint = geometry.Vector(
    x = width / 2.0,
    y = height / 2.0,
    z = 0
  )

  override def moveCamera(): Unit = {
    // Recall: GameObjects are drawn fromTheCenter().

    // This is the default anyway.
    lookAt(
      vantagePoint = defaultVantagePoint,
      focusPoint = defaultFocusPoint,
    )
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
