package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Path;

public class FileSeeker {

    private final Configuration configuration;

    public FileSeeker(String configFilepath) {
        if (StringUtils.isEmpty(configFilepath))
            throw new IllegalArgumentException("configFilepath");

        this.configuration = new Parser().parse(configFilepath);
    }

    public FileMatches seek() {
        return seek( null);
    }

    public FileMatches seek(String rootDirectory) {
        FileMatches globalFileMatches = new FileMatches();

        for (Parser.ConfigurationSection section : configuration.getSections()) {
            FileMatches fileMatches = new FileMatches();

            for (Path root: section.getStartingPoint().getPaths(rootDirectory)) {
                new FileWalker(root, section).walk(fileMatches);
            }

            globalFileMatches.merge(fileMatches);
        }

        return globalFileMatches;
    }
}
