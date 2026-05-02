package nep.timeline.freezerUI.utils;

import android.os.Build;

public class VersionUtils {
    public static String getAndroidVersion() {
        return switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.BAKLAVA -> "16";
            case Build.VERSION_CODES.VANILLA_ICE_CREAM -> "15";
            case Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> "14";
            case Build.VERSION_CODES.TIRAMISU -> "13";
            case Build.VERSION_CODES.S_V2 -> "12.1";
            case Build.VERSION_CODES.S -> "12";
            case Build.VERSION_CODES.R -> "11";
            case Build.VERSION_CODES.Q -> "10";
            case Build.VERSION_CODES.P -> "9";
            default -> Build.VERSION.RELEASE + " (unsupported)";
        };
    }
}
