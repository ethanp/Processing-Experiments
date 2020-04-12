package mygo
import helpers.{MyPApplet, Runner}

/**
 * This really didn't turn out as good as I wanted it to.
 *
 * Created 4/4/20 2:18 PM
 * Updated 4/12/20
 */
class Designation1 extends MyPApplet {
  private val WindowSide: Int = 800
  private val Margin: Float = 50f

  private val NumRows: Int = 20
  private val NumCols: Int = 10

  private val ColoredLen: Float = WindowSide - Margin * 2

  private val RectWidth: Float = 8f * ColoredLen / (9 * NumCols - 1)
  private val RectRightSpace: Float = RectWidth / 6f
  private val ColWidth: Float = RectWidth + RectRightSpace

  private val RectHeight: Float = 8f * ColoredLen / (9 * NumRows - 1)
  private val RectBottomSpace: Float = RectHeight / 8f
  private val RowHeight: Float = RectHeight + RectBottomSpace

  {
    val CalculatedColoredWidth: Float = RectWidth * NumCols
    assert(
      assertion = (CalculatedColoredWidth - ColoredLen).abs < 100,
      message = s"Somewhere width math is wrong: $CalculatedColoredWidth vs $ColoredLen"
    )
  }

  override def settings(): Unit = size(WindowSide, WindowSide)

  override def draw(): Unit = {
    def setColor(row: Int, col: Int): Unit = {
      colors.Current.update(
        fill = colors.lerp(
          from = colors.Solarized.Cyan,
          to = colors.Solarized.Red,
          ratio = row.toFloat / NumRows
        ).copy(
          a = (col + 3).toFloat / NumCols * colors.MaxVal
        ),
        stroke = colors.Empty
      )
    }

    def drawRect(xNow: Float, yNow: Float): Unit = {
      geometry.Rectangle(
        left = xNow,
        top = yNow,
        width = RectWidth,
        height = RectHeight
      ).draw()
    }

    colors.Solarized.Black.background()
    var xNow = Margin
    var yNow = Margin
    for (row <- 1 to NumRows) {
      for (col <- 1 to NumCols) {
        setColor(row, col)
        drawRect(xNow, yNow)
        xNow += ColWidth
      }
      xNow = Margin
      yNow += RowHeight
    }
  }
}

object Designation1 extends Runner {
  override def pAppletClass: Class[_] = classOf[Designation1]
}
