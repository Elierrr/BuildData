import org.gradle.api.Project.GRADLE_PROPERTIES
import java.util.Properties

plugins {
    java
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release/")
}

val props = Properties()
props.load(rootDir.toPath().resolveSibling(GRADLE_PROPERTIES).toFile().inputStream())

dependencies {
    val enigmaVersion = props["enigmaVersion"]
    implementation("cuchaz:enigma-cli:${enigmaVersion}")
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.quiltmc:launchermeta-parser:1.0.0")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("de.undercouch:gradle-download-task:4.1.1")
}

gradlePlugin {
    plugins {
        create("mappings-logic") {
            id = "mappings-logic"
            implementationClass = "mappings.internal.MappingsPlugin"
        }
    }
}
