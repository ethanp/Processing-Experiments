package mygo
import helpers.{MyPApplet, Runner}
import processing.core.PConstants
import processing.event.MouseEvent

/** Created 3/29/20 8:20 PM
 */
class MyScrollbar extends MyPApplet {
  override def settings(): Unit = {
    val Width = 500
    val Height = 400
    size(Width, Height)
  }

  private val outerRectangle = Rectangle(
    leftTop = PVector(10, 10),
    dimensionLengths = PVector(200, 100)
  )

  private val innerRectangle = Rectangle(
    leftTop = PVector(10, 10),
    dimensionLengths = PVector(30, 100)
  )

  var isDragging = false

  override def mousePressed(mouseEvent: MouseEvent): Unit = {
    if (mouseEvent.getButton == PConstants.LEFT) {
      val clickLoc = PVector(mouseEvent.getX, mouseEvent.getY)
      if (clickLoc isInside innerRectangle) {
        isDragging = true
      }
    }
  }

  private def innerRectDragged(): Unit = {
    if (isDragging) {
      innerRectangle centerAt mouseX within outerRectangle
      updateCurValue()
    }
  }

  private def updateCurValue(): Unit = {
    val width = outerRectangle.width - innerRectangle.width
    val extent = innerRectangle.left - outerRectangle.left
    val prop = extent / width
    curValue = (prop * maxValue).toInt
    println(s"INFO: curValue = $curValue")
    // TODO there should be a label on the screen instead of a println
  }

  override def mouseDragged(event: MouseEvent): Unit = innerRectDragged()

  var curValue: Int = 0
  val minValue: Int = 0
  val maxValue: Int = 10

  override def mouseReleased(event: MouseEvent): Unit = {
    innerRectDragged()
    isDragging = false
  }

  override def draw(): Unit = {
    // TODO should be linear gradient
    Solarized.Red.fill()
    outerRectangle.draw()
    // TODO should be radial gradient
    Solarized.Yellow.fill()
    innerRectangle.draw()
  }
}

object MyScrollbar extends Runner {
  override def pAppletClass: Class[_] = classOf[MyScrollbar]
}
