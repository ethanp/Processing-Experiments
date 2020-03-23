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

  private val Height: Int = 600
  private val Width: Int = (Height * 1.8).toInt
  private val NumPoints: Int = 3000
  private val xCoords: Array[Int] = zeroes
  private val yCoords: Array[Int] = zeroes
  private val zCoords: Array[Int] = zeroes
  private val ExplodeSteps: Int = 200
  private var colors: Array[HsbValue] = randomColors
  private var phase: Int = 0
  private var explodeStep: Int = 0
  private var explodeMultipliers: Array[Array[Float]] = calculateExplodeMultipliers
  private var currRotationRadians: Float = 0f
  private var animationPaused: Boolean = false
  private var shouldRotateX: Boolean = true
  private var shouldRotateY: Boolean = true
  private var shouldRotateZ: Boolean = true
  override def settings(): Unit = fullScreen(PConstants.P3D)
  override def setup(): Unit = {
    frameRate(60)
    colorMode(HSB, 100)
  }
  override def draw(): Unit = if (!animationPaused) {
    background(15) // Clear the screen.

    withPushedMatrix {
      translate(width / 2, height / 2, 0)
      if (shouldRotateX) rotateX(currRotationRadians)
      if (shouldRotateY) rotateY(currRotationRadians)
      if (shouldRotateZ) rotateZ(currRotationRadians)
      forAllPointIndexes { pointIdx =>
        val HsbValue(h, s, b) = colors(pointIdx)
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
    }
    currRotationRadians += 0.005f

    val anyPointMoved = move()

    assert(phase >= 0) // crash ok. lol
    if (!anyPointMoved) {
      phase = (phase + 1) % 4
      if (phase == 0) {
        // Restarted.
        colors = randomColors
        explodeStep = 0
        explodeMultipliers = calculateExplodeMultipliers
      }
    }
  }
  /** Runs the given function within a pair of push/popMatrix calls */
  private def withPushedMatrix(fn: => Unit): Unit = {
    pushMatrix()
    fn
    popMatrix()
  }
  /** @return true iff any point moved. */
  private def move(): Boolean = phase match {
    case 0 => explode(Seq((0, xCoords), (1, yCoords), (2, zCoords)))
    case 1 => coalesceToZero(yCoords)
    case 2 => coalesceToZero(zCoords)
    case 3 => coalesceToZero(xCoords)
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
  private def explode(axes: Iterable[(Int, Array[Int])]): Boolean = {
    explodeStep += 1
    0 until NumPoints foreach { i =>
      axes foreach {
        case (axisIndex, coords) =>
          coords(i) = math round explodeStep * explodeMultipliers(i)(axisIndex)
      }
    }
    explodeStep < ExplodeSteps
  }
  private def calculateExplodeMultipliers: Array[Array[Float]] =
    forAllPointIndexes { _ =>
      def randomAngle = Random.nextDouble * math.Pi * 2

      val r = Random.nextDouble * Width
      val (x, y, z) = sphericalToCartesian(r, randomAngle, randomAngle)
      Seq(x, y, z).toArray.map(_ / ExplodeSteps)
    }

  def sphericalToCartesian(radius: Double, θ: Double, φ: Double): (Float, Float, Float) = {
    val x: Double = radius * sin(φ) * cos(θ)
    val y = radius * sin(φ) * sin(θ)
    val z = radius * cos(φ)
    (x.toFloat, y.toFloat, z.toFloat)
  }
  /** HSB values in range [0, 100]. */
  private def randomColors: Array[HsbValue] =
    forAllPointIndexes { _ => HsbValue(Random.nextInt(101), 100, 80 + Random.nextInt(21)) }
  private def forAllPointIndexes[A: ClassTag](fn: Int => A): Array[A] =
    (0 until NumPoints).toArray.map(fn)
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
  private def zeroes: Array[Int] = Array.fill(NumPoints)(0)
  private case class HsbValue(h: Int, s: Int, b: Int)
}

object DiminishingDimensions {
  def main(args: Array[String]): Unit = {
    PApplet.main(classOf[DiminishingDimensions])
  }
}
