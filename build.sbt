name := "simple-search"

version := "0.1"

scalaVersion := "2.13.6"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-explaintypes",
  "-language:higherKinds",
  "-Xlint:_",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:postfixOps",
  "-encoding", "UTF-8"
)

javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-encoding", "UTF-8"
)

fork in Test := false
parallelExecution in Test := true
fork in IntegrationTest := true
parallelExecution in IntegrationTest := true

libraryDependencies ++= Seq(
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)
