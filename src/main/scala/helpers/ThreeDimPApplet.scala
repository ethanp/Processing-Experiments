package helpers
import processing.core.PConstants

import scala.collection.mutable

/** Created 3/25/20 12:08 AM
 */
//noinspection TypeAnnotation
trait ThreeDimPApplet extends MyPApplet {

  protected val gameObjects = mutable.ArrayBuffer.empty[GameObject]

  // TODO(big bug): click-to-save is not working properly for these.

  protected def vantagePoint = geometry.Vector(
    x = 0,
    y = 0,
    // This is roughly what the default is.
    z = height
  )

  //noinspection RedundantDefaultArgument
  // This MUST be lazy otherwise it gets the wrong values.
  private lazy val focusPoint = geometry.Vector.Zero

  // TODO create those sliders that move the camera around.
  //  Or install PeasyCam like The Coding Train guy does.
  def lookAt(
    vantagePoint: geometry.Vector,
    focusPoint: geometry.Vector,
    upDir: geometry.Vector = geometry.Vector.Y
  ): Unit = {
    // NB: the camera() method is based on gluLookAt, which has a nice visual description:
    //
    //  https://stackoverflow.com/a/5721110/1959155
    //
    // In particular, note that the up-dir actually used will be "tilted forward or
    // backward" based on the vector from vantage point to focus point.
    camera(
      vantagePoint.x, vantagePoint.y, vantagePoint.z,
      focusPoint.x, focusPoint.y, focusPoint.z,
      upDir.x, upDir.y, upDir.z
    )
  }

  protected def moveCamera(): Unit = lookAt(
    vantagePoint = vantagePoint,
    focusPoint = focusPoint,
  )

  /** Default implementation. Override as desired. */
  override def drawFrame(): Unit = {
    assert(is3D, s"Class ${ getClass.getName } must be defined to be 3-Dimensional.")

    blackBackground()

    beginCamera()
    moveCamera()
    translate(-width / 2, -height / 2, 0)
    endCamera()

    translate(width / 2, height / 2, 0)

    gameObjects foreach (_.tick())
    gameObjects foreach { obj =>
      withPushedMatrix {
        obj.drawFromCenter()
      }
    }
  }

  override def settings(): Unit = {
    pixelDensity = 2 // This is better when displaying on retina
    fullScreen(PConstants.P3D)
  }

  override def setup(): Unit = {
    // NB: These hints trade performance for visual quality.
    //  https://processing.org/reference/hint_.html
    hint(PConstants.DISABLE_DEPTH_TEST)
    hint(PConstants.ENABLE_STROKE_PERSPECTIVE)
  }

  // For the scala compiler, by 'lifting' this method into this class,
  // it disambiguates which overload should be called in the (Float, Float, Float)
  // case (i.e. it should be THIS overload).
  final override def translate(x: Float, y: Float, z: Float): Unit =
    super.translate(x, y, z)

  def translate(x: Double, y: Double, z: Double): Unit =
    ThreeDimPApplet.super.translate(x.toFloat, y.toFloat, z.toFloat)
}
