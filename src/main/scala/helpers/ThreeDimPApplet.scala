package helpers
import processing.core.{PApplet, PConstants}

import scala.concurrent.duration.FiniteDuration

/** Created 3/25/20 12:08 AM
 */
trait ThreeDimPApplet extends PApplet {

  override def settings(): Unit = fullScreen(PConstants.P3D)

  // For the scala compiler, by 'lifting' this method into this class,
  // it disambiguates which overload should be called in the (Float, Float, Float)
  // case (i.e. it should be THIS overload).
  final override def translate(x: Float, y: Float, z: Float): Unit =
    super.translate(x, y, z)

  def translate(x: Double, y: Double, z: Double): Unit =
    ThreeDimPApplet.super.translate(x.toFloat, y.toFloat, z.toFloat)

  def darkBackground(): Unit = background(15f)

  final protected def fromTheCenter(v: => Unit): Unit = {
    pushMatrix()
    translate(width / 2, height / 2, 0)
    v
    popMatrix()
  }

  sealed trait Color {
    def stroke(): Unit
    def fill(): Unit
    final def strokeAndFill(): Unit = { stroke(); fill() }
  }

  case class Hue(h: Float) extends Color {
    override def stroke(): Unit = {
      colorMode(PConstants.HSB, 100)
      ThreeDimPApplet.this.stroke(h, 100, 100)
    }
    override def fill(): Unit = {
      colorMode(PConstants.HSB, 100)
      ThreeDimPApplet.this.fill(h, 100, 100)
    }
  }

  object Green extends Rgb(0, 255, 0)
  object Black extends Rgb(0, 0, 0)

  sealed case class Rgb(r: Float, g: Float, b: Float) extends Color {
    override def stroke(): Unit = {
      colorMode(PConstants.RGB)
      ThreeDimPApplet.this.stroke(r, g, b)
    }
    override def fill(): Unit = {
      colorMode(PConstants.RGB)
      ThreeDimPApplet.this.fill(r, g, b)
    }
  }

  case class Every(duration: FiniteDuration) {
    private var lastTime = 0L
    def run(block: => Unit): Unit = {
      if (millis() / duration.toMillis > lastTime) {
        block
        lastTime = millis() / duration.toMillis
      }
    }
  }
}
