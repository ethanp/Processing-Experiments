package helpers
import processing.core.PConstants
import processing.event.MouseEvent

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration

/** Created 3/25/20 12:08 AM
 */
//noinspection TypeAnnotation
trait ThreeDimPApplet extends MyPApplet {

  val gameObjects = mutable.ArrayBuffer.empty[GameObject]

  trait GameObject {
    def draw(): Unit = ()
    def tick(): Unit = ()
  }

  def moveCamera(): Unit = ()

  /** Default implementation. Override as desired. */
  override def drawFrame(): Unit = {
    blackBackground()
    moveCamera()
    for (gameObj <- gameObjects) {
      fromTheCenter {
        gameObj.draw()
      }
    }
    gameObjects.foreach(_.tick())
    gifSaver.addFrame(frameCount, saveFrame)
  }

  override def settings(): Unit = {
    pixelDensity = 2 // This is better when displaying on retina
    fullScreen(PConstants.P3D)
  }

  override def setup(): Unit = {
    // NB: These hints trade performance for visual quality.
    //  https://processing.org/reference/hint_.html
    hint(PConstants.DISABLE_DEPTH_TEST)
    hint(PConstants.ENABLE_STROKE_PERSPECTIVE)
  }

  // For the scala compiler, by 'lifting' this method into this class,
  // it disambiguates which overload should be called in the (Float, Float, Float)
  // case (i.e. it should be THIS overload).
  final override def translate(x: Float, y: Float, z: Float): Unit =
    super.translate(x, y, z)

  def translate(x: Double, y: Double, z: Double): Unit =
    ThreeDimPApplet.super.translate(x.toFloat, y.toFloat, z.toFloat)

  // TODO this should be in its own file and get passed the context implicitly, like the
  //  other utilities.
  case class Rotation(period: FiniteDuration) extends GameObject {
    private var rotationFraction = 0D
    val millerPerPeriod = 1.0 / period.toMillis
    private var lastTime = millis()
    gameObjects += this

    override def tick(): Unit = {
      val elapsedTime = millis() - lastTime
      // LowPriorityTodo is this right? can't think right now.
      //  It should "work" (rotate) anyhow, just not necessarily at the desired period.
      rotationFraction += elapsedTime.toDouble / period.toMillis
      lastTime = millis()
    }

    def curRadians(): Float = angleFractionToRadians(rotationFraction)

    private def angleFractionToRadians(angleFraction: Double): Float =
      ((angleFraction - (math floor angleFraction)) * math.Pi * 2).toFloat
  }

  // TODO super useful feature:
  //  Click and drag to move the camera across the different axes.
  //  Should use the [[VFader]] class I made.
  val cameraLoc = geometry.Vector.Z * -10

  var mouseDown = false
  override def mousePressed(event: MouseEvent): Unit = {
    event.getButton match {
      case PConstants.LEFT =>
        mouseDown = true
      case _ => /* ignore. */
    }
  }

  override def mouseDragged(event: MouseEvent): Unit = {
  }
  override def mouseReleased(event: MouseEvent): Unit = {
  }
}
