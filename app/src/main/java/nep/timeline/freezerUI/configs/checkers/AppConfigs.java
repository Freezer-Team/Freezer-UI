package nep.timeline.freezerUI.configs.checkers;

import nep.timeline.freezerUI.GlobalVars;
import nep.timeline.freezerUI.utils.PackageUtils;

public class AppConfigs {
    public static boolean isWhiteApp(String packageName, int userId) {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.whiteApps.contains(key);
    }

    public static boolean isWhiteApp(String packageName) {
        return isWhiteApp(packageName, 0);
    }

    public static boolean isBlackSystemApp(String packageName, int userId) {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.blackSystemApps.contains(key);
    }

    public static boolean isBlackSystemApp(String packageName) {
        return isBlackSystemApp(packageName, 0);
    }
}
