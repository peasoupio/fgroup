package io.peasoup.fgroup;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class RunCucumberTest {

    /**
     * Converts Gherkin (Cucumber) expected answer as null if value is "null"
     * @param value Value to check.
     * @return Nullified value if equals to "null"
     */
    public static String nullify(String value) {
        if (StringUtils.isEmpty(value)) return value;
        if (value.equals("null")) return null;
        return value;
    }

}
