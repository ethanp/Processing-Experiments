package mygo
import helpers.{Runner, ThreeDimPApplet}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

/** Created 3/24/20 10:45 PM
 */
class Gummiworms extends ThreeDimPApplet {

  private val corkscrewBalls = mutable.ArrayBuffer.empty[CorkscrewBall]
  private val periodically = Every(300 milliseconds)
  private val onOff = Every(7 seconds)
  private var on = true
  private var increment = 1f

  override def draw(): Unit = {
    darkBackground()
    onOff run {
      on = !on
      if (on) increment = Random.nextFloat() * 2
    }
    if (on) periodically run (corkscrewBalls += new CorkscrewBall)
    corkscrewBalls foreach (_.draw())
    corkscrewBalls foreach (_.update())
  }

  class CorkscrewBall {
    private var curZ: Float = 0
    private var curRotationRadians: Float = 0f
    def draw(): Unit = {
      fromTheCenter {
        rotateX(curRotationRadians)
        rotateY(curRotationRadians)
        Black.fill()
        Hue(curZ % 100).stroke()
        translate(
          x = math.sin(curZ / 100.0) * 100,
          y = math.cos(curZ / 100.0) * 100,
          z = curZ
        )
        sphere(55)
      }
    }

    def update(): Unit = {
      curRotationRadians += 0.005f
      curZ += increment
    }
  }
}

object Gummiworms extends Runner {
  override def pAppletClass: Class[_] = classOf[Gummiworms]
}
