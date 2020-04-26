package helpers
import geometry.Vector
import processing.core.PApplet


/** Created 3/29/20 11:28 AM
 */
trait MyPApplet extends PApplet {
  implicit val app: MyPApplet = this


  /* ************** RENAMINGS ***************/

  def blackBackground(): Unit = background(0f)

  final def is3D: Boolean = sketchRenderer() contains "3D"

  def mouse: Vector = geometry.Vector(mouseX, mouseY)


  /* ************** User Interaction ***************/

  override def mouseClicked(): Unit = ImageSaver.save(className = getClass.getSimpleName)


  /* ************** GEOMETRY ***************/

  final def withPushedMatrix(block: => Unit): Unit = {
    pushMatrix()
    block
    popMatrix()
  }

  final protected def fromTheCenter(block: => Unit): Unit = {
    withPushedMatrix {
      if (is3D) translate(width / 2, height / 2, 0)
      else translate(width / 2, height / 2)
      block
    }
  }

  /** ************ GIF SAVING **************/
  /** Override this to enable gif saving. */
  protected val gifLength: Int = GifSaver.Disabled

  protected val gifSaver = new GifSaver(
    className = getClass.getSimpleName,
    gifLength = gifLength
    // gifLength = 25
  )

  protected def drawFrame(): Unit

  protected def afterDraw(): Unit = gifSaver.addFrame(frameCount, saveFrame)

  override final def draw(): Unit = {
    drawFrame()
    afterDraw()
  }
}
