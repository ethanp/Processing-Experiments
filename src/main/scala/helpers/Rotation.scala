package helpers
import scala.concurrent.duration.FiniteDuration

/** Created 5/26/20 8:43 PM
 */
case class Rotation(period: FiniteDuration)(implicit pApplet: MyPApplet)
  extends GameObject {

  private def millisElapsedSinceAppStarted(): Int = pApplet.millis()

  private var rotationFraction = 0D
  private var lastTime = millisElapsedSinceAppStarted()

  override def tick(): Unit = {
    val elapsedTime = millisElapsedSinceAppStarted() - lastTime
    // LowPriorityTodo is this right? can't think right now.
    //  It should "work" (rotate) anyhow, just not necessarily at the desired period.
    rotationFraction += elapsedTime.toDouble / period.toMillis
    lastTime = millisElapsedSinceAppStarted()
  }

  def curRadians(): Float = angleFractionToRadians(rotationFraction)

  private def angleFractionToRadians(angleFraction: Double): Float =
    ((angleFraction - (math floor angleFraction)) * math.Pi * 2).toFloat
}
