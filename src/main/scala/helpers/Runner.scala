package helpers
import processing.core.PApplet

/** Created 3/25/20 12:07 AM
 */
trait Runner {
  def pAppletClass: Class[_]
  def main(args: Array[String]): Unit = PApplet main pAppletClass
}
