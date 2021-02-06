package io.peasoup.fgroup;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeStartingPoint implements StartingPoint {

    @Override
    public String getName() {
        return "tree";
    }

    @Override
    public List<Path> getPaths(String rootDirectory) {
        List<Path> paths = new ArrayList<>();
        Path currentPath = new File(rootDirectory).toPath();

        do {
            paths.add(currentPath);
            currentPath = currentPath.getParent();
        }
        while(currentPath != null);

        Collections.reverse(paths);

        return paths;
    }
}
