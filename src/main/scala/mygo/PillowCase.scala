package mygo
import geometry.Triangle
import helpers.{MyPApplet, Runner}
import processing.core.PShape

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

  override def setup(): Unit = {
    frameRate(5)
  }

  override def draw(): Unit = {
    drawOuterBackground()
    drawInnerBackground()
    drawTriangles()
    drawMidBar()
    drawOuterStrips()
    drawSpirals()
    overwritePngsForGifIfEnabled()
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

  private var fastShape: PShape = _

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
      // NB: This is the colors setting that gets applied, /not/ the one we can add
      // while building the [[PShape]] `fastShape`.
      colors.Current.update(
        fill = colors.Solarized.White,
        stroke = colors.Solarized.Cyan
      )

      // We do a batch call with the hope that it is much faster it is than rendering each
      // line individually. With the individual calls I was getting 40000 lines 3x/sec.
      // With this thing it might be like 6x/sec. I was expecting much more speed here.
      if (fastShape == null) {
        fastShape = createShape()
        fastShape.beginShape()
        val first = staticRandomLines.head.from
        fastShape.vertex(first.x, first.y)
        staticRandomLines foreach (line => fastShape.vertex(line.to.x, line.to.y))
        fastShape.endShape()
      }
      fastShape.draw(getGraphics)
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

    def jitter(vector: geometry.Vector) = {
      val scale = 5
      vector.x = vector.x + Random.nextGaussian().toFloat * scale
      vector.y = vector.y + Random.nextGaussian().toFloat * scale
      vector
    }

    geometry.Spiral(
      center = jitter(topStrip.leftTop + offset1),
      numLoops = 4,
      radiusIncrement = .02f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = jitter(topStrip.leftTop + offset2),
      numLoops = 3,
      radiusIncrement = .03f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = jitter(topStrip.leftTop + offset3),
      numLoops = 6,
      radiusIncrement = .01f,
      fillAtDeg = _ => fadedOrange,
      width = 3,
    ).draw()

    geometry.Spiral(
      center = jitter(bottomStrip.leftTop + offset1),
      numLoops = 3,
      radiusIncrement = .03f,
      fillAtDeg = _ => fadedOrange,
      width = 5,
    ).draw()

    geometry.Spiral(
      center = jitter(bottomStrip.leftTop + offset2),
      numLoops = 4,
      radiusIncrement = .02f,
      fillAtDeg = _ => fadedOrange,
      width = 4,
    ).draw()
  }

  /**
   * The resulting pngs can be converted into a gif with the following command
   *
   * $ convert -delay 20 PillowCaseFrames/*.png -loop 0 pillow-case.gif
   *
   */
   * Lolol we need to close the comment twice now because of the glob in the example.
   */
  // MedPriorityTodo turn gif-saving into a general-use utility
  private def overwritePngsForGifIfEnabled(): Unit = {
    val FramesToSave = 0 // zero means gif-saving is disabled. 25 is a pretty long gif.
    if (frameCount >= FramesToSave) return
    saveFrame(s"PillowCaseFrames/Frame-$frameCount.png")
  }
}

object PillowCase extends Runner {
  override def pAppletClass: Class[_] = classOf[PillowCase]
}
