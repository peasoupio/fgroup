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
    public List<Path> getPaths() {
        Path currentPath = new File(UserSettings.getUserDir()).toPath();
        return Collections.singletonList(currentPath.getParent());
    }
}
