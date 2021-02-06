package io.peasoup.fgroup;

import org.apache.commons.lang.StringUtils;
import org.docopt.Docopt;
import org.docopt.DocoptExitException;

import java.util.Map;

public class Main {

    private Main() {
        // private ctor
    }

    /**
     * JVM entry-point. Do not call directly
     * @param args CLI args
     */
    public static void main(String[] args) {
        int exitCode = start(args);

        // Return exit code if != 0
        if (exitCode > 0)
            System.exit(exitCode);
    }

    private static int start(String[] args) {
        // Parse docopt arguments
        Map<String, Object> opts = null;

        try {
            opts = new Docopt(usage())
                    .withExit(false)
                    .parse(args);
        } catch(DocoptExitException ex) {
            System.err.print(usage());
            return -1;
        }

        String directory = (String)opts.get("--directory");
        if (StringUtils.isEmpty(directory))
            directory = FileSeeker.DEFAULT_ROOT_DIRECTORY;

        String configFileLocation = (String)opts.get("<config>");
        FileMatches fileMatches = new FileSeeker(configFileLocation).seek(directory);

        System.out.println(fileMatches.toString());

        return 0;
    }

    private static String usage() {
        return "fgroup, version: " + SystemInfo.version() + ".\n\n" +

                "Usage:\n" +
                "  fgroup [-d <dir>] <config>\n\n" +

                "Options:\n" +
                "  -d, --directory Root directory. \n" +
                "                  By default it uses the current \n" +
                "                  process directory\n\n" +

                "Parameters:\n" +
                "  <config>        Configuration file wherein instructions are\n" +
                "                  given to fgroup.\n" +
                "  <dir>           Specific root directory.\n" +
                "                  It will override the default value.";
    }
}
