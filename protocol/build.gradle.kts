plugins {
    id("mcprotocollib.publish-conventions")
    jacoco
    `maven-publish`
}

version = "1.21-002-SNAPSHOT"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

dependencies {
    // Minecraft related libraries
    api(libs.cloudburstnbt)
    api(libs.mcauthlib)

    // Slf4j
    api(libs.slf4j.api)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.bundles.netty)

    // Checker Framework
    api(libs.checkerframework.qual)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

publishing {
    repositories {
        maven {
            name = "AliYun-Release"
            url = uri("https://packages.aliyun.com/maven/repository/2421751-release-ZmwRAc/")
            credentials {
                username = project.findProperty("aliyun.package.user") as String? ?: System.getenv("ALY_USER")
                password = project.findProperty("aliyun.package.password") as String? ?: System.getenv("ALY_PASSWORD")
            }
        }
        maven {
            name = "AliYun-Snapshot"
            url = uri("https://packages.aliyun.com/maven/repository/2421751-snapshot-i7Aufp/")
            credentials {
                username = project.findProperty("aliyun.package.user") as String? ?: System.getenv("ALY_USER")
                password = project.findProperty("aliyun.package.password") as String? ?: System.getenv("ALY_PASSWORD")
            }
        }
    }
    publications {
        create("gpr", MavenPublication::class.java) {
            from(components.getByName("java"))
        }
    }
}
