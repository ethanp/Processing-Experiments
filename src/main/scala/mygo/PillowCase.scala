package mygo
import helpers.{MyPApplet, Runner}

/** Created 4/3/20 7:07 PM
 */
class PillowCase extends MyPApplet {
  val Width: Int = 700
  val Height: Int = 700
  val MidBarHeight = 50
  val InnerSideMargin = 50
  val InnerTopMargin = 50
  val NumTriangles = 10


  override def settings(): Unit = {
    size(Width, Height)
  }

  override def draw(): Unit = {
    drawBackground()
    drawInnerBackground()
    drawMidBar()
    drawTriangles()
  }

  private def drawBackground(): Unit = {
    /*TODO swap to a new Solarized.Orange */
    colors.Solarized.Yellow.background()
  }

  private def drawInnerBackground(): Unit = {
    colors.Solarized.White.fill()
    noStroke()
    geometry.Rectangle(
      left = InnerSideMargin,
      top = InnerTopMargin,
      width = Width - InnerSideMargin * 2,
      height = Height - InnerTopMargin * 2,
    ).draw()
  }

  private def drawMidBar(): Unit = {
    colors.Solarized.Yellow.fill()
    noStroke()
    geometry.Rectangle(
      left = 0,
      top = Height / 2 - MidBarHeight / 2,
      width = Width,
      height = MidBarHeight,
    ).draw()
  }

  private def drawTriangles(): Unit = {
  }
}

object PillowCase extends Runner {
  override def pAppletClass: Class[_] = classOf[PillowCase]
}
