package mygo

import helpers.{HScrollbar, MyPApplet, Runner, Scrollbar, VScrollbar}
import processing.event.MouseEvent

/** Created 3/29/20 8:20 PM
 */
class MyScrollbar extends MyPApplet {
  override def settings(): Unit = {
    val Width = 500
    val Height = 400
    size(Width, Height)
  }

  private val scrollbar = new HScrollbar(
    initial = 0, min = 0, max = 10,
    listener = (_, _, current: Number) =>
      println(f"Horizontal Scrollbar from main drawing: ${ current.floatValue() }%.02f")
  )

  private val vScroller = new VScrollbar(
    initial = 0, min = 0, max = 10,
    listener = (_, _, current: Number) =>
      println(f"Vertical Scrollbar from main drawing: ${ current.floatValue() }%.02f")
  )

  private val scrollbars: Seq[Scrollbar] = Seq(scrollbar, vScroller)

  override def setup(): Unit = frameRate(60)

  override def mousePressed(click: MouseEvent): Unit = scrollbars foreach (_ mousePressed click)
  override def mouseDragged(event: MouseEvent): Unit = scrollbars foreach (_ mouseDragged event)
  override def mouseReleased(event: MouseEvent): Unit = scrollbars foreach (_ mouseReleased event)
  override def draw(): Unit = scrollbar.draw()
}

object MyScrollbar extends Runner {
  override def pAppletClass: Class[_] = classOf[MyScrollbar]
}
