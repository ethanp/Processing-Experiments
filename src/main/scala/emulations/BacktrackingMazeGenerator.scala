package emulations
import animation.SlowFrameRate
import helpers.{MyPApplet, Runner}

import scala.collection.mutable
import scala.util.Random

/** Created 6/7/20 11:04 AM
 *
 * Based on https://www.youtube.com/watch?v=HyK_Q5rrcr4.
 */
class BacktrackingMazeGenerator extends MyPApplet {
  private val NumRows = 10
  private val NumCols = 10

  private val rows =
    (0 until NumRows)
      .map(r => (0 until NumCols)
        .map(c => Cell(r, c)))

  private val atFrameRate = SlowFrameRate(framesPerSec = 60)

  override protected def drawFrame(): Unit =
    atFrameRate run {
      if (rows exists (_ exists (!_.wasVisited))) {
        gotoNextCell()
      } else {
        println("Done!")
        current.isCurrent = false
        atFrameRate.cancel()
      }
      renderGrid()
    }

  private val stack = mutable.Stack.empty[Cell]

  private var current: Cell = _
  private def setCurrent(cell: Cell): Unit = {
    if (current != null) {
      current.isCurrent = false
    }
    current = cell
    current.isCurrent = true
    current.wasVisited = true
    stack push current
  }

  private def gotoNextCell(): Unit = {
    if (current == null) {
      setCurrent(rows.head.head)
      return
    }

    val next = current.randomAvailableNeighbor
    if (next.nonEmpty) {
      current removeWallTo next.get
      setCurrent(next.get)
      return
    }

    stack.pop()
    setCurrent(stack.pop())
  }

  private def renderGrid(): Unit = rows foreach (_ foreach (_.draw()))

  case class Cell(rowIdx: Int, colIdx: Int) {
    var isCurrent = false
    var wasVisited = false
    var hasLeftWall = true
    var hasTopWall = true
    var hasRightWall = true
    var hasBottomWall = true

    private def leftNeighbor: Option[Cell] = if (colIdx > 0) Some(rows(rowIdx)(colIdx - 1)) else None
    private def upNeighbor: Option[Cell] = if (rowIdx > 0) Some(rows(rowIdx - 1)(colIdx)) else None
    private def rightNeighbor: Option[Cell] = if (colIdx < NumCols - 1) Some(rows(rowIdx)(colIdx + 1)) else None
    private def downNeighbor: Option[Cell] = if (rowIdx < NumRows - 1) Some(rows(rowIdx + 1)(colIdx)) else None
    private def neighbors: Seq[Cell] = Seq(leftNeighbor, upNeighbor, rightNeighbor, downNeighbor).flatten

    def randomAvailableNeighbor: Option[Cell] = {
      val validNeighbors = neighbors filterNot (_.wasVisited)
      val shuffledNeighbors = Random shuffle validNeighbors
      shuffledNeighbors.headOption
    }

    def removeWallTo(other: Cell): Unit = {
      if (rowIdx < other.rowIdx) {
        hasBottomWall = false
        other.hasTopWall = false
      } else if (rowIdx > other.rowIdx) {
        hasTopWall = false
        other.hasBottomWall = false
      } else if (colIdx < other.colIdx) {
        hasRightWall = false
        other.hasLeftWall = false
      } else if (colIdx > other.colIdx) {
        hasLeftWall = false
        other.hasRightWall = false
      } else {
        throw new RuntimeException("Unreachable")
      }
    }

    def draw(): Unit = {
      colors.Current.update(
        fill = (isCurrent, wasVisited) match {
          case (true, _) => colors.Solarized.Orange
          case (_, true) => colors.Solarized.Cyan
          case _ => colors.Solarized.White
        },
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
        stroke = colors.Solarized.Black,
        strokeWeight = 4
      )

      if (hasLeftWall) {
        geometry.Line(
          from = rect.leftTop,
          to = rect.leftBottom
        ).draw()
      }
      if (hasTopWall) {
        geometry.Line(
          from = rect.leftTop,
          to = rect.rightTop
        ).draw()
      }
      if (hasRightWall) {
        geometry.Line(
          from = rect.rightTop,
          to = rect.rightBottom
        ).draw()
      }
      if (hasBottomWall) {
        geometry.Line(
          from = rect.rightBottom,
          to = rect.leftBottom
        )
      }
    }
  }
}

object BacktrackingMazeGenerator extends Runner[BacktrackingMazeGenerator]
