val wordleProject = Project("Wordle", file("Wordle"))
  .settings(
    version := "0.1",
    scalaVersion := "2.13.10",
    libraryDependencies := Seq(
    "org.scalactic" %% "scalactic" % "3.2.15", //Husk: 3.2.15 istedet for 3.0.1
    "org.scalatest" %% "scalatest" % "3.2.15" % "test",
        "org.scalamock" %% "scalamock" % "5.2.0" % "test"))

val pdfDownloaderProject = Project("PDFDownloader", file("PDFDownloader"))
  .settings(
    version := "0.1",
    scalaVersion := "2.13.10",
    libraryDependencies := Seq(
        "org.apache.poi" % "poi-ooxml" % "4.1.2",
        "commons-io" % "commons-io" % "2.5",
        "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
        "org.scalactic" %% "scalactic" % "3.2.15",
        "org.scalatest" %% "scalatest" % "3.2.15" % "test",
        "org.scalamock" %% "scalamock" % "5.2.0" % "test"))

val academyProject = Project("Academy", file(".")).aggregate(wordleProject, pdfDownloaderProject)


