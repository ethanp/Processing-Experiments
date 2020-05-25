package helpers
import geometry.Vector
import helpers.controls.Fader
import processing.core.PApplet
import processing.event.MouseEvent

import scala.collection.mutable


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

  /** ************ GIF SAVING **************/
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

  /* ****** ENABLE FADERS BY DEFAULT (you still have to draw them though!) */
  val faders: mutable.ListBuffer[Fader] = mutable.ListBuffer.empty
  override def mousePressed(click: MouseEvent): Unit = faders foreach (_ mousePressed click)
  override def mouseDragged(event: MouseEvent): Unit = faders foreach (_ mouseDragged event)
  override def mouseReleased(event: MouseEvent): Unit = faders foreach (_ mouseReleased event)
}
