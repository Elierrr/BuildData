package mappings.internal;

import mappings.internal.tasks.build.*;
import mappings.internal.tasks.setup.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MappingsPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        new FileConstants(target);
        Constants.ENIGMA_CONFIGURATION = target.getConfigurations().create("enigmaRuntime");
        TaskContainer tasks = target.getTasks();
        tasks.create(BuildMappingsTinyTask.TASK_NAME, BuildMappingsTinyTask.class);
        tasks.create(CompressTinyTask.TASK_NAME, CompressTinyTask.class);
        tasks.create(DownloadVersionsManifestTask.TASK_NAME, DownloadVersionsManifestTask.class);
        tasks.create(DownloadWantedVersionManifestTask.TASK_NAME, DownloadWantedVersionManifestTask.class);
        tasks.create(DownloadMinecraftServerTask.TASK_NAME, DownloadMinecraftServerTask.class);
        tasks.create(DropInvalidMappingsTask.TASK_NAME, DropInvalidMappingsTask.class);
        tasks.create(TinyJarTask.TASK_NAME, TinyJarTask.class);
    }
}
