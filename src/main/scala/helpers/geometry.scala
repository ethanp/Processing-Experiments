import helpers.MyPApplet
import processing.core.PVector

/** Created 3/29/20 11:17 PM
 */
package object geometry {
  case class Rectangle(leftTop: PVector, widthHeight: PVector) {
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

    def bottomRight: PVector = leftTop.copy().add(widthHeight)

    def draw()(implicit myPApplet: MyPApplet): Unit =
      myPApplet.rect(left, top, widthHeight.x, widthHeight.y)
  }

  object Rectangle {
    def apply(left: Float, top: Float, width: Float, height: Float): Rectangle =
      Rectangle(Vector(left, top), Vector(width, height))
  }

  case class Triangle(p1: PVector, p2: PVector, p3: PVector) {
    def draw()(implicit myPApplet: MyPApplet): Unit = {
      myPApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
    }
  }

  object V {
    def apply(x: Float, y: Float, z: Float = 0): Vector = Vector(x, y, z)
  }

  class Vector(_x: Float, _y: Float, _z: Float = 0) extends PVector(_x, _y, _z) {
    def isInside(rectangle: Rectangle): Boolean =
      x > rectangle.left &&
        y > rectangle.top &&
        x < rectangle.right &&
        y < rectangle.bottom
  }

  object Vector {
    def apply(x: Double, y: Double, z: Double = 0) =
      new Vector(x.toFloat, y.toFloat, z.toFloat)
  }
}
