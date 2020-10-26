package io.peasoup.fgroup;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    String testConfigFile;

    @Given("configuration file is {string}")
    public void configuration_file_is(String testConfigFileStr) {
        this.testConfigFile = ParserTest.class.getResource(testConfigFileStr).getFile();
    }

    @When("I set the user.dir {string} to determine the parsing context")
    public void i_set_the_user_dir_to_determine_the_parsing_context(String userDir) {
        UserSettings.setUserDir(userDir);
    }
    @Then("I should be told the filter chain {string}")
    public void i_should_be_told_the_filter_chain(String expectedOutput) {
        Configuration config = new Parser().parse(testConfigFile);

        assertEquals(RunCucumberTest.nullify(expectedOutput), config.toString().trim());
    }

}