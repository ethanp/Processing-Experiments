package helpers
import scala.reflect.io
import scala.reflect.io.Path

/**
 * This is definitely not needed any-more but I'm leaving it around for now
 * because I just wrote it and I am attached to it.
 *
 * Created 4/5/20 1:42 AM
 */
@deprecated
object CleanupDirsScript {
  def main(args: Array[String]): Unit = {
    val here = io.Directory(".")
    assert(
      assertion = here.list.length > 1,
      message = "directory not found"
    )
    for {
      jpg <- here.files
        if jpg.name matches ".*-.*\\.jpg$"
    } {
      // This is cool, source:
      // https://netzwerg.ch/blog/2015/02/27/scala-gems-number-9-regex-extractors
      val ParseFilename = "(\\w+)-(\\d+)\\.jpg$".r
      jpg.name match {
        case ParseFilename(name: String, num: String) =>
          val newPath: Path = io.Directory(name).createDirectory() / s"$num.jpg"
          val succeeded: Boolean = jpg.jfile renameTo newPath.jfile
          assert(
            assertion = succeeded,
            message = "FATAL: Couldn't move $jpg to $newPath"
          )
      }
    }
  }
}
