package mygo
import animation.{Every, SlowFrameRate}
import colors.Hsb
import helpers.{GameObject, Runner, ThreeDimPApplet}
import processing.core.PApplet.{cos, sin}

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

/** Created 3/24/20 10:45 PM
 */
class Gummiworms extends ThreeDimPApplet {

  // LowPriorityTodo this one doesn't work anymore.

  private val atGenerationRate = SlowFrameRate(framesPerSec = 3)
  private val onOff = new Every(7 seconds)
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
  }

  class CorkscrewBall extends GameObject {
    private var curZ: Float = 0
    private var curRotationRadians: Float = 0f
    override def drawFromCenter(): Unit = {
      rotateX(curRotationRadians)
      rotateY(curRotationRadians)
      Hsb(h = curZ % 81, b = 50, a = 60).fill()
      Hsb(h = curZ % 100, a = 70).stroke()
      translate(
        x = sin(curZ / 100f) * 100,
        y = cos(curZ / 100f) * 100,
        z = curZ
      )
      sphere(55)
    }

    override def tick(): Unit = {
      curRotationRadians += 0.005f
      curZ += increment
    }
  }
}

object Gummiworms extends Runner[Gummiworms]
