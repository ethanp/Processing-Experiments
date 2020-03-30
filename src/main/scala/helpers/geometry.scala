import helpers.MyPApplet
import processing.core.PVector

/** Created 3/29/20 11:17 PM
 */
package object geometry {
  case class Rectangle(leftTop: PVector, widthHeight: PVector) {
    def centerAt(center: Int): Rectangle = {
      leftTop.x = center - width / 2
      this
    }

    def within(rectangle: Rectangle): Rectangle = {
      leftTop.x = left max rectangle.left
      leftTop.x = (right min rectangle.right) - width
      this
    }

    def left: Float = leftTop.x
    def top: Float = leftTop.y
    def right: Float = bottomRight.x
    def bottom: Float = bottomRight.y

    def width: Float = widthHeight.x
    def height: Float = widthHeight.y

    def bottomRight: PVector = leftTop.copy().add(widthHeight)
    def draw()(implicit myPApplet: MyPApplet): Unit = myPApplet.rect(left, top, widthHeight.x, widthHeight.y)
  }

  object Rectangle {
    def apply(left: Float, top: Float, width: Float, height: Float): Rectangle =
      Rectangle(PVector(left, top), PVector(width, height))
  }

  object V {
    def apply(x: Float, y: Float, z: Float = 0): MyPVector = PVector(x, y, z)
  }

  class MyPVector(_x: Float, _y: Float, _z: Float = 0) extends PVector(_x, _y, _z) {
    def isInside(rectangle: Rectangle): Boolean =
      x > rectangle.left &&
        y > rectangle.top &&
        x < rectangle.right &&
        y < rectangle.bottom
  }

  object PVector {
    def apply(x: Double, y: Double, z: Double = 0) =
      new MyPVector(x.toFloat, y.toFloat, z.toFloat)
  }
}
