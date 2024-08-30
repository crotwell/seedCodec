plugins {
    id("edu.sc.seis.version-class") version "1.3.0"
    // Apply the java-library plugin to add support for Java Library
    `java-library`
    `java-library-distribution`
    `maven-publish`
    signing
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "edu.sc.seis"
version = "1.2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}


tasks.register("versionToVersionFile") {
  inputs.files("build.gradle.kts")
  outputs.files("VERSION")
  File("VERSION").writeText(""+version)
}

distributions {
    main {
        distributionBaseName = "seedCodec"
	contents {
	    from(tasks.named("javadoc")) {
	        into("javadoc")
	    }
            from(tasks.named("versionToVersionFile")) {
              into(".")
            }
            from(".") {
                include("LICENSE")
                include("README.md")
                include("build.gradle.kts")
                include("settings.gradle.kts")
                include("src/**")
                include("gradle/**")
                include("gradlew")
                include("gradlew.bat")
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
              name.set("seedCodec")
              description.set("A collection of compression and decompression routines for standard seismic data formats in Java.")
              url.set("http://www.seis.sc.edu/seedCodec.html")

              scm {
                connection.set("scm:git:https://github.com/crotwell/seedCodec.git")
                developerConnection.set("scm:git:https://github.com/crotwell/seedCodec.git")
                url.set("https://github.com/crotwell/seedCodec")
              }

              licenses {
                license {
                  name.set("GNU Lesser General Public License, Version 3")
                  url.set("https://www.gnu.org/licenses/lgpl-3.0.txt")
                }
              }

              developers {
                developer {
                  id.set("crotwell")
                  name.set("Philip Crotwell")
                  email.set("crotwell@seis.sc.edu")
                }
              }
            }
        }
    }
    repositories {
      maven {
        name = "TestDeploy"
          url = uri(layout.buildDirectory.dir("repos/test-deploy"))
      }
      maven {
          val releaseRepo = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
          val snapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
          url = uri(if ( version.toString().lowercase().endsWith("snapshot")) snapshotRepo else releaseRepo)
          name = "ossrh"
          // credentials in gradle.properties as ossrhUsername and ossrhPassword
          credentials(PasswordCredentials::class)
      }
    }

}

signing {
    sign(publishing.publications["mavenJava"])
}

repositories {
    mavenCentral()
}

dependencies {

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf("Automatic-Module-Name" to "edu.sc.seis.seedCodec",
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version)
            )
        }
    }
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}


tasks.named("sourcesJar") {
    dependsOn("makeVersionClass")
}
tasks.get("assemble").dependsOn("versionToVersionFile")

