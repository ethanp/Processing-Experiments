package emulations
import animation.Every
import helpers.{MyPApplet, Runner}
import processing.core.PConstants
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

  private val atFrameRate = Every(200.millis)
  private var food = createFood()
  private var points = 0

  override def settings(): Unit = size(800, 800)

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

  private var direction = "right"

  /** This thing prevents you from taking a u-turn over yourself. */
  private var lastTurnsDirection = "right"


  override def keyPressed(event: KeyEvent): Unit = {
    direction = event.getKeyCode match {
      case PConstants.UP if lastTurnsDirection != "down" => "up"
      case PConstants.DOWN if lastTurnsDirection != "up" => "down"
      case PConstants.LEFT if lastTurnsDirection != "right" => "left"
      case PConstants.RIGHT if lastTurnsDirection != "left" => "right"
      case _ => direction
    }
  }

  class Snake {
    private val body = mutable.ArrayDeque[SnakeCell](
      elems = SnakeCell(
        Random nextInt NumRows,
        Random nextInt NumCols
      )
    )

    private var growing = 0
    def move(): Unit = {
      direction match {
        case "up" => body += body.last.copy(row = body.last.row - 1)
        case "down" => body += body.last.copy(row = body.last.row + 1)
        case "left" => body += body.last.copy(col = body.last.col - 1)
        case "right" => body += body.last.copy(col = body.last.col + 1)
      }
      lastTurnsDirection = direction

      if (growing == 0) {
        body.removeHead()
      } else {
        growing -= 1
      }

      // Ate the food.
      if (body.last covers food) {
        food = createFood()
        points += 1
        growing += 2
      }
      textSize(height / 25)
      text(s"Points: $points", width / 20, height / 10)

      if (body.init contains body.last) {
        println("DEAD: Ran over self")
      }

      // Hit a wall.
      if (body.last.isOutOfBounds) {
        println("DEAD: Out of bounds")
      }
    }

    def draw(): Unit = {
      for (cell <- body)
        cell.draw()
    }

    def covers(cell: Cell): Boolean = body exists (_ covers cell)
  }

  case class SnakeCell(row: Int, col: Int) extends Cell {
    // TODO each snake cell should be part of a color scheme, not always the same color
    override val color: colors.ColorState = colors.ColorState(
      fill = colors.Solarized.Orange,
      stroke = colors.Solarized.Black,
      strokeWeight = 3
    )
  }

  case class FoodCell(row: Int, col: Int) extends Cell {
    // TODO each food should be generated from a color scheme, not always the same color
    override val color: colors.ColorState = colors.ColorState(
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

    def covers(cell: Cell): Boolean =
      row == cell.row &&
        col == cell.col
  }
}

object SnakeGame extends Runner {
  override def pAppletClass: Class[_] = classOf[SnakeGame]
}
