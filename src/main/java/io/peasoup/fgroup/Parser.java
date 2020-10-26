package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    private static final char SPACE = 32;
    private static final char COMMENT = 35;
    private static final char NULL = 0;

    public Configuration parse(String file) {
        Configuration config = new Configuration();
        if (StringUtils.isEmpty(file)) return config;

        Path path = new File(file).toPath();
        if (!Files.exists(path)) return config;

        config.setSource(path);

        linesProcessor(config);

        return config;
    }

    private void linesProcessor(Configuration config) {
        ConfigurationSection currentSection = null;

        try (Scanner scanner = new Scanner(config.getSource().toFile())) {
            while (scanner.hasNextLine()) {
                ConfigurationSection nextSection = sectionProcessor(scanner.nextLine(), currentSection);

                if (nextSection == null ||
                    nextSection == currentSection) continue;

                currentSection = nextSection;
                config.addSection(currentSection);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Sort filters on their position
        if (currentSection != null) {
            currentSection.sort();
        }
    }

    /**
     * Process a line for a current configuration section.
     * @param line Line to process.
     * @param currentSection Current configuration section.
     * @return New instance if a new section is discovered, if same section, will be currentSection reference, otherwise it returns null;
     */
    private ConfigurationSection sectionProcessor(String line, ConfigurationSection currentSection) {

        // Skip if line is empty
        if (StringUtils.isEmpty(line)) return null;

        StartingPoint startingPoint = StartingPoint.get(line);

        // Not null means a new token, thus section is discovered.
        if (startingPoint != null) return new ConfigurationSection(startingPoint);

        // Null and no current section means there's nothing valid to process
        if (currentSection == null) return null;


        SectionFilter sectionFilter = getFilterString(line);
        if (sectionFilter == null)
            return null;

        currentSection.add(sectionFilter);

        return currentSection;
    }

    private SectionFilter getFilterString(String line) {
        int position = 0;
        String filterStr = null;

        // Look for a valid matching string
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '#') {
                return null;
            } else if (line.charAt(i) == '\t') {
                position++;
            } else if (line.charAt(i) == ' ') {

                // TODO 4 is arbitrtaty, should be configurable
                // Check if the next 3 caracters are spaces, thus matching a "tab" character
                switch (isValidSpace(line, i)) {
                    case COMMENT: return null;
                    case NULL:
                        i += 3;
                        continue;
                    default:
                        i += 3;
                        position++;
                }

            }

            filterStr = line.substring(i);
        }

        if (StringUtils.isEmpty(filterStr)) return null;
        // TODO log me
        if (filterStr.contains("/") ||
                filterStr.contains("$") ||
                filterStr.contains(":") ||
                filterStr.contains("\"") ||
                filterStr.contains("\\") ||
                filterStr.contains("<") ||
                filterStr.contains(">") ||
                filterStr.contains("|")) return null;

        return new SectionFilter(filterStr.trim(), position);
    }

    /**
     * Determines if the 4 caracters following the begin index in a line are spaces.
     * @param line Text line
     * @param beginIndex Begin index to look at
     * @return " " if the following 4 characters are spaces, "" if not and '#' if a comment is discovered
     */
    private char isValidSpace(String line, int beginIndex) {
        for (int i = beginIndex + 1; i < beginIndex + 4; i++) {
            if (line.charAt(i) == '#') {
                return COMMENT; // '#'
            } else if (line.charAt(i) != ' ') {
                return NULL; // NUL
            }
        }

        return SPACE; // Space
    }

    static class ConfigurationSection {

        private final StartingPoint startingPoint;
        private final List<SectionFilter> filters;

        private int highestPosition = 0;

        ConfigurationSection(StartingPoint startingPoint) {
            if (startingPoint == null) throw new IllegalArgumentException("startingPoint");

            this.startingPoint = startingPoint;
            this.filters = new ArrayList<>();
        }

        public StartingPoint getStartingPoint() {
            return startingPoint;
        }

        public Iterable<SectionFilter> getFilters() {
            return filters;
        }

        public void add(SectionFilter sectionFilter) {
            if (sectionFilter == null)
                throw new IllegalArgumentException("sectionFilter");

            // Cannot skip a position
            if (sectionFilter.getPosition() - 1 > highestPosition)
                return;

            filters.add(sectionFilter);
            highestPosition++;
        }

        public void sort() {
            filters.sort(Comparator.comparingInt(SectionFilter::getPosition));
        }

        public Map<Integer, List<SectionFilter>> index() {
            return filters.stream()
                    .collect(Collectors.groupingBy(Parser.SectionFilter::getPosition));
        }

    }

    static class SectionFilter {
        private final Filter filter;
        private final int position;

        SectionFilter(String filterStr, int position) {
            if (StringUtils.isEmpty(filterStr)) throw new IllegalArgumentException("filterStr");
            if (position < 0) throw new IllegalArgumentException("position");

            this.filter = new Filter(filterStr);
            this.position = position;
        }

        public Filter getFilter() {
            return filter;
        }

        public int getPosition() {
            return position;
        }
    }

}
