package helpers
import scala.reflect.io.Directory

/** @param gifLength = zero means gif-saving is disabled. */
class GifSaver(className: String, gifLength: Int) {
  lazy val gifFramesDir: Directory = {
    val gifsDir =
      reflect.io
        .Directory(s"gifs/$className")
        .createDirectory()

    val newGifIndex: Int =
      gifsDir
        .list
        .map(_.name)
        .filter(_ contains "Frames")
        .flatMap("(\\d+)".r findFirstMatchIn _)
        .map(_ group 1)
        .map(_.toInt)
        .maxOption
        .getOrElse(0) + 1

    reflect.io
      .Directory(s"$gifsDir/Frames-$newGifIndex")
      .createDirectory()
  }

  /**
   * Subclasses can call this to create gifs.
   *
   * To enable it, update `FramesToSave` to the length of the desired gif.
   *
   * It dumps a bunch of pngs, and then at the right time, it shells out
   * to ImageMagick to merge the pngs into a gif. Then it stops doing anything.
   */
  final def addFrame(
    frameCount: Int,
    saveFrame: String => Unit
  ): Unit = {
    // NB: The first `frameCount` is 1 apparently.
    if (frameCount > gifLength) return

    // Thought: we could probably parallelize this by making it async
    val frameFile = reflect.io.File(gifFramesDir / f"Frame-$frameCount%03d.png")
    println(s"CREATING GIF: saving frame $frameCount of $gifLength as png")
    saveFrame(frameFile.toString)

    if (frameCount == gifLength) {
      convertPngsToGif()
    }
  }

  private def convertPngsToGif(): Unit = {
    println("CREATE GIF: converting pngs to gif")
    val hyphenatedClassName =
      className
        .tail
        .foldLeft(new StringBuilder() append className.head.toLower) {
          (b, char) =>
            char match {
              case _ if char.isUpper => b append "-" + char.toLower
              case _ => b append char
            }
        }.toString()
    val gifPath = s"$gifFramesDir/$hyphenatedClassName.gif"
    import sys.process._
    s"convert -delay 0 $gifFramesDir/*.png -loop 0 $gifPath".!!
    println("CREATING GIF: gif saved successfully")
  }
}

object GifSaver {
  val Disabled: Int = 0
}
