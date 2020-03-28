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

  private def fillRandom(): Unit = Random nextInt 4 match {
    case 0 => fill(1, 200, 3, 255)
    case 1 => fill(1, 200, 3, 255)
    case 2 => fill(1, 200, 3, 255)
    case 3 => fill(1, 200, 3, 255)
  }

  override def draw(): Unit = {
    background(82, 0, 164)
    for (r <- 0 until NumRows; c <- 0 until NumCols) {
      noStroke()
      val facesRight = Random.nextBoolean()
      val hasTopArc = Random.nextBoolean()
      val hasBottomArc = Random.nextBoolean()
      def count(b: Boolean): Int = if (b) 1 else 0
      val numShapes = 1 + count(hasTopArc) + count(hasBottomArc)

      numShapes match {
        case 1 => rect(c * colWidth, r * rowHeight, colWidth, rowHeight)
        case _ =>
          beginShape()
          // TODO write this part
          endShape(PConstants.CLOSE)
      }
    }
  }
}

object FrankStella extends Runner {
  override def pAppletClass: Class[_] = classOf[FrankStella]
}
