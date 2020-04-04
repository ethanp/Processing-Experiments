package mygo
import helpers.{MyPApplet, Runner}

/**
 * The point of this class is to provide a playground in-which to develop a good
 * drawable-Spiral design/API/implementation.
 *
 * Created 4/3/20 11:04 PM
 */
class SpiralDevEnv extends MyPApplet {
  override def draw(): Unit = {
    // TODO fill this in.
  }
}


object SpiralDevEnv extends Runner {
  override def pAppletClass: Class[_] = classOf[SpiralDevEnv]
}

