name := "scala-ssh-shell"

organization := "scala-ssh-shell"

version := "0.0.1-SNAPSHOT"

libraryDependencies <+= scalaVersion { sv =>
  "com.dongxiguo" % ("zero-log_" + sv) % "0.1.1"
}

crossScalaVersions :=
  Seq("2.9.1", "2.9.1-1",
      "2.10.0-M1", "2.10.0-M2")

scalacOptions ++= Vector("-unchecked", "-deprecation", "-Ywarn-all")

javacOptions ++= Vector("-encoding", "UTF-8")

retrieveManaged := true

libraryDependencies <++= (scalaVersion) {
	(scala) => Seq(
	"org.scala-lang" % "scala-compiler" % scala,
	"org.scala-lang" % "jline" % scala,
	"org.bouncycastle" % "bcprov-jdk16" % "1.46",
	"org.apache.sshd" % "sshd-core" % "0.6.0"
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
