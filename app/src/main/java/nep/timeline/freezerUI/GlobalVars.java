package nep.timeline.freezerUI;

import nep.timeline.freezerUI.configs.settings.ApplicationSettings;
import nep.timeline.freezerUI.configs.settings.GlobalSettings;

public class GlobalVars {
    public static final String TAG = "Freezer";
    public static final String CONFIG = "Freezer";
    public final static String CONFIG_DIR = "/data/system_de/0/" + CONFIG;
    public final static String LOG_DIR = CONFIG_DIR + "/log";
    public static GlobalSettings globalSettings = null;
    public static ApplicationSettings applicationSettings = null;
}
