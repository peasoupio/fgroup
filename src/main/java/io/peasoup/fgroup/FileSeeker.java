package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Path;

/**
 * Seek and capture file based on a configuration file.
 */
public class FileSeeker {

    public static final String DEFAULT_ROOT_DIRECTORY = System.getProperty("user.dir");

    private final Configuration configuration;

    /**
     * Create a new FileSeeker instance based on a specific configuration file
     * @param configFilepath Configuration file location
     */
    public FileSeeker(String configFilepath) {
        if (StringUtils.isEmpty(configFilepath))
            throw new IllegalArgumentException("configFilepath");

        this.configuration = new Parser().parse(configFilepath);
    }

    /**
     * Seek fo file matches using the current working directory, also known as System.getProperty("user.dir")
     * @return A new instance of file matches seeked.
     */
    public FileMatches seek() {
        return seek(DEFAULT_ROOT_DIRECTORY);
    }

    /**
     * Seek for file matches using a specific root directory.
     * @param rootDirectory The root directory. Required.
     * @return A new instance of file matches seeked.
     */
    public FileMatches seek(String rootDirectory) {
        if (StringUtils.isEmpty(rootDirectory))
            throw new IllegalArgumentException("rootDirectory");

        FileMatches globalFileMatches = new FileMatches();

        for (Parser.ConfigurationSection section : configuration.getSections()) {
            FileMatches fileMatches = new FileMatches();

            for (Path startingPointRoot: section.getStartingPoint().getPaths(rootDirectory)) {
                new FileWalker(startingPointRoot, section).walk(fileMatches);
            }

            globalFileMatches.merge(fileMatches);
        }

        return globalFileMatches;
    }
}
