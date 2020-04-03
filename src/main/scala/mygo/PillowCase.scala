package mygo
import helpers.{MyPApplet, Runner}

/** Created 4/3/20 7:07 PM
 */
class PillowCase extends MyPApplet {
  override def settings(): Unit = {
    val Width = 700
    val Height = 700
    size(Width, Height)
  }

  override def draw(): Unit = {
    drawBackground()
  }

  private def drawBackground() = {
    /*TODO swap to a new Solarized.Orange */
    colors.Solarized.Yellow.background()
  }
}

object VennColors extends Runner {
  override def pAppletClass: Class[_] = classOf[VennColors]
}
