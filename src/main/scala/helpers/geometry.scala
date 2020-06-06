import helpers.MyPApplet
import processing.core.PApplet.{cos, sin}
import processing.core.{PConstants, PVector}

/** Created 3/29/20 11:17 PM
 */
package object geometry {
  case class Rectangle(leftTop: Vector, widthHeight: Vector) {
    def centerOnX(center: Int): Rectangle = {
      leftTop.x = center - width / 2
      this
    }

    def centerOnY(center: Int): Rectangle = {
      leftTop.y = center - height / 2
      this
    }

    def withinWidth(rectangle: Rectangle): Rectangle = {
      leftTop.x = left max rectangle.left
      leftTop.x = (right min rectangle.right) - width
      this
    }

    def withinHeight(rectangle: Rectangle): Rectangle = {
      leftTop.y = top max rectangle.top
      leftTop.y = (bottom min rectangle.bottom) - height
      this
    }

    def left: Float = leftTop.x
    def top: Float = leftTop.y
    def right: Float = bottomRight.x
    def bottom: Float = bottomRight.y

    def width: Float = widthHeight.x
    def height: Float = widthHeight.y

    def bottomRight: Vector = Vector(leftTop.copy() add widthHeight)

    def draw()(implicit myPApplet: MyPApplet): Unit =
      myPApplet.rect(left, top, widthHeight.x, widthHeight.y)
  }

  object Rectangle {
    def apply(left: Float, top: Float, width: Float, height: Float): Rectangle =
      Rectangle(Vector(left, top), Vector(width, height))
  }

  case class Line(from: Vector, to: Vector) {
    def draw()(implicit myPApplet: MyPApplet): Unit = {
      myPApplet.sketchRenderer() match {
        case PConstants.P3D =>
          myPApplet.line(from.x, from.y, from.z, to.x, to.y, to.z)
        case _ =>
          myPApplet.line(from.x, from.y, to.x, to.y)
      }
    }
  }

  case class Triangle(p1: Vector, p2: Vector, p3: Vector) {
    // Note it *might* matter which order the vectors are given, but most-likely it
    //  it *does not* matter.
    def draw()(implicit myPApplet: MyPApplet): Unit = {
      myPApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
    }
  }

  object V {
    def apply(x: Float, y: Float, z: Float = 0): Vector = Vector(x, y, z)
  }

  class Vector(_x: Float, _y: Float, _z: Float = 0) extends PVector(_x, _y, _z) {

    def constrainedToBounds(rectangle: Rectangle): Vector = {
      x = rectangle.left.max(x).min(rectangle.right)
      y = rectangle.top.max(y).min(rectangle.bottom)
      this
    }

    def isInside(rectangle: Rectangle): Boolean =
      x > rectangle.left &&
        y > rectangle.top &&
        x < rectangle.right &&
        y < rectangle.bottom

    /** Angle (radians) between this vector and straight horizontal. */
    def radiansOffHorizontal: Float = math.acos(dot(Vector.X) / length).toFloat

    /** Creates a copy with `value = this + vector`. */
    def +(vector: PVector): Vector = Vector(copy() add vector)

    /** Creates a copy with `value = this - vector`. */
    def -(vector: PVector): Vector = Vector(copy() sub vector)

    /** Creates a copy multiplied by `scale`. */
    def *(scale: Double): Vector = elementwise(op = _ * scale)

    /** Creates a copy divided by `scale`. */
    def /(scale: Double): Vector = elementwise(op = _ / scale)

    def length: Float = this dist Vector.Zero

    private def elementwise(op: Double => Double): Vector =
      Vector(op(x), op(y), op(z))
  }

  object Vector {
    def apply(x: Double, y: Double, z: Double = 0) = new Vector(x.toFloat, y.toFloat, z.toFloat)
    def apply(vector: PVector) = new Vector(vector.x, vector.y, vector.z)
    object Zero extends Vector(_x = 0, _y = 0, _z = 0)
    object X extends Vector(_x = 1, _y = 0, _z = 0)
    object Y extends Vector(_x = 0, _y = 1, _z = 0)
    object Z extends Vector(_x = 0, _y = 0, _z = 1)
  }

  case class Spiral(
    center: Vector,
    numLoops: Int,
    radiusIncrement: Float,
    fillAtDeg: Float => colors.Color,
    width: Int,
  ) {
    val Smoothness = 4
    def draw()(implicit myPApplet: MyPApplet): Unit = {
      myPApplet withPushedMatrix {
        myPApplet.translate(center.x, center.y)
        for (notSmoothed <- 0 until (360 * numLoops * Smoothness)) {
          val degrees = notSmoothed.toFloat / Smoothness
          colors.Current.update(
            fill = fillAtDeg(degrees),
            stroke = colors.Empty
          )
          val radius = 1f + (degrees * radiusIncrement)
          myPApplet.ellipse(
            /*x*/ cos(degrees.toRadians) * radius,
            /*y*/ sin(degrees.toRadians) * radius,
            /*w*/ width,
            /*h*/ width
          )
        }
      }
    }
  }
}
