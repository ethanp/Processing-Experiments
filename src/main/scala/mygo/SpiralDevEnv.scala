package mygo
import helpers.{MyPApplet, Runner}

/**
 * The point of this class is to provide a playground in-which to develop a good
 * drawable-Spiral design/API/implementation.
 *
 * Created 4/3/20 11:04 PM
 */
class SpiralDevEnv extends MyPApplet {
  override def settings(): Unit = size(700, 700)

  val NumLoops = 20
  val Smoothness = 4

  override def draw(): Unit = {
    blackBackground()
    fromTheCenter {
      for (notSmoothed <- 0 until (360 * NumLoops * Smoothness)) {
        colors.set(
          fill = colors.Hsb((notSmoothed / 5f) % 100),
          stroke = colors.Empty
        )
        val degrees = notSmoothed.toFloat / Smoothness
        val radius = 1f + (degrees / 20f)
        ellipse(
          /*x*/ cosDeg(degrees = degrees) * radius,
          /*y*/ sinDeg(degrees = degrees) * radius,
          /*w*/ 7,
          /*h*/ 7
        )
      }
    }
  }
}


object SpiralDevEnv extends Runner {
  override def pAppletClass: Class[_] = classOf[SpiralDevEnv]
}

