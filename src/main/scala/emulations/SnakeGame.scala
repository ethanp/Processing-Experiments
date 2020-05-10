package emulations
import animation.Every
import helpers.{MyPApplet, Runner}

import scala.collection.mutable
import scala.util.Random

/** Created 5/9/20 7:26 PM
 */
class SnakeGame extends MyPApplet {

  private val NumRows = 10
  private val NumCols = 10

  private val snake = new Snake

  private def rowHeight = height / NumRows
  private def colWidth = width / NumCols

  import scala.concurrent.duration._

  private val atFrameRate = Every(1.seconds)

  override protected def drawFrame(): Unit = atFrameRate.run {
    blackBackground()
    snake.move()
    snake.draw()
  }

  class Snake {
    // TODO this should be a linked list
    private val body = mutable.ArrayBuffer(
      Cell(
        Random nextInt NumRows,
        Random nextInt NumCols
      )
    )

    private var direction = "right"

    def move(): Unit = direction match {
      case "right" =>
        println("moving right")
        body += body.last.copy(col = body.last.col + 1)
        body.dropInPlace(1)
    }

    def draw(): Unit = {
      for (cell <- body)
        cell.draw()
    }
  }

  case class Cell(row: Int, col: Int) {
    def draw(): Unit = {
      // TODO each cell should be a different color
      colors.Current.update(
        fill = colors.Solarized.Orange,
        stroke = colors.Solarized.Black,
        strokeWeight = 3
      )
      geometry.Rectangle(
        left = col * colWidth,
        top = row * rowHeight,
        width = colWidth,
        height = rowHeight
      ).draw()
    }
  }
}

object SnakeGame extends Runner {
  override def pAppletClass: Class[_] = classOf[SnakeGame]
}
