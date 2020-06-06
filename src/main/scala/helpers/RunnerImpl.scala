package helpers
import processing.core.PApplet

/** Created 3/25/20 12:07 AM
 */
trait RunnerImpl {
  def pAppletClass: Class[_]
  def main(args: Array[String]): Unit = PApplet main pAppletClass
}

abstract class Runner[T](implicit m: Manifest[T]) extends RunnerImpl {
  override def pAppletClass: Class[_] = m.runtimeClass
}
