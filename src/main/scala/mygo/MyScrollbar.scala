package mygo

import helpers.{MyPApplet, Runner, Scrollbar}
import processing.event.MouseEvent

/** Created 3/29/20 8:20 PM
 */
class MyScrollbar extends MyPApplet {
  override def settings(): Unit = {
    val Width = 500
    val Height = 400
    size(Width, Height)
  }

  val scrollbar = new Scrollbar(0, 0, 10)

  override def setup(): Unit = frameRate(60)

  override def mousePressed(click: MouseEvent): Unit = scrollbar.mousePressed(click)
  override def mouseDragged(event: MouseEvent): Unit = scrollbar.mouseDragged(event)
  override def mouseReleased(event: MouseEvent): Unit = scrollbar.mouseReleased(event)
  override def draw(): Unit = scrollbar.draw()
}

object MyScrollbar extends Runner {
  override def pAppletClass: Class[_] = classOf[MyScrollbar]
}


