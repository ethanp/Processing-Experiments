package mygo
import helpers.{MyPApplet, Runner}

/** Created 4/4/20 2:18 PM
 */
class Designation1 extends MyPApplet {
  private val WindowSideLength = 800

  private val NumRows = 20
  private val NumCols = 10

  private val ColWidth = WindowSideLength / NumCols - (WindowSideLength / 80f)
  private val RectRightSpace = ColWidth / 5f

  private val RowHeight = WindowSideLength / NumRows - (WindowSideLength / 80f)
  private val RectBottomSpace = RowHeight / 3f

  override def settings(): Unit = size(WindowSideLength, WindowSideLength)
  override def draw(): Unit = {
    colors.Solarized.Black.background()
    for (row <- 1 to NumRows; col <- 1 to NumCols) {
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
      geometry.Rectangle(
        left = col * ColWidth,
        top = row * RowHeight,
        width = ColWidth - RectRightSpace,
        height = RowHeight - RectBottomSpace
      ).draw()
    }
  }
}

object Designation1 extends Runner {
  override def pAppletClass: Class[_] = classOf[Designation1]
}
