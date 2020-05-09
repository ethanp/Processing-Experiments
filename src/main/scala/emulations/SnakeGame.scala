package emulations
import geometry.Line
import helpers.{MyPApplet, Runner}

/** Created 5/9/20 7:26 PM
 */
class SnakeGame extends MyPApplet {
  override protected def drawFrame(): Unit = {
    blackBackground()
    colors.Current.update(
      fill = colors.Solarized.Orange,
      stroke = colors.Solarized.Black,
      strokeWeight = 3
    )
    Line(
      from = geometry.Vector.Zero,
      to = geometry.Vector.Y * height / 2 + geometry.Vector.X * width / 2
    )
      .draw()
  }
}

object SnakeGame extends Runner {
  override def pAppletClass: Class[_] = classOf[SnakeGame]
}
