package nep.timeline.freezerUI.configs.checkers;

import java.util.Objects;

import nep.timeline.freezerUI.GlobalVars;
import nep.timeline.freezerUI.utils.PackageUtils;

public class AppConfigs {
    public static boolean isWhiteApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.whiteApps.contains(key);
    }

    public static boolean isWhiteApp(String packageName)
    {
        return isWhiteApp(packageName, 0);
    }

    public static int getBackgroundLevelApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);

        int result = 0;
        if (GlobalVars.applicationSettings.backgroundLevelApps.containsKey(key)) {
            Integer value = GlobalVars.applicationSettings.backgroundLevelApps.get(key);
            result = Objects.requireNonNullElse(value, 0);
        }
        return result;
    }

    public static int getBackgroundLevelApp(String packageName)
    {
        return getBackgroundLevelApp(packageName, 0);
    }

    public static boolean isBlackSystemApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.blackSystemApps.contains(key);
    }

    public static boolean isBlackSystemApp(String packageName)
    {
        return isBlackSystemApp(packageName, 0);
    }

    public static boolean isWhiteProcess(String processName, int userId)
    {
        if (SettingsConfigChecker.isLiteMode() || SettingsConfigChecker.isFullyAutomatic())
            return false;

        String key = PackageUtils.toConfigKey(userId, processName, true);

        return GlobalVars.applicationSettings.whiteProcessList.contains(key);
    }

    public static boolean isWhiteProcess(String processName)
    {
        return isWhiteProcess(processName, 0);
    }

    public static int getWhiteProcessCount(String packageName, int userId)
    {
        if (SettingsConfigChecker.isLiteMode() || SettingsConfigChecker.isFullyAutomatic())
            return 0;

        String key = PackageUtils.toConfigKey(userId, packageName, true);

        return (int) GlobalVars.applicationSettings.whiteProcessList.stream()
                .filter(proc -> proc.contains("#"))
                .filter(proc -> (proc.contains(":") ? proc.substring(0, proc.indexOf(':')) + "#" + proc.substring(proc.lastIndexOf('#') + 1) : proc).equals(key))
                .count();
    }

    public static int getWhiteProcessCount(String packageName)
    {
        return getWhiteProcessCount(packageName, 0);
    }

    public static boolean isKillProcess(String processName, int userId)
    {
        if (SettingsConfigChecker.isLiteMode() || SettingsConfigChecker.isFullyAutomatic())
            return false;

        String key = PackageUtils.toConfigKey(userId, processName, true);
        
        return GlobalVars.applicationSettings.killProcessList.contains(key);
    }

    public static boolean isKillProcess(String processName)
    {
        return isKillProcess(processName, 0);
    }

    public static boolean isSocketApp(String packageName, int userId)
    {
        if (SettingsConfigChecker.isFullyAutomatic())
            return "com.tencent.mm".equals(packageName) || "com.tencent.mobileqq".equals(packageName);
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.socketApps.contains(key);
    }

    public static boolean isSocketApp(String packageName)
    {
        return isSocketApp(packageName, 0);
    }

    public static boolean isNetReceiveApp(String packageName, int userId)
    {
        if (SettingsConfigChecker.isFullyAutomatic())
            return "com.tencent.mm".equals(packageName) || "com.tencent.mobileqq".equals(packageName);

        if (!isSocketApp(packageName, userId))
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.netReceiveApps.contains(key);
    }

    public static boolean isNetReceiveApp(String packageName)
    {
        return isNetReceiveApp(packageName, 0);
    }

    public static boolean isNetworkCheckApp(String packageName, int userId)
    {
        if (SettingsConfigChecker.isFullyAutomatic())
            return true;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.networkCheckApps.contains(key);
    }

    public static boolean isNetworkCheckApp(String packageName)
    {
        return isNetworkCheckApp(packageName, 0);
    }

    public static boolean isIdleApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.idleApps.contains(key);
    }

    public static boolean isIdleApp(String packageName)
    {
        return isIdleApp(packageName, 0);
    }

    public static boolean isIntervalUnfreezeApp(String packageName, int userId)
    {
        if (SettingsConfigChecker.isFullyAutomatic())
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);

        return GlobalVars.applicationSettings.intervalUnfreezeApps.containsKey(key);
    }

    public static int getAppIntervalUnfreezeDelay(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);

        Integer value = GlobalVars.applicationSettings.intervalUnfreezeApps.get(key);
        return Objects.requireNonNullElse(value, 0);
    }

    public static int getAppIntervalUnfreezeDelay(String packageName)
    {
        return getAppIntervalUnfreezeDelay(packageName, 0);
    }

    public static boolean isIntervalUnfreezeApp(String packageName)
    {
        return isIntervalUnfreezeApp(packageName, 0);
    }

    public static boolean isBackgroundPlayApp(String packageName, int userId)
    {
        if (!SettingsConfigChecker.isProMode())
            return true;

        String key = PackageUtils.toConfigKey(userId, packageName, true);

        return GlobalVars.applicationSettings.backgroundPlayApps.contains(key);
    }

    public static boolean isBackgroundPlayApp(String packageName)
    {
        return isBackgroundPlayApp(packageName, 0);
    }

    public static boolean isBackgroundIntentApp(String packageName, int userId)
    {
        if (!SettingsConfigChecker.isProMode())
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.backgroundIntentApps.contains(key);
    }

    public static boolean isBackgroundIntentApp(String packageName)
    {
        return isBackgroundIntentApp(packageName, 0);
    }

    public static boolean isNotificationKeepApp(String packageName, int userId)
    {
        if (SettingsConfigChecker.isLiteMode() || SettingsConfigChecker.isFullyAutomatic())
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.notificationKeepApps.contains(key);
    }

    public static boolean isNotificationKeepApp(String packageName)
    {
        return isNotificationKeepApp(packageName, 0);
    }

    public static int getLocationCheckApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);

        int result = 0;
        if (GlobalVars.applicationSettings.locationCheckApps.containsKey(key)) {
            Integer value = GlobalVars.applicationSettings.locationCheckApps.get(key);
            result = Objects.requireNonNullElse(value, 0);
        }
        return result;
    }

    public static int getLocationCheckApp(String packageName)
    {
        return getLocationCheckApp(packageName, 0);
    }

    public static boolean isIgnoreRecordingApp(String packageName, int userId)
    {
        if (!SettingsConfigChecker.isProMode())
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        
        return GlobalVars.applicationSettings.ignoreRecordingApps.contains(key);
    }

    public static boolean isIgnoreRecordingApp(String packageName)
    {
        return isIgnoreRecordingApp(packageName, 0);
    }

    public static int getOOMLevel(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);

        int result = 0;
        if (GlobalVars.applicationSettings.oomPriorityApps.containsKey(key)) {
            Integer value = GlobalVars.applicationSettings.oomPriorityApps.get(key);
            result = Objects.requireNonNullElse(value, 0);
        }
        return result;
    }

    public static int getOOMLevel(String packageName)
    {
        return getOOMLevel(packageName, 0);
    }

    public static boolean isBluetoothCheck(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);
        return GlobalVars.applicationSettings.bluetoothCheckApps.contains(key);
    }

    public static boolean isBluetoothCheck(String packageName)
    {
        return isBluetoothCheck(packageName, 0);
    }

    public static boolean isIgnoreBinderApp(String packageName, int userId)
    {
        if (!SettingsConfigChecker.isProMode())
            return false;

        String key = PackageUtils.toConfigKey(userId, packageName, true);
        return GlobalVars.applicationSettings.ignoreBinderApps.contains(key);
    }

    public static boolean isIgnoreBinderApp(String packageName)
    {
        return isIgnoreBinderApp(packageName, 0);
    }

    public static boolean isAllowAPIInjectionApp(String packageName, int userId)
    {
        String key = PackageUtils.toConfigKey(userId, packageName, true);

        return GlobalVars.applicationSettings.allowAPIInjectionApps.contains(key);
    }

    public static boolean isAllowAPIInjectionApp(String packageName)
    {
        return isAllowAPIInjectionApp(packageName, 0);
    }
}
