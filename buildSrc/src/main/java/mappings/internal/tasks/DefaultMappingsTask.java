package mappings.internal.tasks;

import mappings.internal.FileConstants;
import org.gradle.api.DefaultTask;

public abstract class DefaultMappingsTask extends DefaultTask implements MappingsTask {

    protected final FileConstants fileConstants;

    public DefaultMappingsTask(String group) {
        this.fileConstants = FileConstants.INSTANCE;
        this.setGroup(group);
    }
}
