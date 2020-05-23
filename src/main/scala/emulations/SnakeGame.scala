package emulations
import animation.Every
import helpers.{MyPApplet, Runner}
import processing.core.PConstants
import processing.event.KeyEvent

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.Random

/** Created 5/9/20 7:26 PM
 */
class SnakeGame extends MyPApplet {

  private val NumRows = 10
  private val NumCols = 10

  private var snake = new Snake

  private def rowHeight = height / NumRows
  private def colWidth = width / NumCols


  private val atFrameRate = Every(400.millis)
  private var food = createFood()

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
    if (!snake.dead) {
      blackBackground()
      val deathCertificate: Option[String] = snake.move()
      snake.draw()
      food.draw()
      deathCertificate foreach snake.showDeathCertificate
    }
  }

  private var direction = "right"

  /** This thing prevents you from taking a u-turn over yourself. */
  private var lastDirectionMoved = "right"

  override def keyPressed(event: KeyEvent): Unit = {
    direction = event.getKeyCode match {
      case PConstants.UP if lastDirectionMoved != "down" => "up"
      case PConstants.DOWN if lastDirectionMoved != "up" => "down"
      case PConstants.LEFT if lastDirectionMoved != "right" => "left"
      case PConstants.RIGHT if lastDirectionMoved != "left" => "right"
      case _ => direction
    }
    event.getKey match {
      case 'w' if lastDirectionMoved != "down" => direction = "up"
      case 's' if lastDirectionMoved != "up" => direction = "down"
      case 'a' if lastDirectionMoved != "right" => direction = "left"
      case 'd' if lastDirectionMoved != "left" => direction = "right"

      case 'r' => // restart game
        snake = new Snake
        food = createFood()

      case _ =>
    }
  }

  class Snake(var dead: Boolean = false) {

    private var points: Int = 0

    private val body = mutable.ArrayDeque[SnakeCell](
      elems = SnakeCell(
        Random nextInt NumRows,
        Random nextInt NumCols
      )
    )

    private var growing = 0

    /** Returns cause of death if applicable. */
    def move(): Option[String] = {
      lastDirectionMoved = direction

      val newLoc = direction match {
        case "up" => body.last.copy(row = body.last.row - 1)
        case "down" => body.last.copy(row = body.last.row + 1)
        case "left" => body.last.copy(col = body.last.col - 1)
        case "right" => body.last.copy(col = body.last.col + 1)
      }

      val died: Option[String] = newLoc match {
        case _ if body contains newLoc => Option("Ran over self and died")
        case _ if newLoc.isOutOfBounds => Option("Hit the wall and died")
        case _ => None
      }

      if (died.nonEmpty) {
        dead = true
        return died
      }

      body += newLoc

      growing match {
        case 0 => body.removeHead()
        case _ => growing -= 1
      }

      // Ate the food.
      if (body.last covers food) {
        food = createFood()
        points += 1
        growing += 2
      }

      textSize(height / 25)
      text(s"Points: $points", width / 30, height / 20)

      None
    }

    def showDeathCertificate(deathString: String): Unit = {
      colors.Current.update(
        fill = colors.Pure.Red,
        stroke = colors.Pure.Black
      )
      val fullString = Seq(deathString, s"Points: $points") mkString "\n"
      textSize(width / 15)
      val txtWidth = textWidth(fullString)
      val txtHeight = textAscent() + textDescent()
      text(fullString, width / 2 - txtWidth / 2, height / 2 - txtHeight / 2)
    }

    def draw(): Unit = {
      for (cell <- body)
        cell.draw()
    }

    def covers(cell: Cell): Boolean = body exists (_ covers cell)
  }

  case class SnakeCell(row: Int, col: Int) extends Cell {
    // Each snake cell could be part of a color scheme; not always the same color
    override val color: colors.ColorState = colors.ColorState(
      fill = colors.Solarized.Yellow,
      stroke = colors.Solarized.Black,
      strokeWeight = 3
    )
  }

  case class FoodCell(row: Int, col: Int) extends Cell {
    // Each food cell could be part of a color scheme; not always the same color
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
