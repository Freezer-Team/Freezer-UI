package nep.timeline.freezerUI.configs.checkers;

import nep.timeline.freezerUI.GlobalVars;

public class BatteryOptModeChecker {
    public static BatteryOptMode getBatteryOptMode() {
        if (!SettingsConfigChecker.isProMode() && !SettingsConfigChecker.isBatteryOptIgnoreProMode())
            return BatteryOptMode.DISABLE;

        return switch (GlobalVars.globalSettings.batteryOptControlMode) {
            case 1 -> BatteryOptMode.AUTO;
            case 2 -> BatteryOptMode.FULL;
            case 3 -> BatteryOptMode.WHITELIST;
            default -> BatteryOptMode.DISABLE;
        };
    }

    public enum BatteryOptMode {
        DISABLE,
        AUTO,
        FULL,
        WHITELIST
    }
}
