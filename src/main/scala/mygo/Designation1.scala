package mygo
import helpers.{MyPApplet, Runner}

/** Created 4/4/20 2:18 PM
 */
class Designation1 extends MyPApplet {
  private val WindowSideLength = 800

  override def settings(): Unit = size(WindowSideLength, WindowSideLength)
  override def draw(): Unit = {
    colors.Solarized.Black.background()
    for {
      row <- 1 to 10
        col <- 1 to 10
    } {
      colors.set(
        fill = colors.lerp(
          from = colors.Solarized.Cyan,
          to = colors.Solarized.Red,
          ratio = (row + col).toFloat / 20
        ),
        stroke = colors.Empty
      )
      geometry.Rectangle(
        left = col * 30,
        top = row * 15,
        width = 20,
        height = 5
      ).draw()
    }
  }
}

object Designation1 extends Runner {
  override def pAppletClass: Class[_] = classOf[Designation1]
}
