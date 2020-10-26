package io.peasoup.fgroup;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileSeekerTest {

    String testConfigFile;

    @Given("seeker configuration file is {string}")
    public void seeker_configuration_file_is(String testConfigFileStr) {
        this.testConfigFile = ParserTest.class.getResource(testConfigFileStr).getFile();
    }

    @When("I set the user.dir to the test resources folder")
    public void i_set_the_user_dir_to_the_test_resources_folder() {
        UserSettings.setUserDir(ParserTest.class.getResource("/fileseeker/files").getPath());
    }
    @Then("I should be told the seeker report file {string}")
    public void i_should_be_told_the_seeker_report_file(String string) throws IOException {
        Path reportPath = new File(ParserTest.class.getResource(string).getFile()).toPath();
        String testReportContent = new String(Files.readAllBytes(reportPath));

        FileMatches fileMatches = FileSeeker.seek(testConfigFile);

        assertNotNull(fileMatches);
        assertEquals(testReportContent, fileMatches.toString());
    }

}