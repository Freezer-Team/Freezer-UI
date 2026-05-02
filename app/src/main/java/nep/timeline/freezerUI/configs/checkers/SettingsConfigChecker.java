package nep.timeline.freezerUI.configs.checkers;

import nep.timeline.freezerUI.GlobalVars;

public class SettingsConfigChecker {
    public static boolean isBackgroundIntent() {
        return GlobalVars.globalSettings.backgroundIntent || !isProMode();
    }

    public static boolean isBinderFreeze() {
        return GlobalVars.globalSettings.binderFreeze && !isLiteMode() && !isFullyAutomatic();
    }

    public static boolean isSuExecute() {
        return GlobalVars.globalSettings.suExecute && isProMode();
    }

    public static boolean isIgnoreXposedModule() {
        return GlobalVars.globalSettings.ignoreXposedModule || isLiteMode() || isFullyAutomatic();
    }

    public static boolean isBatteryOptIgnoreProMode() {
        return GlobalVars.globalSettings.batteryOptIgnoreProMode;
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
