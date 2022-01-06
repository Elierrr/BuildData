package mappings.internal.tasks.build;

import mappings.internal.Constants;
import mappings.internal.tasks.MappingsTask;
import org.gradle.jvm.tasks.Jar;

public class TinyJarTask extends Jar implements MappingsTask {
    public static final String TASK_NAME = "tinyJar";

    public TinyJarTask() {
        setGroup(Constants.Groups.BUILD_MAPPINGS_GROUP);
        dependsOn(BuildMappingsTinyTask.TASK_NAME);
        outputsNeverUpToDate();
        getArchiveFileName().set(String.format("%s-%s.jar", Constants.MAPPINGS_NAME, Constants.MAPPINGS_VERSION));
        getDestinationDirectory().set(getProject().file("build/libs"));
        getArchiveClassifier().convention("");
        from(getTaskByType(BuildMappingsTinyTask.class).getOutputMappings()).rename(original -> "mappings/mappings.tiny");
    }
}
