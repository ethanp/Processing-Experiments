import helpers.MyPApplet

import scala.concurrent.duration._

/** Created 3/29/20 11:17 PM
 */
package object animation {
  case class Every(duration: FiniteDuration)(implicit myPApplet: MyPApplet) {
    private var lastTime = 0L
    def run(block: => Unit): Unit = {
      if (myPApplet.millis() / duration.toMillis > lastTime) {
        block
        lastTime = myPApplet.millis() / duration.toMillis
      }
    }
  }
}
