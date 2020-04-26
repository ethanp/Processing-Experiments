package mygo
import animation.Every
import colors.Hsb
import helpers.{Runner, ThreeDimPApplet}

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

/** Created 3/24/20 10:45 PM
 */
class Gummiworms extends ThreeDimPApplet {

  // LowPriorityTodo this one isn't working too well these days; not sure what's up

  private val atGenerationRate = Every(300 milliseconds)
  private val onOff = Every(7 seconds)
  private var isGenerating = true
  private var increment = 1f

  override def drawFrame(): Unit = {
    onOff run {
      isGenerating = !isGenerating
      if (isGenerating) increment = Random.nextFloat() * 2
    }
    if (isGenerating) atGenerationRate run {
      gameObjects += new CorkscrewBall
    }
    super.draw()
  }

  class CorkscrewBall extends GameObject {
    private var curZ: Float = 0
    private var curRotationRadians: Float = 0f
    override def draw(): Unit = {
      fromTheCenter {
        rotateX(curRotationRadians)
        rotateY(curRotationRadians)
        Hsb(h = curZ % 81, b = 50, a = 60).fill()
        Hsb(h = curZ % 100, a = 70).stroke()
        translate(
          x = geometry.sinRad(radians = curZ / 100.0) * 100,
          y = geometry.cosRad(radians = curZ / 100.0) * 100,
          z = curZ
        )
        sphere(55)
      }
    }

    override def tick(): Unit = {
      curRotationRadians += 0.005f
      curZ += increment
    }
  }
}

object Gummiworms extends Runner {
  override def pAppletClass: Class[_] = classOf[Gummiworms]
}
