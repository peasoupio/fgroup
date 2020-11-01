package io.peasoup.fgroup;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private final List<Parser.ConfigurationSection> sections;
    private Path source;

    public Configuration() {
        this.sections = new ArrayList<>();
    }

    public void setSource(Path source) {
        if (source == null) throw new IllegalArgumentException("source");

        this.source = source;
    }

    public Path getSource() {
        return source;
    }

    public void addSection(Parser.ConfigurationSection configurationSection) {
        if (configurationSection == null) throw new IllegalArgumentException("configurationSection");

        this.sections.add(configurationSection);
    }

    public Iterable<Parser.ConfigurationSection> getSections() {
        return this.sections;
    }


    public String toString(String rootDirectory) {
        if (sections.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();
        for (Parser.ConfigurationSection section: sections) {
            sb.append(StartingPoint.toString(section.getStartingPoint(), rootDirectory));

            int currentPosition = 0;
            for(Parser.SectionFilter filter : section.getFilters()) {
                if (filter.getPosition() > currentPosition) {
                    sb.append("/");
                    currentPosition++;
                } else
                    sb.append("|");

                sb.append(filter.getFilter());
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
