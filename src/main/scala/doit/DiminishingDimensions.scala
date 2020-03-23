package doit

import processing.core.PConstants.HSB
import processing.core.{PApplet, PConstants}
import processing.event.KeyEvent

import scala.math.{cos, sin}
import scala.reflect.ClassTag
import scala.util.Random

/** Created 3/22/20 11:01 PM
 */
class DiminishingDimensions extends PApplet {
  /* This came from https://github.com/dcbriccetti/scala-lessons/blob/master/src/proc/DiminishingDimensions.scala
   * and has been modified for my personal clarity.
   * */

  private val NumPoints: Int = 3000

  /** Point coordinates. */
  private val xCoords: Array[Int] = Array.fill(n = NumPoints)(elem = 0)
  private val yCoords: Array[Int] = Array.fill(n = NumPoints)(elem = 0)
  private val zCoords: Array[Int] = Array.fill(n = NumPoints)(elem = 0)

  /** How long the explosion lasts. */
  private val ExplodeSteps: Int = 200
  /** How var each point explodes. */
  private var explodeMultipliers: Array[Array[Float]] = generateNewExplodeMultipliers()
  /** How long we've exploded so far. */
  private var explodeStep: Int = 0

  private var pointColors: Array[HsbValue] = generateRandomColors()

  private var phase: Phase = Phases.Explode

  /** Slowly increments while animation is not paused. */
  private var currRotationRadians: Float = 0f

  /** Brings the whole simulation to a standstill. */
  private var animationPaused: Boolean = false

  private var shouldRotateX: Boolean = true
  private var shouldRotateY: Boolean = true
  private var shouldRotateZ: Boolean = true

  override def setup(): Unit = colorMode(HSB, 100)

  override def draw(): Unit = if (!animationPaused) {
    background(/*greyBrightness=*/ 15) // Clear the screen to a pretty dark color.

    withPushedMatrix {
      translate(width / 2, height / 2, 0)
      if (shouldRotateX) rotateX(currRotationRadians)
      if (shouldRotateY) rotateY(currRotationRadians)
      if (shouldRotateZ) rotateZ(currRotationRadians)

      drawPoints()
    }
    currRotationRadians += 0.005f

    val keepPhase = move()

    if (!keepPhase) {
      phase = phase.next
      if (phase == Phases.Explode) {
        // Restarted.
        pointColors = generateRandomColors()
        explodeStep = 0
        explodeMultipliers = generateNewExplodeMultipliers()
      }
    }
  }

  private def drawPoints(): Unit =
    forAllPointIndexes { pointIdx =>
      val HsbValue(h, s, b) = pointColors(pointIdx)
      stroke(h, s, b)
      fill(h, s, b)
      withPushedMatrix {
        translate(
          xCoords(pointIdx),
          yCoords(pointIdx),
          zCoords(pointIdx)
        )
        box(/*sideLength=*/ 3)
      }
    }
  /** Runs the given function within a pair of push/popMatrix calls */
  private def withPushedMatrix(fn: => Unit): Unit = {
    pushMatrix()
    fn
    popMatrix()
  }
  /** @return false iff phase should change. */
  private def move(): Boolean = phase match {
    case Phases.Explode => explode(Seq((0, xCoords), (1, yCoords), (2, zCoords)))
    case Phases.CoalesceY => coalesceToZero(yCoords)
    case Phases.CoalesceZ => coalesceToZero(zCoords)
    case Phases.CoalesceX => coalesceToZero(xCoords)
  }
  private def explode(axes: Iterable[(Int, Array[Int])]): Boolean = {
    explodeStep += 1
    for {
      pointIdx <- 0 until NumPoints
        (axisIndex, coords) <- axes
    } coords(pointIdx) = math round explodeStep * explodeMultipliers(pointIdx)(axisIndex)

    explodeStep < ExplodeSteps
  }
  /** @return true iff a point moved. */
  private def coalesceToZero(coords: Array[Int]) = {
    var anyPointChanged = false
    forAllPointIndexes { pointIdx =>
      val absVal: Int = math.abs(coords(pointIdx))
      val chg: Int = if (absVal == 0) 0 else coords(pointIdx) / absVal
      if (chg != 0) {
        coords(pointIdx) -= chg * (absVal min 3)
        anyPointChanged = true
      }
    }
    anyPointChanged
  }
  private def generateNewExplodeMultipliers(): Array[Array[Float]] =
    forAllPointIndexes { _ =>
      def randomAngle = Random.nextDouble * math.Pi * 2

      val r = Random.nextDouble * ExplosionDimensions.Width
      val (x, y, z) = sphericalToCartesian(r, randomAngle, randomAngle)
      Seq(x, y, z).toArray.map(_ / ExplodeSteps)
    }
  def sphericalToCartesian(radius: Double, θ: Double, φ: Double): (Float, Float, Float) = {
    val x: Double = radius * sin(φ) * cos(θ)
    val y = radius * sin(φ) * sin(θ)
    val z = radius * cos(φ)
    (x.toFloat, y.toFloat, z.toFloat)
  }
  private def forAllPointIndexes[A: ClassTag](fn: Int => A): Array[A] =
    (0 until NumPoints).toArray.map(fn)
  /** HSB values in range [0, 100]. */
  private def generateRandomColors(): Array[HsbValue] =
    forAllPointIndexes { _ => HsbValue(Random.nextInt(101), 100, 80 + Random.nextInt(21)) }
  override def settings(): Unit = fullScreen(PConstants.P3D)
  /** Callback for each key press. */
  override def keyPressed(event: KeyEvent): Unit = {
    commonKeyPressed(event)
    super.keyPressed(event)
  }
  /** Processes a KeyEvent */
  private def commonKeyPressed(event: KeyEvent): Unit =
    event.getKey match {
      case 'x' => shouldRotateX = !shouldRotateX
      case 'y' => shouldRotateY = !shouldRotateY
      case 'z' => shouldRotateZ = !shouldRotateZ
      case ' ' => animationPaused = !animationPaused
      case _ => // ignore.
    }
  sealed trait Phase {def next: Phase }
  private case class HsbValue(h: Int, s: Int, b: Int)
  object ExplosionDimensions {
    val Height: Int = 600
    val Width: Int = (Height * 1.8).toInt
  }
  case object Phases {
    case object Explode extends Phase {override def next: Phase = CoalesceY }
    case object CoalesceY extends Phase {override def next: Phase = CoalesceZ }
    case object CoalesceZ extends Phase {override def next: Phase = CoalesceX }
    case object CoalesceX extends Phase {override def next: Phase = Explode }
  }
}

object DiminishingDimensions {
  def main(args: Array[String]): Unit = {
    PApplet.main(classOf[DiminishingDimensions])
  }
}
