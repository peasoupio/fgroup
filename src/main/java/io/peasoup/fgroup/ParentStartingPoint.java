package io.peasoup.fgroup;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ParentStartingPoint implements StartingPoint {

    @Override
    public String getName() {
        return "parent";
    }

    @Override
    public List<Path> getPaths(String rootDirectory) {
        Path currentPath = new File(rootDirectory).toPath();
        return Collections.singletonList(currentPath.getParent());
    }
}
