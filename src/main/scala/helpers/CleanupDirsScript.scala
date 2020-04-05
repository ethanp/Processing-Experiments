package helpers
import scala.reflect.io
import scala.reflect.io.Path

/** Created 4/5/20 1:42 AM
 */
object CleanupDirsScript {
  def main(args: Array[String]): Unit = {
    val here = io.Directory(".")
    assert(
      assertion = here.list.length > 1,
      message = "directory not found"
    )
    for (jpg <- here.files) {
      if (jpg.name.matches(".*-.*\\.jpg$")) {
        "(\\w+)-(\\d+)\\.jpg$".r findFirstMatchIn jpg.name match {
          case Some(found) =>
            val name = found.group(1)
            val num = found.group(2)
            val dir = io.Directory(name)
            dir.createDirectory()
            val newPath: Path = dir / s"$num.jpg"
            if (!jpg.jfile.renameTo(newPath.jfile)) {
              System.err.println(s"FATAL: Couldn't move $jpg to $newPath")
              System.exit(1)
            }

          case None =>
            System.err.println(s"WARNING: Couldn't parse file path: $jpg")
        }
      }
    }
  }
}
