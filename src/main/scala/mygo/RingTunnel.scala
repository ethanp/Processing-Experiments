package mygo
import helpers.{GifSaver, Runner, ThreeDimPApplet}
import processing.core.PConstants

/** Created 4/12/20 1:00 PM
 */
class RingTunnel extends ThreeDimPApplet {
  gameObjects ++= (0 until 30 map (new Ring(_)))

  // TODO this should probably live in [[ThreeDimPApplet]]
  private val gifSaver = new GifSaver(
    className = getClass.getSimpleName,
    gifLength = GifSaver.Disabled
    // gifLength = 25
  )
  override def draw(): Unit = {
    super.draw()
    gifSaver.addFrame(frameCount, saveFrame)
  }

  // TODO move this elsewhere.
  def lookAt(vantagePoint: geometry.Vector, focusPoint: geometry.Vector, upDir: geometry.Vector): Unit = {
    camera(
      vantagePoint.x, vantagePoint.y, vantagePoint.z,
      focusPoint.x, focusPoint.y, focusPoint.z,
      upDir.x, upDir.y, upDir.z
    )
  }

  override def moveCamera(): Unit = {
    lookAt(
      vantagePoint = geometry.Vector(
        x = 30,
        y = 0,
        z = -30,
      ),
      focusPoint = geometry.Vector(
        x = 0,
        y = 0,
        z = 30,
      ),
      upDir = geometry.Vector(
        x = 0,
        y = 0,
        z = 1,
      )
    )
    rotateY(frameCount)
  }

  class Ring(i: Int) extends GameObject {
    override def draw(): Unit = {
      colors.Current.update(
        fill = colors.Hsb(h = frameCount / 4f % 100),
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
