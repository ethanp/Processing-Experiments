package helpers
import scala.reflect.io.Directory

/** @param gifLength = zero means gif-saving is disabled. */
class GifSaver(className: String, gifLength: Int) {
  lazy val gifFramesDir: Directory = {
    val gifsDir =
      reflect.io
        .Directory(s"gifs/$className")
        .createDirectory()

    val newGifIndex: Int = gifsDir
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
   * It dumps a bunch of pngs, then shells out to ImageMagick to merge them into a gif.
   */
  final def overwritePngsForGifIfEnabled(
    frameCount: Int,
    saveFrame: String => Unit
  ): Unit = {
    // NB: The first `frameCount` is 1 apparently.
    if (frameCount > gifLength) return

    // Thought: we could probably parallelize this by making it async
    val frameFile = reflect.io.File(gifFramesDir / s"Frame-$frameCount.png")
    println(s"CREATING GIF: saving frame $frameCount as png")
    saveFrame(frameFile.toString)

    if (frameCount == gifLength - 1) {
      convertPngsToGif()
    }
  }
  private def convertPngsToGif(): Unit = {
    println("CREATE GIF: converting pngs to gif")
    import sys.process._
    val hyphenatedClassName =
      className
        .tail
        .foldLeft(new StringBuilder(className.head.toLower)) {
          (b, char) =>
            char match {
              case _ if char.isUpper => b append "-" + char.toLower
              case _ => b append char
            }
        }.toString()
    val gifPath = s"$gifFramesDir/$hyphenatedClassName.gif"
    s"convert -delay 20 $gifFramesDir/*.png -loop 0 $gifPath".!!
    println("CREATING GIF: gif saved successfully")
  }
}
