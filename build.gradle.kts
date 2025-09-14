group = "io.github.bitfiddling"
version = "0.0.1"
description = "Avaje JsonB adapters for Google Guava collection types"

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotless)
}

repositories {
    mavenCentral()
}

dependencies {
    // Avaje JSONB
    implementation(libs.avaje.jsonb)
    
    // Google Guava
    implementation(libs.guava)

    // Annotation processors
    compileOnly(libs.avaje.jsonb.generator)
    annotationProcessor(libs.avaje.jsonb.generator)
    
    // Test dependencies
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.assertj.core)
    testImplementation(libs.avaje.jsonb)
    testImplementation(libs.guava)
    
    // Test annotation processing
    testCompileOnly(libs.avaje.jsonb.generator)
    testAnnotationProcessor(libs.avaje.jsonb.generator)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

tasks.test {
    useJUnitPlatform()
}

// Configure source and javadoc JARs
java {
    withSourcesJar()
    withJavadocJar()
}

// Publishing configuration
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Avaje Jsonb Guava Adapters")
                description.set("JSON serialization adapters for Google Guava collection types using Avaje jsonb")
                url.set("https://github.com/bitfiddling/avaje-jsonb-guava")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id = "vamega"
                        name = "Varun Madiath"
                        email = "opensource@bitfiddling.com.com"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/vamega/avaje-jsonb-guava.git"
                    developerConnection = "scm:git:ssh://github.com/vamega/avaje-jsonb-guava.git"
                    url = "https://github.com/vamega/avaje-jsonb-guava"
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = findProperty("sonatypeUsername") as String? ?: System.getenv("SONATYPE_USERNAME")
                password = findProperty("sonatypePassword") as String? ?: System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

// Signing configuration
signing {
    if (project.hasProperty("signing.keyId")) {
        sign(publishing.publications["maven"])
    }
}

// Spotless code formatting
configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        palantirJavaFormat()
        removeUnusedImports()
        target("src/**/*.java")
    }
}
