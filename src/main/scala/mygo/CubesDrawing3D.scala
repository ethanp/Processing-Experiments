package mygo

import colors.Rgb
import helpers.{GameObject, Runner, ThreeDimPApplet}

/**
 * This is based on [[emulations.CubesDrawing]]
 *
 * Created Saturday March 28, 2020 9:22 PM
 */
class CubesDrawing3D extends ThreeDimPApplet {

  // TODO this did used-to work, but it doesn't work now.
  //  Probably, something is wrong with the camera.

  private val Span = 2
  private val range = -Span to Span
  private val colWidth = width / range.length
  private val rowHeight = height / range.length
  private val depthLength = (rowHeight + colWidth) / 2

  for (r <- range; c <- range; t <- range)
    gameObjects += new Cube(r, c, t)

  override def moveCamera(): Unit = rotateX(.38f)

  private class Cube(r: Int, c: Int, t: Int) extends GameObject {
    override def drawFromCenter(): Unit = {
      ortho(-width / 2, width / 2, -height / 2, height / 2, -frameCount / 2, frameCount / 2)
      val factor = 8
      rotateX(4f)
      rotateY(7f)
      translate(
        c * colWidth * factor,
        r * rowHeight * factor,
        t * depthLength * factor
      )
      rotateY(r / 10f)
      rotateX(c / 5f + 3.8f)
      rotateZ(t / 10f + 3.8f)
      strokeWeight(2)
      Rgb(80, 70, 40).stroke()(app)
      Rgb(
        r = (Span - r) * 20,
        g = (c + Span) * 10,
        b = (t / 2 + Span) * 15,
        a = 50
      ).fill()(app)
      box(depthLength * factor / 1.3f)
    }
  }
}

object CubesDrawing3D extends Runner[CubesDrawing3D]
