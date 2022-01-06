package mappings.internal.tasks.build;

import java.io.File;
import java.io.IOException;

import cuchaz.enigma.command.MapSpecializedMethodsCommand;
import cuchaz.enigma.translation.mapping.serde.MappingParseException;
import mappings.internal.tasks.setup.DownloadMinecraftServerTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import mappings.internal.Constants;
import mappings.internal.tasks.DefaultMappingsTask;

public class BuildMappingsTinyTask extends DefaultMappingsTask {
    public static final String TASK_NAME = "buildMappingsTiny";
    private final File mappings;

    @OutputFile
    public File outputMappings = new File(fileConstants.tempDir, String.format("%s.tiny", Constants.MAPPINGS_NAME));

    public BuildMappingsTinyTask() {
        super(Constants.Groups.BUILD_MAPPINGS_GROUP);
        dependsOn(DownloadMinecraftServerTask.TASK_NAME);
        mappings = getProject().file("mappings");
        getInputs().dir(mappings);
        outputsNeverUpToDate();
    }

    @TaskAction
    public void buildMappingsTiny() throws IOException, MappingParseException {
        getLogger().lifecycle(":generating tiny mappings");

        new MapSpecializedMethodsCommand().run(
                fileConstants.minecraftJar.getAbsolutePath(),
                "enigma",
                mappings.getAbsolutePath(),
                "tinyv2:named",
                outputMappings.getAbsolutePath()
        );
    }

    public File getOutputMappings() {
        return outputMappings;
    }
}
