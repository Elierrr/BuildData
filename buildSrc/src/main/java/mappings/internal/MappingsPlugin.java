package mappings.internal;

import mappings.internal.tasks.setup.DownloadMinecraftServerTask;
import mappings.internal.tasks.setup.DownloadVersionsManifestTask;
import mappings.internal.tasks.setup.DownloadWantedVersionManifestTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

public class MappingsPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        new FileConstants(target);
        Constants.ENIGMA_CONFIGURATION = target.getConfigurations().create("enigmaRuntime");
        TaskContainer tasks = target.getTasks();
        tasks.register(DownloadVersionsManifestTask.TASK_NAME, DownloadVersionsManifestTask.class);
        tasks.register(DownloadWantedVersionManifestTask.TASK_NAME, DownloadWantedVersionManifestTask.class);
        tasks.register(DownloadMinecraftServerTask.TASK_NAME, DownloadMinecraftServerTask.class);
    }
}
