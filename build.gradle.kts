import mappings.internal.tasks.mappings.EnigmaMappingsTask
import mappings.internal.FileConstants

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
    enigmaRuntime("org.quiltmc:stitch:0.6.3")
}

val mappings: Task by tasks.creating(EnigmaMappingsTask::class) {
    dependsOn(tasks.getByName("downloadMinecraftServer"))
    jarToMap.set(FileConstants.INSTANCE.minecraftJar)
}
