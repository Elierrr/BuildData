import mappings.internal.tasks.mappings.EnigmaMappingsTask
import mappings.internal.FileConstants

val cacheFiles = file(".gradle/minecraft")
val minecraftVersion: String by project
val minecraftJar = File(cacheFiles, "${minecraftVersion}-server.jar")

plugins {
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
}

val mappings: Task by tasks.creating(EnigmaMappingsTask::class) {
    dependsOn(tasks.getByName("downloadMinecraftServer"))
    jarToMap.set(FileConstants.INSTANCE.minecraftJar)
}
