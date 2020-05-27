package helpers.controls

import colors.{Pure, Solarized}
import geometry.{Rectangle, Vector}
import helpers.MyPApplet
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.value.ChangeListener
import processing.core.PConstants
import processing.event.MouseEvent

/**
 * The following code is required when using this class.
 *
 * It is enabled by default in [[MyPApplet]] unless these methods
 * are overridden in a subclass.
 *
 * {{{
 *
 * override def mousePressed(click: MouseEvent): Unit = fader.mousePressed(click)
 * override def mouseDragged(event: MouseEvent): Unit = fader.mouseDragged(event)
 * override def mouseReleased(event: MouseEvent): Unit = fader.mouseReleased(event)
 * override def draw(): Unit = fader.draw()
 *
 * }}}
 *
 * Created 3/29/20 11:23 PM
 */
abstract class Fader {
  protected def initialValue: Float
  protected def minValue: Float
  protected def maxValue: Float
  protected def changeListener: ChangeListener[Number]
  protected def bounds: Rectangle
  protected def app: MyPApplet
  implicit val iApp: MyPApplet = app
  app.faders += this

  protected val floatProperty: SimpleFloatProperty =
    new SimpleFloatProperty(initialValue) {
      addListener(changeListener)
    }

  def apply(): Float = floatProperty.get()

  protected val slideTrackRect: Rectangle
  protected val handleRect: Rectangle

  private var isDragging = false

  def mousePressed(click: MouseEvent): Unit = {
    if (click.getButton == PConstants.LEFT) {
      val clickLoc = Vector(click.getX, click.getY)
      if (clickLoc isInside handleRect) {
        isDragging = true
      }
    }
  }

  protected def centerKnobOnSlider(mouseX: Int): Unit

  private def innerRectDragged(mouseX: Int): Unit =
    if (isDragging) {
      centerKnobOnSlider(mouseX)
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
    slideTrackRect.draw()
    Solarized.Yellow.fill()
    handleRect.draw()
    Solarized.White.stroke().fill()
    app.textSize(24)

    // MedPriorityTodo the text location should differ for HScrollbar vs `VScrollbar`
    app.text(
      /*string=*/ f"${ floatProperty.get.floatValue }%.2f",
      /*left=*/ slideTrackRect.right + 10,
      /*bottom??=*/ slideTrackRect.bottom - slideTrackRect.height / 3
    )
  }
}

/** @inheritdoc
 */
class HorizontalFader(
  override protected val initialValue: Float,
  override protected val minValue: Float,
  override protected val maxValue: Float,
  override protected val bounds: Rectangle,
  override protected val changeListener: ChangeListener[Number] = (_, _, _) => (),
)(implicit pApp: MyPApplet)
  extends Fader {
  override def app: MyPApplet = pApp

  override protected def centerKnobOnSlider(mouseX: Int): Unit =
    handleRect centerOnX mouseX withinWidth slideTrackRect

  override protected def mouseDimLoc: MouseEvent => Int = _.getX

  private val trackHeight = 10
  override protected val slideTrackRect: Rectangle = geometry.Rectangle(
    left = bounds.left,
    top = bounds.top + bounds.height / 2 - trackHeight / 2,
    width = bounds.width,
    height = trackHeight
  )

  override protected val handleRect: Rectangle = geometry.Rectangle(
    left = bounds.left,
    top = bounds.top,
    width = 10,
    height = bounds.height
  )

  override protected def updateCurValue(): Unit = {
    val extent: Float = handleRect.left - slideTrackRect.left
    val width: Float = slideTrackRect.width - handleRect.width
    val prop: Float = extent / width
    val range = maxValue - minValue
    floatProperty.setValue(minValue + range * prop)
  }
}

/** @inheritdoc
 */
class VerticalFader(
  override protected val initialValue: Float,
  override protected val minValue: Float,
  override protected val maxValue: Float,
  override protected val changeListener: ChangeListener[Number],
  override protected val bounds: Rectangle,
)(implicit pApp: MyPApplet)
  extends Fader {
  override def app: MyPApplet = pApp

  override protected def centerKnobOnSlider(mouseCoord: Int): Unit =
    handleRect centerOnY mouseCoord withinHeight slideTrackRect

  override protected def mouseDimLoc: MouseEvent => Int = _.getY

  private val trackWidth = 10
  override protected val slideTrackRect: Rectangle = geometry.Rectangle(
    left = bounds.left + bounds.width / 2 - trackWidth / 2,
    top = bounds.top,
    width = trackWidth,
    height = bounds.height
  )

  override protected val handleRect: Rectangle = geometry.Rectangle(
    left = bounds.left,
    top = bounds.top,
    width = bounds.width,
    height = 10
  )

  override protected def updateCurValue(): Unit = {
    val extent: Float = handleRect.top - slideTrackRect.top
    val height: Float = slideTrackRect.height - handleRect.height
    val prop: Float = extent / height
    floatProperty.setValue(prop * maxValue)
  }
}
