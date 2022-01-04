package mappings.internal;

import org.gradle.api.artifacts.Configuration;

public final class Constants {
    public static final String MINECRAFT_VERSION = "1.8.8";

    public static final String MAPPINGS_NAME = "1.8-mappings";

    public static final String MAPPINGS_VERSION = MINECRAFT_VERSION + "+build." + System.getenv().getOrDefault("BUILD_NUMBER", "local");

    static Configuration ENIGMA_CONFIGURATION;

    public static Configuration getEnigmaConfiguration() {
        return ENIGMA_CONFIGURATION;
    }

    public static final class Groups {
        public static final String SETUP_GROUP = "jar setup";
        public static final String MAPPINGS_GROUP = MAPPINGS_NAME;
        public static final String BUILD_MAPPINGS_GROUP = "build mappings";
    }
}
