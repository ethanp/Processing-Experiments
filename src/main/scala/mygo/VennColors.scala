package mygo

import helpers.{Runner, ThreeDimPApplet}
import processing.core.PConstants

import scala.concurrent.duration._
import scala.language.postfixOps

/** Created 3/24/20 10:45 PM
 */
class VennColors extends ThreeDimPApplet {

  private val intensity = 60

  gameObjects ++= Seq(
    StaticCircle(color = Rgb(intensity, 0, 0, intensity), givenDegrees = 0),
    StaticCircle(color = Rgb(0, intensity, 0, intensity), givenDegrees = 120),
    StaticCircle(color = Rgb(0, 0, intensity, intensity), givenDegrees = 240),
    SpinningCircle(color = Rgb(intensity, intensity, intensity, intensity))
  )

  trait VennCircle extends GameObject {

    def curRadians: Double
    def color: Color

    private val overlap = 100
    private val topLeft = PVector(x = -overlap, y = -overlap)
    private val Width = 400
    private val Height = 400

    override def draw(): Unit = {
      fromTheCenter {
        rotateZ(curRadians.toFloat)
        color.fill()
        White.stroke()
        ellipseMode(PConstants.CORNER)
        ellipse(topLeft.x, topLeft.y, Width, Height)
      }
    }
  }

  case class StaticCircle(color: Color, givenDegrees: Double) extends VennCircle {
    override def curRadians: Double = math.toRadians(givenDegrees)
  }

  case class SpinningCircle(color: Color) extends VennCircle {
    private val rotation = Rotation(period = 3 seconds)
    override def curRadians: Double = rotation.curRadians()
  }
}

object VennColors extends Runner {
  override def pAppletClass: Class[_] = classOf[VennColors]
}
