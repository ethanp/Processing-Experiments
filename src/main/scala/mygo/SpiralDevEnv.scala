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

  override def draw(): Unit = {
    blackBackground()
    fromTheCenter {
      var radius: Float = 1f
      for (degrees <- 0 until (360 * 4)) {
        colors.set(
          fill = colors.Hsb(radius % 100),
          stroke = colors.Empty
        )
        val angle = math.toRadians(degrees)
        ellipse(
          /*x*/ math.cos(angle).toFloat * radius,
          /*y*/ math.sin(angle).toFloat * radius,
          /*w*/ 7,
          /*h*/ 7
        )
        radius += .2f
      }
    }
  }
}


object SpiralDevEnv extends Runner {
  override def pAppletClass: Class[_] = classOf[SpiralDevEnv]
}

