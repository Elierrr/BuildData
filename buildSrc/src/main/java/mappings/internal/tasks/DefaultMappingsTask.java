package mappings.internal.tasks;

import mappings.internal.FileConstants;
import mappings.internal.util.DownloadImmediate;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;

public abstract class DefaultMappingsTask extends DefaultTask {

    protected final FileConstants fileConstants;

    public DefaultMappingsTask(String group) {
        this.fileConstants = FileConstants.INSTANCE;
        this.setGroup(group);
    }

    protected DownloadImmediate.Builder startDownload() {
        return new DownloadImmediate.Builder(this);
    }

    protected <T extends Task> T getTaskByName(String taskName) {
        return (T) getProject().getTasks().getByName(taskName);
    }

    protected <T extends Task> T getTaskByType(Class<T> taskClass) {
        return getProject().getTasks().stream().filter(task -> taskClass.isAssignableFrom(task.getClass())).map(taskClass::cast).findAny().orElseThrow();
    }

    protected void outputsNeverUpToDate() {
        getOutputs().upToDateWhen(task -> false);
    }
}
