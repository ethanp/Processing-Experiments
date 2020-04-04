package helpers

import colors.{Pure, Solarized}
import geometry.{Rectangle, V, Vector}
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.value.ChangeListener
import processing.core.PConstants
import processing.event.MouseEvent

/** @inheritdoc
 */
class HScrollbar(
  override protected val initial: Float,
  override protected val min: Float,
  override protected val max: Float,
  override protected val listener: ChangeListener[Number],
  bounds: Rectangle,
)(implicit pApp: MyPApplet)
  extends Scrollbar {
  override def app: MyPApplet = pApp

  override protected def centerKnob(mouseX: Int): Unit =
    knob centerOnX mouseX withinWidth slider

  override protected def mouseDimLoc: MouseEvent => Int = _.getX

  // TODO incorporate `bounds` into size choices of both rectangles and the value label
  override protected val slider: Rectangle = Rectangle(
    leftTop = V(10, 35),
    widthHeight = V(200, 10)
  )

  val labelWidth = 20 // TODO use the real label-width instead of a random constant
  override protected val knob: Rectangle = Rectangle(
    leftTop = bounds.leftTop,
    widthHeight = V(bounds.width - labelWidth, bounds.height)
  )

  override protected def updateCurValue(): Unit = {
    val extent: Float = knob.left - slider.left
    val width: Float = slider.width - knob.width
    val prop: Float = extent / width
    floatProperty.setValue(prop * max)
  }
}

/** @inheritdoc
 */
class VScrollbar(
  override protected val initial: Float,
  override protected val min: Float,
  override protected val max: Float,
  override protected val listener: ChangeListener[Number]
)(implicit pApp: MyPApplet)
  extends Scrollbar {
  override def app: MyPApplet = pApp

  override protected def centerKnob(mouseCoord: Int): Unit =
    knob centerOnY mouseCoord withinHeight slider

  override protected def mouseDimLoc: MouseEvent => Int = _.getY

  override protected val slider: Rectangle = Rectangle(
    leftTop = V(35, 10),
    widthHeight = V(10, 200)
  )

  override protected val knob: Rectangle = Rectangle(
    leftTop = V(20, 10),
    widthHeight = V(40, 15)
  )

  override protected def updateCurValue(): Unit = {
    val extent: Float = knob.top - slider.top
    val height: Float = slider.height - knob.height
    val prop: Float = extent / height
    floatProperty.setValue(prop * max)
  }
}

/**
 * The following code is required when using this class
 *
 * {{{
 *
 * override def mousePressed(click: MouseEvent): Unit = scrollbar.mousePressed(click)
 * override def mouseDragged(event: MouseEvent): Unit = scrollbar.mouseDragged(event)
 * override def mouseReleased(event: MouseEvent): Unit = scrollbar.mouseReleased(event)
 * override def draw(): Unit = scrollbar.draw()
 *
 * }}}
 *
 * Admittedly this class didn't quite turn out that good lol. Any suggestions welcome :)
 * <p>
 * Created 3/29/20 11:23 PM
 */
trait Scrollbar {
  protected def initial: Float
  protected def min: Float
  protected def max: Float
  protected def listener: ChangeListener[Number]
  protected def app: MyPApplet
  implicit val iApp: MyPApplet = app

  val floatProperty: SimpleFloatProperty =
    new SimpleFloatProperty(initial) {
      addListener(listener)
    }

  //noinspection NotImplementedCode
  if (min != 0) ???

  protected val slider: Rectangle
  protected val knob: Rectangle

  private var isDragging = false

  def mousePressed(click: MouseEvent): Unit = {
    if (click.getButton == PConstants.LEFT) {
      val clickLoc = Vector(click.getX, click.getY)
      if (clickLoc isInside knob) {
        isDragging = true
      }
    }
  }

  protected def centerKnob(mouseX: Int): Unit

  private def innerRectDragged(mouseX: Int): Unit =
    if (isDragging) {
      centerKnob(mouseX)
      updateCurValue()
    }

  protected def updateCurValue(): Unit

  protected def mouseDimLoc: MouseEvent => Int

  def mouseDragged(event: MouseEvent): Unit = innerRectDragged(mouseDimLoc(event))

  def mouseReleased(event: MouseEvent): Unit = {
    innerRectDragged(mouseDimLoc(event))
    isDragging = false
  }

  def draw(): Unit = {
    app.strokeWeight(2)
    Pure.Black.stroke()
    Solarized.Red.fill()
    slider.draw()
    Solarized.Yellow.fill()
    knob.draw()
    Solarized.White.stroke().fill()
    app.textSize(24)

    // TODO the text location should differ for HScrollbar vs `VScrollbar`
    app.text(
      /*string=*/ f"${ floatProperty.get.floatValue }%.2f",
      /*left=*/ slider.right + 10,
      /*bottom??=*/ slider.bottom - slider.height / 3
    )
  }
}
