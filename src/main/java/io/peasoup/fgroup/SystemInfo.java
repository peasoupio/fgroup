package io.peasoup.fgroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class SystemInfo {

    private SystemInfo() {
        // private ctor
    }

    static String version() {
        Properties releaseInfo = new Properties();
        InputStream releaseFile = SystemInfo.class.getResourceAsStream("/release.properties");
        if (releaseFile == null)
            return "undefined";

        try {
            releaseInfo.load(releaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return releaseInfo.getProperty("version");
    }
}
