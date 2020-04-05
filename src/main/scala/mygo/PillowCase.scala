package mygo
import geometry.Triangle
import helpers.{MyPApplet, Runner}

import scala.collection.mutable
import scala.util.Random

/** Created 4/3/20 7:07 PM
 */
class PillowCase extends MyPApplet {

  private val OverallSide: Int = 700
  private val MidSide: Int = OverallSide / 2

  private val MidBarHeight: Float = 100f

  private val InnerMargin: Float = 75f

  private val NumTriangles: Int = 10
  private val TriangleWidth: Float = (OverallSide - InnerMargin * 2) / NumTriangles
  private val TriangleHeight: Float = (OverallSide - InnerMargin * 2) / 3
  private val TriangleStripGap: Float = OverallSide / 20f

  private val StripWidth = OverallSide - 2 * InnerMargin
  private val StripHeight = MidSide - TriangleHeight - InnerMargin - TriangleStripGap

  private val RandomLineLength = 10

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

  private val innerBkgdRegion = geometry.Rectangle(
    left = InnerMargin,
    top = InnerMargin,
    width = OverallSide - InnerMargin * 2,
    height = OverallSide - InnerMargin * 2,
  )

  private val staticRandomLines: Seq[geometry.Line] = {
    def randomLineFrom(from: geometry.Vector): geometry.Line = {
      geometry.Line(
        from = from,
        to = geometry.Vector(
          x = from.x + Random.nextGaussian() * RandomLineLength,
          y = from.y + Random.nextGaussian() * RandomLineLength
        ).constrainedToBounds(rectangle = innerBkgdRegion)
      )
    }

    val startingPoint = geometry.Vector(
      x = MidBarHeight,
      y = MidBarHeight
    )

    val lines = mutable.ArrayBuffer(randomLineFrom(from = startingPoint))
    1 to 40000 foreach (_ => lines += randomLineFrom(from = lines.last.to))
    lines.toSeq
  }

  private def drawInnerBackground(): Unit = {
    { // Draw underlying rectangle
      colors.Current.update(
        fill = colors.Solarized.White,
        stroke = colors.Pure.Black,
        strokeWeight = 30
      )
      innerBkgdRegion.draw()
    }

    { // Draw static random lines
      colors.Current.update(
        fill = colors.Solarized.White,
        stroke = colors.Solarized.Cyan
      )
      staticRandomLines foreach (_.draw())
    }
  }

  // Note: As desired, we could refactor to draw both the upper and lower inner
  // backgrounds separately, and then we wouldn't need this mid-bar at all.
  private def drawMidBar(): Unit = {
    colors.Current.update(
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
    colors.Current.update(
      fill = colors.Solarized.Red,
      stroke = colors.Solarized.White,
      strokeWeight = 5
    )

    for (idx <- 0 until NumTriangles) {
      def randomOffset(): Float = Random.nextGaussian().toFloat * 20
      val top: Float = MidSide + TriangleHeight + randomOffset()
      val bottom: Float = MidSide - TriangleHeight + randomOffset()

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

  private val topStrip = geometry.Rectangle(
    left = InnerMargin,
    top = InnerMargin,
    width = StripWidth,
    height = StripHeight
  )

  private val bottomStrip = geometry.Rectangle(
    left = InnerMargin,
    top = MidSide + TriangleHeight + TriangleStripGap,
    width = StripWidth,
    height = StripHeight
  )

  private def drawOuterStrips(): Unit = {
    colors.Current.update(
      fill = colors.Solarized.Black,
      stroke = colors.Empty
    )
    topStrip.draw()
    bottomStrip.draw()
  }

  private def drawSpirals(): Unit = {
    val fadedOrange = colors.Solarized.Orange.copy(a = 4)

    val offset1 = geometry.V(
      x = OverallSide / 14,
      y = OverallSide / 14
    )
    val offset2 = geometry.V(
      x = OverallSide / 2,
      y = OverallSide / 10
    )
    val offset3 = geometry.V(
      x = OverallSide.toFloat * (1f / 3f),
      y = OverallSide.toFloat * (1f / 10f)
    )

    geometry.Spiral(
      center = topStrip.leftTop + offset1,
      numLoops = 4,
      radiusIncrement = .02f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = topStrip.leftTop + offset2,
      numLoops = 3,
      radiusIncrement = .03f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = topStrip.leftTop + offset3,
      numLoops = 6,
      radiusIncrement = .01f,
      fillAtDeg = _ => fadedOrange,
      width = 3,
    ).draw()

    geometry.Spiral(
      center = bottomStrip.leftTop + offset1,
      numLoops = 3,
      radiusIncrement = .03f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = bottomStrip.leftTop + offset2,
      numLoops = 4,
      radiusIncrement = .02f,
      fillAtDeg = _ => fadedOrange,
      width = 4,
    ).draw()
  }
}

object PillowCase extends Runner {
  override def pAppletClass: Class[_] = classOf[PillowCase]
}
