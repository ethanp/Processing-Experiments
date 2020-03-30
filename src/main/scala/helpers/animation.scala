import helpers.MyPApplet

/** Created 3/29/20 11:17 PM
 */
package object animation {

  import concurrent.duration._

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
