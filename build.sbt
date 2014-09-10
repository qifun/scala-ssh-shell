name := "scala-ssh-shell"

organization := "scala-ssh-shell"

version := "0.0.1-SNAPSHOT"

compileOrder := CompileOrder.JavaThenScala

libraryDependencies += "commons-codec" % "commons-codec" % "1.9"

libraryDependencies <+= scalaVersion { sv =>
  "com.dongxiguo" %% "zero-log" % "0.3.6"
}

crossScalaVersions := Seq("2.11.2")

scalacOptions ++= Vector("-unchecked", "-deprecation")

javacOptions ++= Vector("-encoding", "UTF-8")

libraryDependencies += "jline" % "jline" % "2.12"

libraryDependencies <++= (scalaVersion) {
	(scala) => Seq(
	"org.scala-lang" % "scala-compiler" % scala,
	"org.bouncycastle" % "bcprov-jdk16" % "1.46",
	"org.apache.sshd" % "sshd-core" % "0.8.0",
	"org.slf4j" % "slf4j-api" % "1.6.4"
	)}

pomExtra <<= scalaVersion { sv =>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <scala.version>{sv}</scala.version>
  </properties>
  <build>
    <plugins>
      <plugin>
        <groupId>org.scala-tools</groupId>
        <artifactId>maven-scala-plugin</artifactId>
        <version>2.15.2</version>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>testCompile</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <recompileMode>modified-only</recompileMode>
          <args>
            <arg>-Xelide-below</arg>
            <arg>FINEST</arg>
            <arg>-deprecation</arg>
          </args>
        </configuration>
      </plugin>
    </plugins>
  </build>
}
