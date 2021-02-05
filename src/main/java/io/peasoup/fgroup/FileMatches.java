package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMatches {

    private final Map<String, List<FileMatchRecord>> capturedFile;

    public FileMatches() {
        this.capturedFile = new HashMap<>();
    }

    /**
     * Merge results from another FileMatches instance into this one.
     * @param other The other FileMatches instance.
     */
    public void merge(FileMatches other) {
        if (other == null)
            throw new IllegalArgumentException("other");

        // Will not process itself
        if (other == this)
            return;

        for(Map.Entry<String, List<FileMatchRecord>> kpv : other.capturedFile.entrySet()) {
            if (!this.capturedFile.containsKey(kpv.getKey())) {
                this.capturedFile.put(kpv.getKey(), kpv.getValue());
                continue;
            }

            // If keyword exists, check if some paths are missing
            List<FileMatchRecord> myKeywordPaths = this.capturedFile.get(kpv.getKey());
            for(FileMatchRecord otherPath : kpv.getValue()) {
                if (!myKeywordPaths.contains(otherPath))
                    myKeywordPaths.add(otherPath);
            }
        }
    }

    /**
     * Add a matching keyword from its startingPointRoot and the found file's path
     * @param keyword Corresponding keyword.
     * @param startingPointRoot The starting point root path
     * @param path The found file's path
     */
    public void match(String keyword, Path startingPointRoot, Path path) {
        if (StringUtils.isEmpty(keyword))
            throw new IllegalArgumentException("keyword");

        if (path == null)
            throw new IllegalArgumentException("path");

        if (!Files.exists(path))
            throw new IllegalStateException("path does not exists");

        // Make sure this keyword as its list
        if (!capturedFile.containsKey(keyword))
            capturedFile.put(keyword, new ArrayList<>());

        // Add path to keywords list
        List<FileMatchRecord> keywordPaths = capturedFile.get(keyword);
        FileMatchRecord record = new FileMatchRecord(startingPointRoot, path);

        if (!keywordPaths.contains(record))
            keywordPaths.add(record);
    }

    /**
     * Gets all FileMatchRecords for a specific keyword
     * @param keyword The keyword. Required.
     * @return Iterable of FileMatchRecords
     */
    public Iterable<FileMatchRecord> get(String keyword) {
        if (StringUtils.isEmpty(keyword))
            throw new IllegalArgumentException("keyword");

        if (!this.capturedFile.containsKey(keyword))
            return Collections.emptyList();

        return this.capturedFile.get(keyword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<String> keywords = new ArrayList<>(this.capturedFile.keySet());
        Collections.sort(keywords);

        for(int i = 0; i<keywords.size(); i++) {
            if (i > 0)
                sb.append(System.lineSeparator());

            String key = keywords.get(i);
            sb.append(key);

            for (FileMatchRecord fileMatchRecord : this.capturedFile.get(key)) {
                sb.append(System.lineSeparator());
                sb.append('\t').append(fileMatchRecord);
            }
        }

        return sb.toString();
    }

    public static class FileMatchRecord {

        private final Path startingPointRoot;
        private final Path match;

        public FileMatchRecord(Path startingPointRoot, Path match) {
            if (startingPointRoot == null || !Files.exists(startingPointRoot))
                throw new IllegalArgumentException("startingPointRoot");

            if (match == null || !Files.exists(match))
                throw new IllegalArgumentException("match");

            this.startingPointRoot = startingPointRoot;
            this.match = match;
        }

        /**
         * Gets the starting point root path.
         * @return The path.
         */
        public Path getStartingPointRoot() {
            return this.startingPointRoot;
        }

        /**
         * Gets the matched file path.
         * @return The path.
         */
        public Path getMatch() {
            return this.match;
        }

        @Override
        public int hashCode() {
            return this.match.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.match.equals(obj);
        }

        @Override
        public String toString() {
            return this.startingPointRoot.relativize(this.match).toString().replace("\\", "/");
        }
    }
}
