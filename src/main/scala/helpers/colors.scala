import helpers.MyPApplet
import processing.core.{PApplet, PConstants}

/** Created 3/29/20 11:16 PM
 */
package object colors {
  abstract class Color(
    v1: Float,
    v2: Float,
    v3: Float,
    v4: Float,
  ) {
    protected def myColorMode: Int

    def stroke()(implicit pApplet: MyPApplet): Color = apply(pApplet.stroke)
    def fill()(implicit pApplet: MyPApplet): Color = apply(pApplet.fill)
    final def background()(implicit pApplet: MyPApplet): Color = apply(pApplet.background)

    private def apply(
      f: (Float, Float, Float, Float) => Unit
    )(
      implicit pApplet: MyPApplet
    ): Color = {
      pApplet.colorMode(myColorMode, 100)
      f(v1, v2, v3, v4)
      this
    }
  }

  case class Hsb(
    h: Float,
    s: Float = 100,
    b: Float = 100,
    a: Float = 100,
  ) extends Color(h, s, b, a) {
    override protected def myColorMode: Int = PConstants.HSB
  }

  // Based on values listed at: https://www.wikiwand.com/en/Solarized_(color_scheme)
  object Solarized {
    object Black extends Rgb(0, 18, 21)
    object Cyan extends Rgb(42, 161, 152)
    object Orange extends Rgb(203, 75, 22)
    object Red extends Rgb(220, 50, 47)
    object White extends Rgb(238, 232, 213)
    object Yellow extends Rgb(181, 137, 0)
  }

  object Pure {
    object Green extends Rgb(0, 100, 0)
    object Red extends Rgb(100, 0, 0)
    object Black extends Rgb(0, 0, 0)
    object White extends Rgb(100, 100, 100)
  }

  sealed case class Rgb(
    r: Float,
    g: Float,
    b: Float,
    a: Float = 100
  ) extends Color(r, g, b, a) {
    override protected def myColorMode: Int = PConstants.RGB
  }

  object Empty extends Color(0, 0, 0, 0) {
    override protected def myColorMode: Int = throw new UnsupportedOperationException
    override def fill()(implicit pApplet: MyPApplet): Color = {
      pApplet.noFill()
      this
    }
    override def stroke()(implicit pApplet: MyPApplet): Color = {
      pApplet.noStroke()
      this
    }
  }

  def set(fill: Color, stroke: Color)(implicit pApplet: MyPApplet): Unit = {
    fill.fill()
    stroke.stroke()
  }

  def lerp(from: Rgb, to: Rgb, ratio: Double): Color = {
    def lerp(f: Rgb => Float): Float = PApplet.lerp(f(from), f(to), ratio.toFloat)
    Rgb(lerp(_.r), lerp(_.g), lerp(_.b))
  }
}
