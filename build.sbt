name := "scala-ssh-shell"

libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-compiler" % _ }

libraryDependencies <+= scalaVersion { "org.scala-lang" % "jline" % _ }

libraryDependencies <+= scalaVersion { sv =>
  "com.dongxiguo" % ("zero-log_" + sv) % "0.1.2-SNAPSHOT"
}

libraryDependencies += "org.bouncycastle" % "bcprov-jdk16" % "1.46"

libraryDependencies += "org.apache.sshd" % "sshd-core" % "0.6.0"

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
