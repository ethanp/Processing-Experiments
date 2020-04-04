import helpers.MyPApplet
import processing.core.{PApplet, PConstants}

/** Created 3/29/20 11:16 PM
 */
package object colors {
  val MaxVal = 100

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
      pApplet.colorMode(myColorMode, MaxVal)
      f(v1, v2, v3, v4)
      this
    }
  }

  case class Hsb(
    h: Float,
    s: Float = MaxVal,
    b: Float = MaxVal,
    a: Float = MaxVal,
  ) extends Color(h, s, b, a) {
    override protected def myColorMode: Int = PConstants.HSB
  }

  // Based on values listed at: https://www.wikiwand.com/en/Solarized_(color_scheme)
  object Solarized {
    private def scale(int: Int) = int / 255f * 100f
    object Black extends Rgb(scale(0), scale(43), scale(54))
    object Cyan extends Rgb(scale(42), scale(161), scale(152))
    object Orange extends Rgb(scale(203), scale(75), scale(22))
    object Red extends Rgb(scale(220), scale(50), scale(47))
    object White extends Rgb(scale(238), scale(232), scale(213))
    object Yellow extends Rgb(scale(181), scale(137), scale(0))
  }

  object Pure {
    object Green extends Rgb(0, MaxVal, 0)
    object Red extends Rgb(MaxVal, 0, 0)
    object Black extends Rgb(0, 0, 0)
    object White extends Rgb(MaxVal, MaxVal, MaxVal)
  }

  sealed case class Rgb(
    r: Float,
    g: Float,
    b: Float,
    a: Float = MaxVal
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

  def lerp(from: Rgb, to: Rgb, ratio: Double): Rgb = {
    def lerp(f: Rgb => Float): Float = PApplet.lerp(f(from), f(to), ratio.toFloat)
    Rgb(lerp(_.r), lerp(_.g), lerp(_.b))
  }
}
