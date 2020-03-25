package mygo
import processing.core.{PApplet, PConstants}

import scala.collection.mutable

/** Created 3/24/20 10:45 PM
 */
class Corkscrew extends ThreeDimPApplet {

  private val corkscrewBalls = mutable.ArrayBuffer.empty[CorkscrewBall]
  private var lastCreation = 0

  override def draw(): Unit = {
    background(/*greyBrightness=*/ 15) // Clear the screen darkly
    if (millis() / 1000 > lastCreation) {
      corkscrewBalls += new CorkscrewBall
      lastCreation = millis() / 1000
    }
    corkscrewBalls foreach (_.draw())
  }

  class CorkscrewBall {
    private var curZ: Int = 0
    private var curRotationRadians: Float = 0f
    def draw(): Unit = {
      fromTheCenter {
        rotateX(curRotationRadians)
        rotateY(curRotationRadians)
        curRotationRadians += 0.005f

        Black.fill()
        Hue(curZ % 100).stroke()
        translate(
          x = math.sin(curZ / 100.0) * 100,
          y = math.cos(curZ / 100.0) * 100,
          z = curZ
        )
        curZ += 1
        sphere(55)
      }
    }
  }
}

object Corkscrew {
  def main(args: Array[String]): Unit =
    PApplet.main(classOf[Corkscrew])
}

trait Color {
  def stroke(): Unit
  def fill(): Unit
  final def strokeAndFill(): Unit = { stroke(); fill() }
}

trait ThreeDimPApplet extends PApplet {

  override def settings(): Unit = fullScreen(PConstants.P3D)

  // For the scala compiler, by 'lifting' this method into this class,
  // it disambiguates which overload should be called in the (Float, Float, Float)
  // case (i.e. it should be THIS overload).
  final override def translate(x: Float, y: Float, z: Float): Unit =
    super.translate(x, y, z)

  def translate(x: Double, y: Double, z: Double): Unit =
    ThreeDimPApplet.super.translate(x.toFloat, y.toFloat, z.toFloat)

  final protected def fromTheCenter(v: => Unit): Unit = {
    pushMatrix()
    translate(width / 2, height / 2, 0)
    v
    popMatrix()
  }

  sealed case class Hue(h: Float) extends Color {
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
}
