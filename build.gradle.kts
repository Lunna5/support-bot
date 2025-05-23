import org.gradle.internal.declarativedsl.parsing.main

plugins {
    id("java")
    id("application")
    id("com.diffplug.spotless") version "7.0.2"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = "dev.lunna"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("info.picocli:picocli:4.7.7")
    implementation("net.dv8tion:JDA:5.3.2") { exclude(module = "opus-java") } // Discord Integration
    implementation("com.google.inject:guice:7.0.0") // Dependency Injection
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    implementation("org.incendo:cloud-jda5:1.0.0-beta.3")
    implementation("org.incendo:cloud-annotations:2.0.0")

    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.fusesource.jansi:jansi:2.4.0")

    implementation("net.sourceforge.tess4j:tess4j:5.14.0")

    implementation("org.spongepowered:configurate-yaml:4.2.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.lunna.support.bot.bootstrap.Bootstrap")
}

allprojects {
    spotless {
        format("misc") {
            target(
                "*.gradle.kts",
                ".gitattributes",
                ".gitignore"
            )

            trimTrailingWhitespace()
            endWithNewline()
        }

        java {
            googleJavaFormat("1.26.0").aosp().reflowLongStrings().skipJavadocFormatting()
            formatAnnotations()
            removeUnusedImports()
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}