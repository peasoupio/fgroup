package io.peasoup.fgroup;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CurrentStartingPoint implements StartingPoint{

    @Override
    public String getName() {
        return "current";
    }

    @Override
    public List<Path> getPaths(String rootDirectory) {
        return Collections.singletonList(new File(rootDirectory).toPath());
    }
}
