package mygo

import helpers.{Runner, ThreeDimPApplet}


/**
 * This is based on [[emulations.CubesDrawing]]
 *
 * Created Saturday March 28, 2020 9:22 PM
 */
class CubesDrawing3D extends ThreeDimPApplet {
  private val Span = 2
  private val range = -Span to Span
  private val colWidth = width / range.length
  private val rowHeight = height / range.length
  private val depthLength = (rowHeight + colWidth) / 2

  for (r <- range; c <- range; t <- range)
    gameObjects += Cube(r, c, t)

  override def mouseClicked(): Unit = {
    val className = this.getClass.getSimpleName
    val idx: Int =
      reflect.io.Directory(".")
        .list
        .map(_.name)
        .filter(_ contains className)
        .filter(_.endsWith(".jpg"))
        .toSeq
        .maxOption
      match {
        case None => 0
        case Some(path) =>
          "-(\\d+)".r findFirstMatchIn path match {
            case Some(found) => found.group(1).toInt + 1
            case None =>
              System.err.println(s"WARNING: Couldn't parse file path: $path")
              0
          }
      }
    // TODO first we should check if the new one is gonna just be the same
    //  as one of the ones we've already saved.
    saveFrame(f"$className-$idx%02d.jpg")
  }

  override def moveCamera(): Unit = {
    rotateX(.38f)
  }

  private case class Cube(r: Int, c: Int, t: Int) extends GameObject {
    override def draw(): Unit = {
      ortho()
      val factor = 4
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
      strokeWeight(3)
      Rgb(80, 70, 40).stroke()
      Rgb((Span - r) * 20, (c + Span) * 10, (t / 2 + Span) * 15, 70).fill()
      box(depthLength * factor / 1.3f)
    }
  }
}

object CubesDrawing3D extends Runner {
  override def pAppletClass: Class[_] = classOf[CubesDrawing3D]
}
