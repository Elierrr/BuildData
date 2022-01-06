package mappings.internal.tasks.setup;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import mappings.internal.Constants;
import mappings.internal.tasks.DefaultMappingsTask;
import org.gradle.api.tasks.TaskAction;
import org.quiltmc.launchermeta.version.v1.Version;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DownloadMinecraftServerTask extends DefaultMappingsTask {
    public static final String TASK_NAME = "downloadMinecraftServer";

    private Version file;

    public DownloadMinecraftServerTask() {
        super(Constants.Groups.SETUP_GROUP);
        this.dependsOn(DownloadWantedVersionManifestTask.TASK_NAME);

        File serverJar = fileConstants.minecraftJar;
        getOutputs().file(serverJar);

        getOutputs().upToDateWhen(_input -> {
            try {
                return serverJar.exists()
                        && validateChecksum(serverJar, getVersionFile().getDownloads().getServer().get().getSha1());
            } catch (Exception e) {
                return false;
            }
        });
    }

    @TaskAction
    public void downloadMinecraftJars() throws IOException {
        getLogger().lifecycle(":downloading minecraft jars");

        startDownload()
                .src(getVersionFile().getDownloads().getServer().get().getUrl())
                .dest(fileConstants.minecraftJar)
                .overwrite(false)
                .download();
    }

    @SuppressWarnings("deprecation")
    private static boolean validateChecksum(File file, String checksum) throws IOException {
        if (file != null) {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha1());
            StringBuilder builder = new StringBuilder();
            for (byte b : hash.asBytes()) {
                builder.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
            }
            return builder.toString().equals(checksum);
        }
        return false;
    }

    private Version getVersionFile() throws IOException {
        if (file == null) {
            file = Version.fromReader(java.nio.file.Files.newBufferedReader(getTaskByType(DownloadWantedVersionManifestTask.class).getVersionFile().toPath(), StandardCharsets.UTF_8));
        }

        return file;
    }
}