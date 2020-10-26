package io.peasoup.fgroup;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StartingPointTest {

    private String token;
    private StartingPoint actualStartingPoint;

    @Given("token is {string}")
    public void token_is(String token) {
        this.token = token;
    }

    // startingpoint_token

    @When("I ask whether it's valid and has a name")
    public void i_ask_whether_it_s_valid_and_has_a_name() {
        this.actualStartingPoint = StartingPoint.get(token);
    }
    @Then("I should be told its name {string}")
    public void i_should_be_told_its_name(String expectedAnswer) {
        String name = null;
        if (actualStartingPoint != null)
            name = actualStartingPoint.getName();

        assertEquals(RunCucumberTest.nullify(expectedAnswer), name);
    }

    // startingpoint_path

    @When("I set the user.dir {string} to determine the path context")
    public void i_use_the_userdir_to_determine_the_path_context(String userDir) {
        UserSettings.setUserDir(userDir);

        this.actualStartingPoint = StartingPoint.get(token);
    }

    @Then("I should be told its paths {string}")
    public void i_should_be_told_its_paths(String string) {

        List<String> actualPaths = new ArrayList<>();
        List<String> expectedPaths = new ArrayList<>();

        // Get real expected paths
        if (RunCucumberTest.nullify(string) != null)
            expectedPaths = Arrays.stream(string.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

        // Resolve actual paths
        if (actualStartingPoint != null)
            actualPaths = this.actualStartingPoint.getPaths().stream()
                    // Change Windows path.separator ('\') to Unix one ('/')
                    .map(s -> s.toString().replace('\\', '/'))
                    .collect(Collectors.toList());

        assertTrue(actualPaths.containsAll(expectedPaths));
    }
}
