package doit

import processing.core.PConstants.{PI, TRIANGLE_STRIP}
import processing.core.{PApplet, PConstants}

/** Created 3/22/20 4:52 PM
 */
class Terrain extends PApplet {
  /* This app came from https://github.com/dcbriccetti/scala-lessons/blob/master/src/proc/Terrain.scala
   * And has been modified to suit my programming style.
   */

  override def settings(): Unit = fullScreen(PConstants.P3D)

  override def draw(): Unit = {
    val spacing = 20
    lights() // Default 3D lighting. Includes directional and ambient etc.
    background(135, 206, 250) // Sky color.
    translate(width / 2, height / 2, 0)
    rotateX(PI / 3)
    translate(-width, -height, 0)
    fill(128, 200, 128) // Terrain
    noStroke()

    val range = 0 to width / spacing * 2
    for (y <- range) {
      beginShape(TRIANGLE_STRIP)
      for (x <- range) {
        vertex(spacing * x, spacing * y, scaledNoise(x, y))
        vertex(spacing * x, spacing * (y + 1), scaledNoise(x, y + 1))
      }
      endShape()
    }
  }

  private def scaledNoise(x: Int, y: Int) = {
    val smoothness = 10f
    val perlinNoiseValue = noise(x / smoothness, (y - frameCount) / smoothness)
    def asZCoord(cur: Float) = PApplet.map(cur, 0, 1, -100, 100)
    asZCoord(perlinNoiseValue)
  }
}


object Terrain {
  def main(args: Array[String]): Unit = {
    PApplet.main(classOf[Terrain])
  }
}
