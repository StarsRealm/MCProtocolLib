plugins {
    idea
    `java-library`
    `maven-publish`
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.lombok)
}

val repoName = if (version.toString().endsWith("SNAPSHOT")) "maven-snapshots" else "maven-releases"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/StarsRealm/Packages")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create("gpr", MavenPublication::class.java) {
            from(components.getByName("java"))
        }
    }
}

dependencies {
    // Minecraft related libraries
    api(libs.opennbt)
    api(libs.mcauthlib)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.bundles.netty)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
}

lombok {
    version = libs.versions.lombok.version.get()
}

group = "com.github.steveice10"
version = "1.20.4-3-SNAPSHOT"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all,-processing")
}

tasks.withType<Javadoc> {
    title = "MCProtocolLib Javadocs"
    val options = options as StandardJavadocDocletOptions
    options.encoding = "UTF-8"
    options.addStringOption("Xdoclint:all,-missing", "-quiet")
}

