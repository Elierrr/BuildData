import com.google.common.hash.HashCode
import com.google.common.hash.Hashing
import de.undercouch.gradle.tasks.download.DownloadAction
import org.apache.commons.io.FileUtils
import org.quiltmc.launchermeta.version.v1.Version
import org.quiltmc.launchermeta.version_manifest.VersionEntry
import org.quiltmc.launchermeta.version_manifest.VersionManifest
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Optional
import kotlin.experimental.and

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.quiltmc.org/repository/release/")
    }
    dependencies {
        classpath("commons-io:commons-io:2.8.0")
        classpath("org.quiltmc:launchermeta-parser:1.0.0")
        classpath("com.google.guava:guava:31.0.1-jre")
        classpath("de.undercouch:gradle-download-task:4.1.1")
    }
}

val cacheFiles = file(".gradle/minecraft")
val enigmaRuntime: Configuration by configurations.creating
val enigmaVersion: String by project
val minecraftVersion: String by project
val minecraftJar = File(cacheFiles, "${minecraftVersion}-server.jar")
val versionFile = File(cacheFiles, "${minecraftVersion}.json")

repositories {
    maven("https://maven.quiltmc.org/repository/release/")
    mavenCentral()
}

dependencies {
    enigmaRuntime("cuchaz:enigma-swing:${enigmaVersion}") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling::class, Bundling.EXTERNAL))
        }
    }
}

val downloadVersionsManifest: Task by tasks.creating {
    val manifestFile = File(cacheFiles, "version_manifest_v2.jsom")
    outputs.file(manifestFile)
    doLast {
        logger.lifecycle(":downloading minecraft versions manifest")
        FileUtils.copyURLToFile(URL("https://launchermeta.mojang.com/mc/game/version_manifest_v2.json"), manifestFile)
    }
}

fun getManifestVersion(manifestFile: File, minecraftVersion: String): Optional<VersionEntry> {
    val manifest: VersionManifest? = if (manifestFile.exists()) VersionManifest.fromReader(Files.newBufferedReader(
        manifestFile.toPath(),
        Charset.defaultCharset())) else null
    return if (manifest != null) manifest.versions.stream().filter {
        it.id.equals(minecraftVersion)
    }.findFirst() else Optional.empty()
}

val downloadWantedVersionManifest: Task by tasks.creating {
    dependsOn(downloadVersionsManifest)
    val manifestFile = downloadVersionsManifest.outputs.files.singleFile
    val manifestVersion = getManifestVersion(manifestFile, minecraftVersion)

    outputs.file(versionFile)

    doLast {
        @Suppress("LocalVariableName")
        val _manifestVersion = if(manifestVersion.isEmpty) getManifestVersion(manifestFile, minecraftVersion) else manifestVersion
        if(_manifestVersion.isPresent || versionFile.exists()) {
            if(_manifestVersion.isPresent) {
                FileUtils.copyURLToFile(URL(_manifestVersion.get().url), versionFile)
            }
        } else {
            throw RuntimeException("No version data for Minecraft version $minecraftVersion")
        }
    }
}

fun validateChecksum(file: File?, checksum: String): Boolean {
    if (file != null) {
        val hash: HashCode = com.google.common.io.Files.asByteSource(file).hash(Hashing.sha1())
        val builder = StringBuilder()
        for (b in hash.asBytes()) {
            builder.append(((b and 0xFF.toByte()) + 0x100).toString(16).substring(1))
        }
        return builder.toString() == checksum
    }
    return false
}

val download: Task by tasks.creating {
    dependsOn(downloadWantedVersionManifest)
    outputs.file(minecraftJar)

    outputs.upToDateWhen {
        val version = Version.fromReader(Files.newBufferedReader(versionFile.toPath(), StandardCharsets.UTF_8))
        return@upToDateWhen minecraftJar.exists() && validateChecksum(minecraftJar, version.downloads.server.get().sha1)
    }

    doLast {
        if (!versionFile.exists()) {
            throw RuntimeException("Can't download the jars without the ${versionFile.name} file!")
        }

        val version = Version.fromReader(Files.newBufferedReader(versionFile.toPath(), StandardCharsets.UTF_8))

        logger.lifecycle(":downloading minecraft jar")

        val download = DownloadAction(project)
        download.src(version.downloads.server.get().url)
        download.dest(minecraftJar)
        download.overwrite(false)
        download.execute()
    }
}

tasks.create("enigma", JavaExec::class) {
    dependsOn(download)
    classpath = (enigmaRuntime)
    mainClass.set("cuchaz.enigma.gui.Main")

    args("-jar")
    args(minecraftJar.absolutePath)
    //args("-mappings")
    //args("") // TODO

    jvmArgs("-Xmx2048m")
}