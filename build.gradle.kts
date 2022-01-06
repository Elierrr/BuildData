import mappings.internal.Constants
import mappings.internal.FileConstants
import mappings.internal.tasks.build.CompressTinyTask
import mappings.internal.tasks.mappings.EnigmaMappingsTask

plugins {
    `maven-publish`
    `mappings-logic`
}

repositories {
    maven("https://maven.quiltmc.org/repository/release/")
    mavenCentral()
}

dependencies {
    val enigmaVersion: String by project
    enigmaRuntime("cuchaz:enigma-swing:${enigmaVersion}") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling::class, Bundling.EXTERNAL))
        }
    }
    enigmaRuntime("org.quiltmc:stitch:0.6.3")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.elierrr"
            artifactId = Constants.MAPPINGS_NAME
            version = Constants.MAPPINGS_VERSION

            val compressTiny: CompressTinyTask = tasks.compressTiny.get()

            artifact(compressTiny.compressedTiny) {
                classifier = "tiny"
                builtBy(compressTiny)
            }
            artifact(tasks.tinyJar.get())
        }
    }
    repositories {
        mavenLocal()
    }
}

val mappings: Task by tasks.creating(EnigmaMappingsTask::class) {
    dependsOn(tasks.getByName("downloadMinecraftServer"))
    jarToMap.set(FileConstants.INSTANCE.minecraftJar)
}
