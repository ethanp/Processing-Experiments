package doit
import processing.core.PApplet

/** Created 3/22/20 4:52 PM
 */
class App extends PApplet {
  override def draw(): Unit = ellipse(50, 50, 50, 50)
}

object App {
  def main(args: Array[String]): Unit = {
    PApplet.main("doit.App")
  }
}
