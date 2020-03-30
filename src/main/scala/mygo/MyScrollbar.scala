package mygo

import colors.{Pure, Solarized}
import geometry.{PVector, Rectangle, V}
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

  val scrollbar = new Scrollbar(this)

  override def setup(): Unit = {
    frameRate(60)
  }

  override def mousePressed(click: MouseEvent): Unit = scrollbar.mousePressed(click)
  override def mouseDragged(event: MouseEvent): Unit = scrollbar.mouseDragged(event)
  override def mouseReleased(event: MouseEvent): Unit = scrollbar.mouseReleased(event)
  override def draw(): Unit = scrollbar.draw()
}

object MyScrollbar extends Runner {
  override def pAppletClass: Class[_] = classOf[MyScrollbar]
}

class Scrollbar(myPApplet: MyPApplet) {
  implicit val app: MyPApplet = myPApplet
  private val outerRectangle = Rectangle(
    leftTop = V(10, 10),
    widthHeight = V(200, 100)
  )

  private val innerRectangle = Rectangle(
    leftTop = V(10, 10),
    widthHeight = V(30, 100)
  )

  var isDragging = false

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
    val extent = innerRectangle.left - outerRectangle.left
    val width = outerRectangle.width - innerRectangle.width
    val prop = extent / width
    curValue = (prop * maxValue).toInt
  }

  def mouseDragged(event: MouseEvent): Unit = innerRectDragged(event.getX)

  var curValue: Int = 0
  val minValue: Int = 0
  val maxValue: Int = 10

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
      /*string=*/ curValue,
      /*left=*/ outerRectangle.right + 10,
      /*bottom??=*/ outerRectangle.bottom - outerRectangle.height / 3
    )
  }
}
