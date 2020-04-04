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
    drawOuterBackground()
    drawInnerBackground()
    drawMidBar()
    drawOuterTriangles()
  }

  private def drawOuterBackground(): Unit = {
    /*TODO swap to a new Solarized.Orange */
    colors.Solarized.Yellow.background()
  }

  private def drawInnerBackground(): Unit = {
    noStroke()
    colors.set(
      fill = colors.Solarized.White,
      stroke = colors.Empty
    )
    geometry.Rectangle(
      left = InnerSideMargin,
      top = InnerTopMargin,
      width = Width - InnerSideMargin * 2,
      height = Height - InnerTopMargin * 2,
    ).draw()
  }

  // Note: As desired, we could refactor to draw both the upper and lower inner
  // backgrounds separately, and then we wouldn't need this mid-bar at all.
  private def drawMidBar(): Unit = {
    colors.set(
      fill = colors.Solarized.Yellow,
      stroke = colors.Empty
    )
    geometry.Rectangle(
      left = 0,
      top = Height / 2 - MidBarHeight / 2,
      width = Width,
      height = MidBarHeight,
    ).draw()
  }

  private def drawOuterTriangles(): Unit = {
    colors.set(
      fill = colors.Solarized.Red,
      stroke = colors.Empty
    )
    for (idx <- 0 until NumTriangles) {
      triangle(
        /*x1*/ 0,
        /*y1*/ 0,
        /*x2*/ 100,
        /*y2*/ 100,
        /*x3*/ 0,
        /*y3*/ 200
      )
    }
  }
}

object PillowCase extends Runner {
  override def pAppletClass: Class[_] = classOf[PillowCase]
}
