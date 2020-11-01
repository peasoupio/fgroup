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

    public void merge(FileMatches other) {
        if (other == null)
            throw new IllegalArgumentException("other");

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

    public void match(String keyword, Path root, Path path) {
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
        FileMatchRecord record = new FileMatchRecord(root, path);

        if (!keywordPaths.contains(record))
            keywordPaths.add(record);
    }

    public Iterable<FileMatchRecord> get(String keyword) {
        if (StringUtils.isEmpty(keyword))
            throw new IllegalArgumentException("keyword");

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

        private final Path root;
        private final Path current;

        public FileMatchRecord(Path root, Path current) {
            if (root == null || !Files.exists(root))
                throw new IllegalArgumentException("root");

            if (current == null || !Files.exists(current))
                throw new IllegalArgumentException("current");

            this.root = root;
            this.current = current;
        }

        public Path getRoot() {
            return this.root;
        }

        public Path getCurrent() {
            return this.current;
        }

        @Override
        public int hashCode() {
            return this.current.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.current.equals(obj);
        }

        @Override
        public String toString() {
            return this.root.relativize(this.current).toString().replace("\\", "/");
        }
    }
}
