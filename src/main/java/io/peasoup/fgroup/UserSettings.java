package io.peasoup.fgroup;

public class UserSettings {

    private static String userDir = System.getProperty("user.dir");

    private UserSettings() {
        // private ctor
    }

    /**
     * Gets the current System.getenv("user.dir")
     * @return String representation of the user.dir
     */
    public static String getUserDir() {
        return userDir;
    }

    /**
     * Override and sets a different user.dir than System.getenv("user.dir")
     * @param userDir New user.dir
     */
    public static void setUserDir(String userDir) {
        UserSettings.userDir = userDir;
    }
}
