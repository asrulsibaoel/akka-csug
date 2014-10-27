scalaVersion := "2.11.2"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

val Akka = "2.3.6"

val Spray = "1.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-actor"                    % Akka,
  "com.typesafe.akka"  %% "akka-cluster"                  % Akka,
  "com.typesafe.akka"  %% "akka-persistence-experimental" % Akka,
  "io.spray"           %% "spray-httpx"                   % Spray,
  "io.spray"           %% "spray-can"                     % Spray,
  "io.spray"           %% "spray-routing"                 % Spray,
  "org.typelevel"      %% "scodec-core"                   % "1.3.0",
  "com.typesafe.akka"  %% "akka-testkit"                  % Akka % "test",
  "io.spray"           %% "spray-testkit"                 % Spray % "test",
  "org.scalacheck"     %% "scalacheck"                    % "1.11.6" % "test"
)
