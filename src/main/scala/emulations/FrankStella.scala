package emulations
import animation.Every
import colors.Solarized
import helpers.{MyPApplet, Runner}
import processing.core.PConstants

import scala.concurrent.duration._
import scala.util.Random


/**
 * This is based on https://www.instagram.com/p/B9KFD_qINDL/
 *
 * by @generated.simplicity.
 *
 * Created 3/28/20 12:41 PM
 */
class FrankStella extends MyPApplet {
  private val NumRows = 2
  private val NumCols = 3

  private def colWidth = width / NumCols
  private def rowHeight = height / NumRows

  private val atFrameRate = new Every(4.seconds)

  override def settings(): Unit = {
    pixelDensity = 2
    fullScreen(PConstants.P2D)
  }

  private def fillRandom(): Unit = Random nextInt 4 match {
    case 0 => Solarized.Black.fill
    case 3 => Solarized.Yellow.fill
    case 1 => Solarized.White.fill
    case 2 => Solarized.Red.fill
  }

  override def mouseClicked(): Unit =
    saveFrame(this.getClass.getSimpleName + ".jpg")

  override def drawFrame(): Unit = atFrameRate run {
    background(82, 0, 164)
    noStroke()

    for (r <- 0 until NumRows; c <- 0 until NumCols) {
      val xLeft = c * colWidth
      val xRight = (c + 1) * colWidth
      val xHalf = (xLeft + xRight) / 2
      val yTop = r * rowHeight
      val yBottom = (r + 1) * rowHeight
      val yHalf = (yTop + yBottom) / 2

      Random.nextBoolean() match {
        case true => /* Bottom Left <-> Top Right */

          /* Top fill */
          fillRandom()
          beginShape()
          vertex(xLeft, yBottom)
          bezierVertex(xLeft, yHalf, xHalf, yTop, xRight, yTop)
          vertex(xLeft, yTop)
          endShape(PConstants.CLOSE)

          /* Bottom fill*/
          fillRandom()
          beginShape()
          vertex(xRight, yTop)
          bezierVertex(xRight, yHalf, xHalf, yBottom, xLeft, yBottom)
          vertex(xRight, yBottom)
          endShape(PConstants.CLOSE)

          /* Middle fill */
          fillRandom()
          beginShape()
          vertex(xLeft, yBottom)
          bezierVertex(xLeft, yHalf, xHalf, yTop, xRight, yTop)
          bezierVertex(xRight, yHalf, xHalf, yBottom, xLeft, yBottom)
          endShape(PConstants.CLOSE)


        case false => /* Top Left <-> Bottom Right */

          /* Top fill */
          fillRandom()
          beginShape()
          vertex(xLeft, yTop)
          bezierVertex(xHalf, yTop, xRight, yHalf, xRight, yBottom)
          vertex(xRight, yTop)
          endShape(PConstants.CLOSE)

          /* Bottom fill*/
          fillRandom()
          beginShape()
          vertex(xRight, yBottom)
          bezierVertex(xHalf, yBottom, xLeft, yHalf, xLeft, yTop)
          vertex(xRight, yTop)
          endShape(PConstants.CLOSE)

          /* Middle fill */
          fillRandom()
          beginShape()
          vertex(xLeft, yTop)
          bezierVertex(xHalf, yTop, xRight, yHalf, xRight, yBottom)
          bezierVertex(xHalf, yBottom, xLeft, yHalf, xLeft, yTop)
          endShape(PConstants.CLOSE)
      }
    }
  }
}

object FrankStella extends Runner[FrankStella]
