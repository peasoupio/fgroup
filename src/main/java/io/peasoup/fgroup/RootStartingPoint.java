package io.peasoup.fgroup;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class RootStartingPoint implements StartingPoint {

    @Override
    public String getName() {
        return "root";
    }

    @Override
    public List<Path> getPaths(String rootDirectory) {
        Path currentPath = new File(rootDirectory).toPath();
        while(currentPath.getParent() != null)
            currentPath = currentPath.getParent();

        return Collections.singletonList(currentPath);
    }
}
