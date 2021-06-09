name := "tapirBug"

version := "0.1"

scalaVersion := "2.13.6"
val AkkaVersion = "2.6.14"
val AkkaHttpVersion = "10.2.4"
val TapirVersion = "0.18.0-M15"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % TapirVersion,
)