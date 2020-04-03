import helpers.MyPApplet
import processing.core.PConstants

/** Created 3/29/20 11:16 PM
 */
package object colors {
  abstract class Color(
    protected val v1: Float,
    protected val v2: Float,
    protected val v3: Float,
    protected val v4: Float,
  ) {
    protected def myColorMode: Int

    final def stroke()(implicit pApplet: MyPApplet): Color = apply(pApplet.stroke)
    final def fill()(implicit pApplet: MyPApplet): Color = apply(pApplet.fill)
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

  sealed case class Rgb(
    r: Float,
    g: Float,
    b: Float,
    a: Float = 100
  ) extends Color(r, g, b, a) {
    override protected def myColorMode: Int = PConstants.RGB
  }
}
