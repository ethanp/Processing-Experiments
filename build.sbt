name := "FirstTry"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test,
    "org.mongodb.scala" %% "mongo-scala-driver" % "4.0.1",
    "org.json4s" %% "json4s-native" % "3.7.0-M2",
    "org.jsoup" % "jsoup" % "1.13.1",
    "org.processing" % "core" % "3.3.6",
    "org.jogamp.jogl" % "jogl-all-main" % "2.3.2",
)

// Use Java 8
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

// Enforce usage of Java 8
initialize := {
    val _ = initialize.value
    if (sys.props("java.specification.version") != "1.8")
        sys.error("Java 8 is required for this project.")
}
