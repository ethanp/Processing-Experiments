package framework_experiments

import helpers.controls.{HorizontalFader, VerticalFader}
import helpers.{MyPApplet, Runner}

/** Created 3/29/20 8:20 PM
 */
class FaderDemo extends MyPApplet {
  override def settings(): Unit = {
    val Width = 500
    val Height = 400
    size(Width, Height)
  }

  new HorizontalFader(
    initialValue = 0, minValue = 0, maxValue = 10,
    changeListener = (_, _, current: Number) =>
      println(f"Horizontal Scrollbar from main drawing: ${ current.floatValue() }%.02f"),
    bounds = geometry.Rectangle(
      left = 100,
      top = 100,
      width = 300,
      height = 30
    )
  )

  new VerticalFader(
    initialValue = 0, minValue = 0, maxValue = 10,
    changeListener = (_, _, current: Number) =>
      println(f"Vertical Scrollbar from main drawing: ${ current.floatValue() }%.02f"),
    bounds = geometry.Rectangle(
      left = 100,
      top = 150,
      width = 30,
      height = 150
    )
  )


  override def setup(): Unit = frameRate(60)

  override def drawFrame(): Unit = {
    background(10)
    faders foreach (_.draw())
  }
}

object FaderDemo extends Runner[FaderDemo]
