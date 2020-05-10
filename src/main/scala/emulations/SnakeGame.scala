package emulations
import animation.Every
import helpers.{MyPApplet, Runner}
import processing.event.KeyEvent

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
  private var food = createFood()
  private var points = 0

  private def createFood(): FoodCell = {
    def randomCell() = FoodCell(
      Random nextInt NumRows,
      Random nextInt NumCols
    )

    // Surely there's a "functional" way of doing this,
    // E.g. RandomLocationGenerator.takeWhile(snake.covers)
    var loc: FoodCell = randomCell()
    while (snake covers loc) {
      loc = randomCell()
    }
    loc
  }

  override protected def drawFrame(): Unit = atFrameRate.run {
    blackBackground()
    snake.move()
    snake.draw()
    food.draw()
  }

  override def keyPressed(event: KeyEvent): Unit = {
    // TODO How do I match the up arrow in processing?
    event.getKey match {
      case ' ' => ???
    }
  }

  class Snake {
    private val body = mutable.ArrayDeque[SnakeCell](
      elems = SnakeCell(
        Random nextInt NumRows,
        Random nextInt NumCols
      )
    )

    // TODO capture direction changes from the user
    private var direction = "right"

    def move(): Unit = direction match {
      case "right" =>
        println("moving right")
        body += body.last.copy(col = body.last.col + 1)
        body.dropInPlace(1)

        // Ate the food.
        if (body.last == food) {
          food = createFood()
          points += 1
          println(s"Points: $points")
        }

        // Hit a wall.
        if (body.last.isOutOfBounds) {
          println("You died!")
        }
    }

    def draw(): Unit = {
      for (cell <- body)
        cell.draw()
    }

    def covers(cell: Cell): Boolean = body contains cell
  }

  case class SnakeCell(row: Int, col: Int) extends Cell {
    // TODO each snake cell should be part of a color scheme, not always the same color
    override val color = colors.ColorState(
      fill = colors.Solarized.Orange,
      stroke = colors.Solarized.Black,
      strokeWeight = 3
    )
  }

  case class FoodCell(row: Int, col: Int) extends Cell {
    // TODO each food should be generated from a color scheme, not always the same color
    override val color = colors.ColorState(
      fill = colors.Solarized.White,
      stroke = colors.Solarized.Red,
      strokeWeight = 3
    )
  }

  trait Cell {
    val row: Int
    val col: Int
    val color: colors.ColorState

    def isOutOfBounds: Boolean =
      row < 0 || row >= NumRows ||
        col < 0 || col >= NumCols

    def draw(): Unit = {
      colors.Current update color
      geometry.Rectangle(
        left = col * colWidth,
        top = row * rowHeight,
        width = colWidth,
        height = rowHeight
      ).draw()
    }


    final override def equals(obj: Any): Boolean = obj match {
      case Cell(r, c, _) => row == r && col == c
      case _ => false
    }
  }
  object Cell {
    def unapply(arg: Cell): Option[(Int, Int, colors.ColorState)] = Option(
      (arg.row, arg.col, arg.color)
    )
  }
}

object SnakeGame extends Runner {
  override def pAppletClass: Class[_] = classOf[SnakeGame]
}
