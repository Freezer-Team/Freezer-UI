package nep.timeline.freezerUI.configs.checkers;

import android.os.Build;

import java.util.HashSet;
import java.util.Set;

import nep.timeline.freezerUI.GlobalVars;

public class SettingsConfigChecker {
    public static boolean isBackgroundIntent() {
        return GlobalVars.globalSettings.backgroundIntent || !isProMode();
    }

    public static boolean isDoze() {
        return GlobalVars.globalSettings.useDoze && !isLiteMode() && !isFullyAutomatic();
    }

    public static boolean isIgnorePushBroadcasts() {
        return GlobalVars.globalSettings.ignorePushBroadcasts;
    }

    public static boolean isFCMFix() {
        return GlobalVars.globalSettings.fcmfix;
    }

    public static boolean isBlockAutoClear() {
        return GlobalVars.globalSettings.blockAutoClear;
    }

    public static boolean isRelaunchOnNotificationClick() {
        return GlobalVars.globalSettings.relaunchOnNotificationClick;
    }

    public static boolean isAllowNonHighAccuracyLocation() {
        return (GlobalVars.globalSettings.allowNonHighAccuracyLocation && !isProMode()) || isFullyAutomatic();
    }

    public static boolean isSuExecute() {
        return GlobalVars.globalSettings.suExecute && isProMode();
    }

    public static boolean isBootFreeze() {
        return GlobalVars.globalSettings.bootFreeze || !isProMode();
    }

    public static boolean allowLiveUpdate() {
        return GlobalVars.globalSettings.liveUpdate;
    }

    public static boolean isRotationUnfreeze() {
        return GlobalVars.globalSettings.rotationUnfreeze && BinderModeChecker.getBinderMode() == BinderModeChecker.BinderMode.DISABLE;
    }

    public static boolean isComputeOOM() {
        return GlobalVars.globalSettings.computeOOM && isProMode() && Build.VERSION.SDK_INT > Build.VERSION_CODES.P;
    }

    public static boolean isDumpThaw() {
        return GlobalVars.globalSettings.dumpThaw;
    }

    public static long getFreezeInterval() {
        return GlobalVars.globalSettings.freezeInterval;
    }

    public static long getTemporaryInterval() {
        return GlobalVars.globalSettings.temporaryInterval;
    }

    public static long getBinderInterval() {
        return GlobalVars.globalSettings.binderInterval;
    }

    public static long getFlymeInterval() {
        return GlobalVars.globalSettings.flymeInterval;
    }

    public static boolean isIgnoreXposedModule() {
        return GlobalVars.globalSettings.ignoreXposedModule || isLiteMode() || isFullyAutomatic();
    }

    public static boolean isNotificationKeep() {
        return GlobalVars.globalSettings.notificationKeep || isLiteMode() || isFullyAutomatic();
    }

    public static boolean isForegroundLastAppWhenScreenOff() {
        return GlobalVars.globalSettings.foregroundLastAppWhenScreenOff && isProMode();
    }

    public static boolean isMemoryCompact() {
        return GlobalVars.globalSettings.memoryCompact;
    }

    public static int getMemoryReclaimThreshold() {
        return GlobalVars.globalSettings.memoryReclaimThreshold;
    }

    public static int getCompactDelay() {
        return GlobalVars.globalSettings.compactDelay;
    }

    public static boolean isCommandInject() {
        return GlobalVars.globalSettings.commandInject;
    }

    public static Set<String> getAllowBroadcastIntents() {
        Set<String> intents = GlobalVars.globalSettings.allowBroadcastIntents;
        if (intents == null)
            return new HashSet<>();
        return intents;
    }

    public static boolean isPowerSaveMode() {
        return !GlobalVars.globalSettings.disablePowerSaveMode || isLiteMode() || isFullyAutomatic();
    }

    public static boolean isSuperStandbyMode() {
        return (GlobalVars.globalSettings.superStandbyMode && isPowerSaveMode()) || isFullyAutomatic();
    }

    public static boolean isExtremeStandbyMode() {
        return (GlobalVars.globalSettings.extremeStandbyMode && isSuperStandbyMode()) || isFullyAutomatic();
    }

    public static boolean isFasterNetworkRecovery() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE;
    }

    public static boolean isDisableClearTask() {
        return GlobalVars.globalSettings.disableClearTask;
    }

    public static boolean isCheckForegroundService() {
        return GlobalVars.globalSettings.checkForegroundService && !isFullyAutomatic() && !isProMode() && Build.VERSION.SDK_INT > Build.VERSION_CODES.P;
    }

    public static boolean isBatteryOptIgnoreProMode() {
        return GlobalVars.globalSettings.batteryOptIgnoreProMode;
    }

    public static boolean isKeepAllProcessAlive() {
        return GlobalVars.globalSettings.keepAllProcessAlive;
    }

    public static int getMemoryThreshold() {
        return GlobalVars.globalSettings.memoryThreshold;
    }

    public static Set<String> getMemoryThresholdWhitelistApp() {
        return GlobalVars.globalSettings.memoryThresholdWhitelistApp;
    }

    public static boolean isLiteMode() {
        return GlobalVars.globalSettings.liteMode && !isFullyAutomatic();
    }

    public static boolean isFullyAutomatic() {
        return GlobalVars.globalSettings.fullyAutomatic;
    }

    public static boolean isProMode() {
        return GlobalVars.globalSettings.proMode && !isLiteMode() && !isFullyAutomatic();
    }

    public static boolean isCompatibleMode() {
        return GlobalVars.globalSettings.compatibleMode || isLiteMode() || isFullyAutomatic();
    }
}
