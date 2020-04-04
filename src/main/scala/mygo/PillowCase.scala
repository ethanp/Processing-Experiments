package mygo
import geometry.Triangle
import helpers.{MyPApplet, Runner}

/** Created 4/3/20 7:07 PM
 */
class PillowCase extends MyPApplet {

  val OverallSide: Int = 700
  val MidSide: Int = OverallSide / 2

  val MidBarHeight: Float = 100f

  val InnerMargin: Float = 75f

  val NumTriangles: Int = 10
  val TriangleWidth: Float = (OverallSide - InnerMargin * 2) / NumTriangles
  val TriangleHeight: Float = (OverallSide - InnerMargin * 2) / 3


  override def settings(): Unit = size(OverallSide, OverallSide)

  override def draw(): Unit = {
    drawOuterBackground()
    drawInnerBackground()
    drawTriangles()
    drawMidBar()
    drawOuterStrips()
    drawSpirals()
  }

  private def drawOuterBackground(): Unit = {
    colors.Solarized.Orange.background()
  }

  private def drawInnerBackground(): Unit = {
    noStroke()
    colors.set(
      fill = colors.Solarized.White,
      stroke = colors.Empty
    )
    geometry.Rectangle(
      left = InnerMargin,
      top = InnerMargin,
      width = OverallSide - InnerMargin * 2,
      height = OverallSide - InnerMargin * 2,
    ).draw()
  }

  // Note: As desired, we could refactor to draw both the upper and lower inner
  // backgrounds separately, and then we wouldn't need this mid-bar at all.
  private def drawMidBar(): Unit = {
    colors.set(
      fill = colors.Solarized.Orange.copy(a = 50),
      stroke = colors.Empty
    )
    geometry.Rectangle(
      left = 0,
      top = OverallSide / 2 - MidBarHeight / 2,
      width = OverallSide,
      height = MidBarHeight,
    ).draw()
  }

  private def drawTriangles(): Unit = {
    colors.set(
      fill = colors.Solarized.Red,
      stroke = colors.Empty
    )

    for (idx <- 0 until NumTriangles) {
      val top: Float = MidSide + TriangleHeight
      val bottom: Float = MidSide - TriangleHeight

      val centerLeft = geometry.Vector(idx * TriangleWidth + InnerMargin, MidSide)
      val centerRight = geometry.Vector((idx + 1) * TriangleWidth + InnerMargin, MidSide)
      val downLeft = geometry.Vector(centerLeft.x, top)
      val upRight = geometry.Vector(centerRight.x, bottom)

      idx match {
        case _ if idx % 2 == 0 => Triangle(centerLeft, centerRight, downLeft).draw()
        case _ => Triangle(centerLeft, upRight, centerRight).draw()
      }
    }
  }

  private val StripWidth = OverallSide - 2 * InnerMargin
  private val StripHeight = MidSide - TriangleHeight - InnerMargin

  private val topStrip = geometry.Rectangle(
    left = InnerMargin,
    top = InnerMargin,
    width = StripWidth,
    height = StripHeight
  )

  private val bottomStrip = geometry.Rectangle(
    left = InnerMargin,
    top = MidSide + TriangleHeight,
    width = StripWidth,
    height = StripHeight
  )

  private def drawOuterStrips(): Unit = {
    colors.set(
      fill = colors.Solarized.Black,
      stroke = colors.Empty
    )

    topStrip.draw()

    // bottom one
    bottomStrip.draw()
  }

  private def drawSpirals(): Unit = {
    // TODO draw a few of these on top and a few on bottom

    geometry.Spiral(
      center = topStrip.leftTop.copy().add(geometry.V(50, 50)),
      numLoops = 3,
      radiusIncrement = .03f,
      fillAtDeg = _ => colors.Solarized.Orange
    ).draw()
  }
}

object PillowCase extends Runner {
  override def pAppletClass: Class[_] = classOf[PillowCase]
}
