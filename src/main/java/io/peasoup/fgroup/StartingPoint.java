package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public interface StartingPoint {

    /**
     * Gets the name of this starting point.
     * @return String representation of this starting point.
     */
    String getName();

    /**
     * Gets a list of path which become starting points for file lookup.
     * Paths must be ordered from the widest to the most narrow path.
     * @return List of Path.
     */
    List<Path> getPaths();

    /**
     * Get a StartingPoint object based on a token.
     * @param token Specified token.
     * @return A new instance of a specialized starting point, otherwise null.
     */
    static StartingPoint get(String token) {
        if (StringUtils.isEmpty(token)) return null;

        switch (token) {
            case "^/": return new RootStartingPoint();
            case "*/": return new TreeStartingPoint();
            case "./": return new CurrentStartingPoint();
            case "../": return new ParentStartingPoint();
            default: return null;
        }
    }

    static String toString(StartingPoint sp) {
        if (sp == null)
            throw new IllegalArgumentException("sp");

        StringBuilder sb = new StringBuilder();

        // Starting point name prefix
        sb.append("[").append(sp.getName().toUpperCase()).append("] ");

        // Get paths as strings
        List<String> stringPaths = sp.getPaths().stream()
                // Change Windows-style to Unix-style file separator
                .map(p -> p.toString().replace('\\', '/'))
                // Make sure it does not end with a "/"
                .map(s -> s.endsWith("/") ? s.substring(0, s.length() - 1) : s)
                .collect(Collectors.toList());

        // Add paths and join them by a ","
        sb.append("[").append(String.join(",", stringPaths)).append("]");

        return sb.toString();
    }
}
