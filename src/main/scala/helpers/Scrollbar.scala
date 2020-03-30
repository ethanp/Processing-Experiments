package helpers
import colors.{Pure, Solarized}
import geometry.{PVector, Rectangle, V}
import processing.core.PConstants
import processing.event.MouseEvent

/** Created 3/29/20 11:23 PM
 */
class Scrollbar(initial: Float, min: Float, max: Float)(implicit app: MyPApplet) {

  //noinspection NotImplementedCode
  if (min != 0) ???

  private val outerRectangle = Rectangle(
    leftTop = V(10, 10),
    widthHeight = V(200, 100)
  )

  private val innerRectangle = Rectangle(
    leftTop = V(10, 10),
    widthHeight = V(30, 100)
  )

  private var curValue: Float = initial

  private var isDragging = false

  def mousePressed(click: MouseEvent): Unit = {
    if (click.getButton == PConstants.LEFT) {
      val clickLoc = PVector(click.getX, click.getY)
      if (clickLoc isInside innerRectangle) {
        isDragging = true
      }
    }
  }

  private def innerRectDragged(mouseX: Int): Unit = {
    if (isDragging) {
      innerRectangle centerAt mouseX within outerRectangle
      updateCurValue()
    }
  }

  private def updateCurValue(): Unit = {
    val extent: Float = innerRectangle.left - outerRectangle.left
    val width: Float = outerRectangle.width - innerRectangle.width
    val prop: Float = extent / width
    curValue = prop * max
  }

  def mouseDragged(event: MouseEvent): Unit = innerRectDragged(event.getX)

  def mouseReleased(event: MouseEvent): Unit = {
    innerRectDragged(event.getX)
    isDragging = false
  }

  def draw(): Unit = {
    app.background(10)
    app.strokeWeight(2)
    Pure.Black.stroke()
    Solarized.Red.fill()
    outerRectangle.draw()
    Solarized.Yellow.fill()
    innerRectangle.draw()
    Solarized.White.stroke().fill()
    app.textSize(24)
    app.text(
      /*string=*/ f"$curValue%.2f",
      /*left=*/ outerRectangle.right + 10,
      /*bottom??=*/ outerRectangle.bottom - outerRectangle.height / 3
    )
  }
}
