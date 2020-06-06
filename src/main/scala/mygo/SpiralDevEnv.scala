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

  override def drawFrame(): Unit = {
    blackBackground()
    geometry.Spiral(
      center = geometry.V(width / 2, height / 2),
      numLoops = 20,
      radiusIncrement = .05f,
      fillAtDeg = deg => colors.Hsb(deg / 10f % 100),
      width = 7,
    ).draw()
  }
}


object SpiralDevEnv extends Runner[SpiralDevEnv]

