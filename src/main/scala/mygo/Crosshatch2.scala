package mygo

import helpers.{MyPApplet, Runner}

/** Created 5/24/20 9:20 PM
 */
class Crosshatch2 extends MyPApplet {
  override def settings(): Unit = size(700, 700)

  override protected def drawFrame(): Unit = {
    background(100f)
    /* Down at angle */
    Crosshatch(
      from = geometry.Vector(200, 50),
      to = geometry.Vector(400, 500),
      color = colors.Solarized.Violet,
      width = width / 20,
      lineCount = 10
    ).draw()
  }

  case class Crosshatch(
    from: geometry.Vector,
    to: geometry.Vector,
    color: colors.Color,
    width: Double,
    lineCount: Int
  ) {
    def draw(): Unit = withPushedMatrix {
      colors.Current.update(
        fill = colors.Empty,
        stroke = color,
      )

      translate(from.x, from.y)
      rotate((to - from).radiansOffHorizontal)

      for (lineIdx <- 1 to lineCount) {
        val length = from dist to
        val y = (-width / 2) + width / lineCount * lineIdx
        geometry.Line(
          from = geometry.Vector(0, y),
          to = geometry.Vector(length, y)
        ).draw()
      }
    }
  }
}

object Crosshatch2 extends Runner {
  override def pAppletClass: Class[_] = classOf[Crosshatch2]
}

