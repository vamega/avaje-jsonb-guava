group = "com.bitfiddling"
val artifactId = "avaje-jsonb-guava"
version = "0.0.1"
description = "Avaje Jsonb adapters for Google Guava collection types"

plugins {
    `java-library`
    alias(libs.plugins.spotless)
    alias(libs.plugins.mavenPublish)
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
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

tasks.test {
    useJUnitPlatform()
}

// Maven publish plugin configuration
mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(groupId = group.toString(), artifactId = artifactId, version = version.toString())

    pom {
        name.set("Avaje Jsonb Guava Adapters")
        description.set("JSON serialization adapters for Google Guava collection types using Avaje JsonB")
        url.set("https://github.com/vamega/avaje-jsonb-guava")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("vamega")
                name.set("Varun Madiath")
                email.set("opensource@bitfiddling.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/vamega/avaje-jsonb-guava.git")
            developerConnection.set("scm:git:ssh://github.com/vamega/avaje-jsonb-guava.git")
            url.set("https://github.com/vamega/avaje-jsonb-guava")
        }
    }
}

// Spotless code formatting
configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        palantirJavaFormat(libs.versions.palantirJavaFormat.get())
        removeUnusedImports()
        target("src/**/*.java")
    }
}
