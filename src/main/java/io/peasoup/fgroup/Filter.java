package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

    private static final String CAPTURE_KEYWORD_PATTERN_REGEX = "^\\((?<match>.*) as '(?<keyword>[a-zA-Z]*)'\\)$";
    private static final Pattern CAPTURE_KEYWORD_PATTERN = Pattern.compile(CAPTURE_KEYWORD_PATTERN_REGEX);

    private final String match;
    private final String keyword;

    public Filter(String match) {
        if (StringUtils.isEmpty(match))
            throw new IllegalArgumentException("match");

        Matcher matcher = CAPTURE_KEYWORD_PATTERN.matcher(match);

        if (matcher.matches()) {
            this.match = matcher.group("match");
            this.keyword = matcher.group("keyword");
        } else {
            this.match = match;
            this.keyword = null;
        }
    }

    public String getMatch() {
        return this.match;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public boolean hasKeyword() {
        return this.keyword != null;
    }

    public boolean isRegex() { return this.match.contains("*"); }

    public String convertRegex() {
        return this.match.replace("*", ".*");
    }

    @Override
    public String toString() {
        return this.match;
    }
}
