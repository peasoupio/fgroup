package io.peasoup.fgroup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileWalker {

    private static final Integer INITIAL_FILTER_POSITION = 1;

    private final Path root;
    private final Parser.ConfigurationSection configurationSection;

    public FileWalker(Path root, Parser.ConfigurationSection configurationSection) {
        if (root == null)
            throw new IllegalArgumentException("root");
        if (configurationSection == null)
            throw new IllegalArgumentException("configurationSection");

        this.root = root;
        this.configurationSection = configurationSection;
    }

    public void walk(FileMatches fileMatches) {
        LinkedList<Path> pathStack = new LinkedList<>();

        // Get indexed filters
        Map<Integer, List<Parser.SectionFilter>> indexedFilters = configurationSection.index();
        if (indexedFilters.isEmpty()) return;
        if (!indexedFilters.containsKey(INITIAL_FILTER_POSITION)) return;

        // Add first index into the queue
        for (Parser.SectionFilter sectionFilter : indexedFilters.get(INITIAL_FILTER_POSITION)) {
            processSection(pathStack, root, sectionFilter, fileMatches);
        }

        // Number of path to check before increasing the position
        int currentLimit = pathStack.size();
        int currentPosition = 2;

        while(currentPosition <= indexedFilters.size()) {
            while (currentLimit > 0) {
                if (currentPosition > indexedFilters.size())
                    break;

                Path current = pathStack.poll();

                for (Parser.SectionFilter sectionFilter : indexedFilters.get(currentPosition)) {
                    processSection(pathStack, current, sectionFilter, fileMatches);
                }
                currentLimit--;
            }

            // Reset current limit to the expected processing
            currentLimit = pathStack.size();

            // Set the next position
            currentPosition++;
        }
    }

    private void processSection(LinkedList<Path> pathStack, Path current, Parser.SectionFilter sectionFilter, FileMatches fileMatches) {
        if (!sectionFilter.getFilter().isRegex()) {
            Path guessingPath = current.resolve(sectionFilter.getFilter().getMatch());
            if (Files.exists(guessingPath)) {
                pathStack.add(guessingPath);

                if (sectionFilter.getFilter().hasKeyword())
                    fileMatches.match(sectionFilter.getFilter().getKeyword(), root, guessingPath);
            }
        } else {
            Pattern pattern = Pattern.compile(sectionFilter.getFilter().convertRegex());
            try (Stream<Path> paths = Files.list(current)){
                paths
                    .filter(path -> pattern.matcher(path.getFileName().toString()).matches())
                    .filter(Files::exists)
                    .forEach(guessingPath -> {
                        pathStack.add(guessingPath);

                        if (sectionFilter.getFilter().hasKeyword())
                            fileMatches.match(sectionFilter.getFilter().getKeyword(), root, guessingPath);
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
