package mygo
import helpers.{MyPApplet, Runner}

/** Created 5/23/20 2:33 PM
 */
class Crosshatch1 extends MyPApplet {
  override def settings(): Unit = {
    pixelDensity = 2 // This is better when displaying on retina
    size(700, 700)
  }

  override protected def drawFrame(): Unit = {
    background(100f)
    for (idx <- 1 to width / 10) {
      /* Downward */
      colors.Current.update(
        fill = colors.Empty,
        stroke = colors.Solarized.Violet.copy(a = 80),
      )
      geometry.Line(
        from = geometry.Vector(x = idx * 3, y = 0),
        to = geometry.Vector(x = idx * 3, y = height / 3)
      ).draw()

      /* Diagonal */
      colors.Current.update(
        fill = colors.Empty,
        stroke = colors.Solarized.Red.copy(a = 80),
        strokeWeight = 2
      )
      geometry.Line(
        from = geometry.Vector(x = idx * 3, y = 0),
        to = geometry.Vector(x = idx * 3 + 30, y = height / 2)
      ).draw()

      /* Sideways */
      colors.Current.update(
        fill = colors.Empty,
        stroke = colors.Solarized.Black.copy(a = 80),
      )
      geometry.Line(
        from = geometry.Vector(x = 0, y = idx * 3),
        to = geometry.Vector(x = width / 1.5, y = idx * 3 + 30)
      ).draw()
    }
  }
}

object Crosshatch1 extends Runner {
  override def pAppletClass: Class[_] = classOf[Crosshatch1]
}
