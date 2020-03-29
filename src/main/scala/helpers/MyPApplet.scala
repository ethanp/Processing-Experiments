package helpers
import processing.core.{PApplet, PConstants, PVector}

import scala.concurrent.duration.FiniteDuration

/** Created 3/29/20 11:28 AM
 */
trait MyPApplet extends PApplet {

  def darkBackground(): Unit = background(0f)

  override def mouseClicked(): Unit = {
    val className = this.getClass.getSimpleName
    val idx: Int =
      reflect.io.Directory(".")
        .list
        .map(_.name)
        .filter(_ contains className)
        .filter(_ endsWith ".jpg")
        .toSeq
        .maxOption
      match {
        case None => 0
        case Some(path) =>
          "-(\\d+)\\.jpg$".r findFirstMatchIn path match {
            case Some(found) => found.group(1).toInt + 1
            case None =>
              System.err.println(s"WARNING: Couldn't parse file path: $path")
              0
          }
      }
    // TODO first we should check if the new frame is gonna just be the same
    //  as one of the ones we've already saved and show a warning instead of saving
    saveFrame(f"$className-$idx%02d.jpg")
  }

  sealed trait Color {
    def stroke(): Unit
    def fill(): Unit
    final def strokeAndFill(): Unit = { stroke(); fill() }
  }

  case class Hsb(h: Float, s: Float = 100, b: Float = 100, a: Float = 100) extends Color {
    override def stroke(): Unit = {
      colorMode(PConstants.HSB, 100)
      MyPApplet.this.stroke(h, s, b, a)
    }
    override def fill(): Unit = {
      colorMode(PConstants.HSB, 100)
      MyPApplet.this.fill(h, s, b, a)
    }
  }

  object Green extends Rgb(0, 100, 0)
  object Black extends Rgb(0, 0, 0)
  object White extends Rgb(100, 100, 100)

  sealed case class Rgb(r: Float, g: Float, b: Float, a: Float = 100) extends Color {
    override def stroke(): Unit = {
      colorMode(PConstants.RGB, 100)
      MyPApplet.this.stroke(r, g, b, a)
    }
    override def fill(): Unit = {
      colorMode(PConstants.RGB, 100)
      MyPApplet.this.fill(r, g, b, a)
    }
  }

  object PVector {
    def apply(x: Double, y: Double, z: Double = 0) =
      new PVector(x.toFloat, y.toFloat, z.toFloat)
  }

  final protected def withPushedMatrix(block: => Unit): Unit = {
    pushMatrix()
    block
    popMatrix()
  }

  final def is3D: Boolean = sketchRenderer() contains "3D"

  final protected def fromTheCenter(block: => Unit): Unit = {
    withPushedMatrix {
      if (is3D) translate(width / 2, height / 2, 0)
      else translate(width / 2, height / 2)
      block
    }
  }

  final def sin(x: Double): Float = math.sin(x).toFloat

  case class Every(duration: FiniteDuration) {
    private var lastTime = 0L
    def run(block: => Unit): Unit = {
      if (millis() / duration.toMillis > lastTime) {
        block
        lastTime = millis() / duration.toMillis
      }
    }
  }
}
