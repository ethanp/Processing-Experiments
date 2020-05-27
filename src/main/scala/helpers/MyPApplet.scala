package helpers
import helpers.controls.Fader
import processing.core.PApplet
import processing.event.MouseEvent

import scala.collection.mutable


/**
 * This trait is meant to be generally-useful enough to be extended by all Processing
 * applications that I create.
 *
 * To make 3D apps, it is recommended to extend the [[ThreeDimPApplet]] subclass instead.
 *
 * Created 3/29/20 11:28 AM
 */
trait MyPApplet extends PApplet {
  implicit val app: MyPApplet = this


  /** ************* RENAMINGS ************* **/

  def blackBackground(): Unit = background(0f)

  final def is3D: Boolean = sketchRenderer() contains "3D"


  /** ************* User Interaction ************* **/

  override def mouseClicked(): Unit = ImageSaver.save(className = getClass.getSimpleName)


  /** ************* GEOMETRY ************* **/

  protected final def withPushedMatrix(block: => Unit): Unit = {
    pushMatrix()
    block
    popMatrix()
  }

  /** ************ GIF SAVING ************ **/
  /** Override this to enable gif saving. */
  protected def gifLength: Int = GifSaver.Disabled

  protected lazy val gifSaver = new GifSaver(
    className = getClass.getSimpleName,
    gifLength = gifLength
  )

  protected def drawFrame(): Unit

  protected def afterDraw(): Unit = gifSaver.addFrame(frameCount, saveFrame)

  override final def draw(): Unit = {
    drawFrame()
    afterDraw()
  }

  /** ** ENABLE FADERS BY DEFAULT (you still have to draw them though!) ** **/

  // MedPriorityTodo: It'd be great to not render these on the frame saves.
  //  One way to maybe do that is to use right-click-drag to select a sub-rect
  //  of the full frame to save to the png. Though not sure how doable that is.
  //  Another way is to have a boolean field `isSaving` that would allow you to
  //  skip drawing the faders on the next round, and then would call the ImageSaver
  //  on that next frame.
  val faders: mutable.ListBuffer[Fader] = mutable.ListBuffer.empty
  override def mousePressed(click: MouseEvent): Unit = faders foreach (_ mousePressed click)
  override def mouseDragged(event: MouseEvent): Unit = faders foreach (_ mouseDragged event)
  override def mouseReleased(event: MouseEvent): Unit = faders foreach (_ mouseReleased event)
}
