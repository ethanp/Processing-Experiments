package emulations

import helpers.Runner
import processing.core.{PApplet, PConstants}

import scala.util.Random


/**
 * This is based on https://www.instagram.com/p/B9KFD_qINDL/
 *
 * by @generated.simplicity.
 *
 * Created 3/28/20 12:41 PM
 */
class FrankStella extends PApplet {
  private val NumRows = 4
  private val NumCols = 4

  private def colWidth = width / NumCols
  private def rowHeight = height / NumRows

  override def settings(): Unit = {
    pixelDensity = 2
    fullScreen(PConstants.P2D)
  }

  override def setup(): Unit = frameRate(1)

  private def fillRandom(): Unit = Random nextInt 4 match {
    case 0 => fill(0, 43, 54)
    case 1 => fill(211, 54, 130)
    case 2 => fill(211, 1, 2)
    case 3 => fill(181, 137, 0)
  }

  /* TODO(feature) some of the arcs should be in the other pair of corners */

  override def draw(): Unit = {
    background(82, 0, 164)
    noStroke()

    for (r <- 0 until NumRows; c <- 0 until NumCols) {
      val xLeft = c * colWidth
      val xRight = (c + 1) * colWidth
      val yTop = r * rowHeight
      val yBottom = (r + 1) * rowHeight

      // TODO something in these curves is not quite right

      /* Top fill */
      fillRandom()
      beginShape()
      vertex(xLeft, yTop)
      vertex(xRight, yTop)
      bezierVertex(xLeft, yTop, xLeft, yTop, xLeft, yBottom)
      endShape(PConstants.CLOSE)

      /* Middle fill */
      fillRandom()
      beginShape()
      vertex(xRight, yTop)
      bezierVertex(xLeft, yTop, xLeft, yTop, xLeft, yBottom)
      bezierVertex(xRight, yBottom, xRight, yBottom, xRight, yTop)
      endShape(PConstants.CLOSE)

      /* Bottom fill*/
      fillRandom()
      beginShape()
      // 1. top left
      vertex(xLeft, yTop)
      // 2. top right
      vertex(xRight, yTop)
      // 3. arc to bottom left via bottom right
      bezierVertex(xRight, yBottom, xLeft, yBottom, xLeft, yBottom)
      // maybe we just need three points and then close it?
      endShape(PConstants.CLOSE)
    }
  }
}

object FrankStella extends Runner {
  override def pAppletClass: Class[_] = classOf[FrankStella]
}
