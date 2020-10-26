package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Path;

public class FileSeeker {

    private FileSeeker() {
        // private ctor
    }

    public static FileMatches seek(String configFilepath) {
        if (StringUtils.isEmpty(configFilepath))
            return null;

        Configuration config = new Parser().parse(configFilepath);
        FileMatches globalFileMatches = new FileMatches();

        for (Parser.ConfigurationSection section : config.getSections()) {
            FileMatches fileMatches = new FileMatches();

            for (Path root: section.getStartingPoint().getPaths()) {
                new FileWalker(root, section).walk(fileMatches);
            }

            globalFileMatches.merge(fileMatches);
        }

        return globalFileMatches;
    }
}
