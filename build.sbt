import spray.revolver.RevolverPlugin._
import AssemblyKeys._

name := "akka-training"

version := "0.0.1"

scalaVersion := "2.11.3"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases"
)

libraryDependencies ++= Seq(
  "com.typesafe"            % "config"                    % "1.2.1",
  "com.typesafe.akka"      %% "akka-actor"                % "2.3.6",
  "com.typesafe.akka"      %% "akka-cluster"              % "2.3.6",
  "com.typesafe.akka"      %% "akka-slf4j"                % "2.3.6",
  "ch.qos.logback"          % "logback-classic"           % "1.1.2",
  "com.typesafe.akka"      %% "akka-testkit"              % "2.3.6"   % "test",
  "org.scalatest" 	    % "scalatest_2.11"            % "2.2.1"   % "test",
  "junit"                   % "junit"                     % "4.11"    % "test"
)


seq(Revolver.settings: _*)

assemblySettings

scalacOptions ++= Seq(
  "-deprecation",           
  "-encoding", "UTF-8",
  "-feature",                
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",       
  "-Xlint",
  "-Yno-adapted-args",       
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",   
  "-Ywarn-value-discard",
  "-Xfuture"     
)

