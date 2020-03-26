package helpers
import processing.core.{PApplet, PConstants, PVector}

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration

/** Created 3/25/20 12:08 AM
 */
//noinspection TypeAnnotation
trait ThreeDimPApplet extends PApplet {

  val gameObjects = mutable.ArrayBuffer.empty[GameObject]

  trait GameObject {
    def draw(): Unit = ()
    def tick(): Unit = ()
  }

  /** Default implementation. Override as desired. */
  override def draw(): Unit = {
    darkBackground()
    gameObjects.foreach(_.draw())
    gameObjects.foreach(_.tick())
  }

  override def settings(): Unit = fullScreen(PConstants.P3D)

  // For the scala compiler, by 'lifting' this method into this class,
  // it disambiguates which overload should be called in the (Float, Float, Float)
  // case (i.e. it should be THIS overload).
  final override def translate(x: Float, y: Float, z: Float): Unit =
    super.translate(x, y, z)

  def translate(x: Double, y: Double, z: Double): Unit =
    ThreeDimPApplet.super.translate(x.toFloat, y.toFloat, z.toFloat)

  def darkBackground(): Unit = background(0f)

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

  object Green extends Rgba(0, 100, 0)
  object Black extends Rgba(0, 0, 0)
  object White extends Rgba(100, 100, 100)

  sealed case class Rgba(r: Float, g: Float, b: Float, a: Float = 100) extends Color {
    override def stroke(): Unit = {
      colorMode(PConstants.RGB, 100)
      ThreeDimPApplet.this.stroke(r, g, b, a)
    }
    override def fill(): Unit = {
      colorMode(PConstants.RGB, 100)
      ThreeDimPApplet.this.fill(r, g, b, a)
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

  object PVector {
    def apply(x: Double, y: Double, z: Double = 0) =
      new PVector(x.toFloat, y.toFloat, z.toFloat)
  }

  case class Rotation(period: FiniteDuration) extends GameObject {
    private var rotationFraction = 0D
    val millerPerPeriod = 1.0 / period.toMillis
    private var lastTime = millis()
    gameObjects += this

    override def tick(): Unit = {
      val elapsedTime = millis() - lastTime
      // TODO is this right? can't think right now.
      //  It should "work" (rotate) anyhow, just not necessarily at the right speed.
      rotationFraction += elapsedTime.toDouble / period.toMillis
      lastTime = millis()
    }

    def curRadians(): Float = angleFractionToRadians(rotationFraction)
  }

  def angleFractionToRadians(angleFraction: Double): Float =
    ((angleFraction - (math floor angleFraction)) * math.Pi * 2).toFloat
}
