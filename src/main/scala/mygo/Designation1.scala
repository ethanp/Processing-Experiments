package mygo
import helpers.{MyPApplet, Runner}

/** Created 4/4/20 2:18 PM
 */
class Designation1 extends MyPApplet {
  private val WindowSideLength = 800

  private val NumRows = 10
  private val NumCols = 10

  private val ColWidth = WindowSideLength / NumCols * (2f / 3)
  private val RectRightSpace = ColWidth / 5f

  private val RowHeight = WindowSideLength / NumRows * (2f / 3)
  private val RectBottomSpace = RowHeight / 5f

  override def settings(): Unit = size(WindowSideLength, WindowSideLength)
  override def draw(): Unit = {
    colors.Solarized.Black.background()
    for (row <- 1 to NumRows; col <- 1 to NumCols) {
      colors.set(
        fill = colors.lerp(
          from = colors.Solarized.Cyan,
          to = colors.Solarized.Red,
          ratio = row.toFloat / 10
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
