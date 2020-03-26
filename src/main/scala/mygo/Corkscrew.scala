package mygo
import helpers.{Runner, ThreeDimPApplet}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

/** Created 3/24/20 10:45 PM
 */
class Corkscrew extends ThreeDimPApplet {

  // TODO bennett has two notes
  //  1: The screen should fill in shorter, like within 5 seconds
  //  2: Once the screen fills it should stop emitting circles, and then
  //     once the screen is empty start again.

  private val corkscrewBalls = mutable.ArrayBuffer.empty[CorkscrewBall]
  private val periodically = Every(300 milliseconds)

  override def draw(): Unit = {
    darkBackground()
    periodically run (corkscrewBalls += new CorkscrewBall)
    corkscrewBalls foreach (_.draw())
    corkscrewBalls foreach (_.update())
  }

  class CorkscrewBall {
    private var curZ: Int = 0
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
      curZ += 1
    }
  }
}

object Corkscrew extends Runner {
  override def pAppletClass: Class[_] = classOf[Corkscrew]
}
