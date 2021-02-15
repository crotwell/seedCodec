plugins {
    id("edu.sc.seis.version-class") version "1.1.1"
    // Apply the java-library plugin to add support for Java Library
    "java-library"
    `maven-publish`
    signing
    eclipse
}

group = "edu.sc.seis"
version = "1.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
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
        url = uri("$buildDir/repos/test-deploy")
      }
      maven {
          val releaseRepo = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
          val snapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
          url = uri(if ( version.toString().toLowerCase().endsWith("snapshot")) snapshotRepo else releaseRepo)
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
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf("Implementation-Title" to project.name,
                      "Implementation-Version" to project.version)
            )
        }
    }
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
