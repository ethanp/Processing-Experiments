package emulations
import colors.Rgb
import helpers.{GameObject, Runner, ThreeDimPApplet}


/**
 * This is based on https://www.instagram.com/p/B-AP7xmISP3/
 *
 * by @generated.simplicity.
 *
 * Created 3/28/20 12:41 PM
 */
class CubesDrawing extends ThreeDimPApplet {
  private val Span = 2
  private val range = -Span to Span
  private val colWidth = width / range.length
  private val rowHeight = height / range.length

  for (r <- range; c <- range)
    gameObjects += Cube(r, c)

  override def mouseClicked(): Unit =
    saveFrame(this.getClass.getSimpleName + ".jpg")

  private case class Cube(r: Int, c: Int) extends GameObject {
    override def drawFromCenter(): Unit = {
      ortho(-width / 2, width / 2, -height / 2, height / 2, -frameCount / 2, frameCount / 2)
      val factor = 10
      translate(c * colWidth * factor, r * rowHeight * factor)
      rotateY((r + Span) * rowHeight)
      rotateX((c + Span) * rowHeight)
      strokeWeight(3)
      Rgb(90, 80, 40, 70).stroke()
      Rgb(30, 20, 70, 60).fill()
      box((colWidth min rowHeight) / 2 * factor)
    }
  }
}

object CubesDrawing extends Runner {
  override def pAppletClass: Class[_] = classOf[CubesDrawing]
}
