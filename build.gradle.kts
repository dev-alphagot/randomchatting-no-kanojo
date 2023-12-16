import com.github.jengelman.gradle.plugins.shadow.relocation.Relocator
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version = "2.3.5"
val logback_version = "1.4.11"

plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://libraries.minecraft.net")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots1"
    }
    maven(url = "https://repo.codemc.io/repository/maven-snapshots/"){
        name = "codemc-snapshots"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    /*implementation("com.github.twitch4j:twitch4j:1.15.0")
    implementation(group = "com.github.philippheuer.events4j", name = "events4j-handler-reactor", version = "0.9.8")*/

    // compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // compileOnly("me.clip:placeholderapi:2.11.5")

    // implementation("com.github.stefvanschie.inventoryframework:IF:0.10.11")
    // implementation("net.wesjd:anvilgui:1.9.0-SNAPSHOT")

    compileOnly("io.github.monun:kommand-api:3.1.7")

    // compileOnly("org.litote.kmongo:kmongo:4.10.0")
    // compileOnly("org.litote.kmongo:kmongo-serialization-mapping:4.10.0")

    // compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")

    /*
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    */
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    processResources {
        val file = File("version")
        val build = file.readText().toInt() + 1

        file.writeText(build.toString())

        val mmap = project.properties.toMutableMap()

        mmap["version"] = "$build"

        filesMatching("**/*.yml") {
            expand(mmap)
        }
        filesMatching("**/*.bin") {
        }
        filteringCharset = "UTF-8"
    }

    jar {
        exclude("offline-plugin.yml")

        //include("com.github.stefvanschie.inventoryframework:IF:0.10.11")
        //include()
    }

    // offline jar should be ready to go with all dependencies
    shadowJar {
        // relocate("com.github.stefvanschie.inventoryframework", "io.github.devalphagot.ppc.inventoryframework")

        // minimize()
        archiveClassifier.set("offline")
        //exclude("plugin.yml")
        //rename("offline-plugin.yml", "plugin.yml")

        dependencies {
            // exclude(dependency("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.9.20"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20"))
        }
    }

    // avoid classpath conflicts/pollution via relocation
    val configureShadowRelocation by registering(ConfigureShadowRelocation::class) {
        target = shadowJar.get()
        prefix = "${project.group}.${project.name.lowercase()}.libraries"
    }

    build {
        dependsOn(shadowJar)//.dependsOn(configureShadowRelocation)
    }
}
