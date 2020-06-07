package emulations
import helpers.{MyPApplet, Runner}

/** Created 6/7/20 11:04 AM
 *
 * Based on https://www.youtube.com/watch?v=HyK_Q5rrcr4.
 */
class BacktrackingMazeGenerator extends MyPApplet {
  private val NumRows = 10
  private val NumCols = 10

  private val rows =
    for (rowIdx <- 0 until NumRows)
      yield for (colIdx <- 0 until NumCols)
        yield Cell(rowIdx, colIdx)

  override protected def drawFrame(): Unit = {
    for (row <- rows; cell <- row) {
      cell.draw()
    }
  }

  case class Cell(rowIdx: Int, colIdx: Int) {
    private var left = true
    private var top = true
    private var right = true
    private var bottom = true

    def draw(): Unit = {
      colors.Current.update(
        fill = colors.Solarized.White,
        stroke = colors.Empty
      )
      val cellWidth = width / NumCols
      val cellHeight = height / NumRows
      val rect = geometry.Rectangle(
        left = cellWidth * colIdx,
        top = cellHeight * rowIdx,
        width = cellWidth,
        height = cellHeight
      )
      rect.draw()

      /* Draw walls. */
      colors.Current.update(
        fill = colors.Solarized.Black,
        stroke = colors.Solarized.Black
      )

      if (left) {
        geometry.Line(
          from = rect.leftTop,
          to = rect.leftBottom
        ).draw()
      }
      if (top) {
        geometry.Line(
          from = rect.leftTop,
          to = rect.rightTop
        ).draw()
      }
      if (right) {
        geometry.Line(
          from = rect.rightTop,
          to = rect.rightBottom
        ).draw()
      }
      if (bottom) {
        geometry.Line(
          from = rect.rightBottom,
          to = rect.leftBottom
        )
      }
    }
  }
}

object BacktrackingMazeGenerator extends Runner[BacktrackingMazeGenerator]
