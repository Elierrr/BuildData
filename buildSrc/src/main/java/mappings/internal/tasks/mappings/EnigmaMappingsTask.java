package mappings.internal.tasks.mappings;

import mappings.internal.Constants;
import org.gradle.api.Task;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.JavaExec;

import java.util.List;

public class EnigmaMappingsTask extends JavaExec implements Task {
    public EnigmaMappingsTask() {
        this.setGroup(Constants.Groups.MAPPINGS_GROUP);
        this.getMainClass().set("cuchaz.enigma.gui.Main");
        this.classpath(getProject().getConfigurations().getByName("enigmaRuntime"));
        jarToMap = getObjectFactory().fileProperty();
        jvmArgs("-Xmx2048m");
    }

    @Override
    public void exec() {
        args(List.of(
                "-jar", this.jarToMap.get().getAsFile().getAbsolutePath(), "-mappings", getProject().file("mappings").getAbsolutePath()/*, "-profile", "enigma_profile.json"*/
        ));
        super.exec();
    }

    @InputFile
    private final RegularFileProperty jarToMap;

    public RegularFileProperty getJarToMap() {
        return jarToMap;
    }
}
