name := "scala-ssh-shell"

libraryDependencies <++= (scalaVersion) {
	(scala) => Seq(
	"org.scala-lang" % "scala-compiler" % scala,
	"org.scala-lang" % "jline" % scala,
	"com.weiglewilczek.slf4s" %% "slf4s" % "1.0.7",
	"org.slf4j" % "slf4j-api" % "1.6.4",
	"org.apache.sshd" % "sshd-core" % "0.6.0"
	)}
