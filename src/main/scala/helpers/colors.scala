import helpers.MyPApplet
import processing.core.PConstants

/** Created 3/29/20 11:16 PM
 */
package object colors {
  sealed trait Color {
    def stroke()(implicit myPApplet: MyPApplet): Color
    def fill()(implicit myPApplet: MyPApplet): Color
  }

  case class Hsb(h: Float, s: Float = 100, b: Float = 100, a: Float = 100) extends Color {
    override def stroke()(implicit myPApplet: MyPApplet): Color = {
      myPApplet.colorMode(PConstants.HSB, 100)
      myPApplet.stroke(h, s, b, a)
      this
    }
    override def fill()(implicit myPApplet: MyPApplet): Color = {
      myPApplet.colorMode(PConstants.HSB, 100)
      myPApplet.fill(h, s, b, a)
      this
    }
  }

  // source: https://www.wikiwand.com/en/Solarized_(color_scheme)
  object Solarized {
    object Black extends Rgb(0, 54, 66)
    object Yellow extends Rgb(181, 137, 0)
    object White extends Rgb(238, 232, 213)
    object Red extends Rgb(220, 50, 47)
  }

  object Pure {
    object Green extends Rgb(0, 100, 0)
    object Red extends Rgb(100, 0, 0)
    object Black extends Rgb(0, 0, 0)
    object White extends Rgb(100, 100, 100)
  }

  sealed case class Rgb(r: Float, g: Float, b: Float, a: Float = 100) extends Color {
    override def stroke()(implicit myPApplet: MyPApplet): Color = {
      myPApplet.colorMode(PConstants.RGB, 100)
      myPApplet.stroke(r, g, b, a)
      this
    }
    override def fill()(implicit myPApplet: MyPApplet): Color = {
      myPApplet.colorMode(PConstants.RGB, 100)
      myPApplet.fill(r, g, b, a)
      this
    }
  }
}
