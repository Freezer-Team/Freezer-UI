package nep.timeline.freezerUI.configs.checkers;

import nep.timeline.freezerUI.GlobalVars;

public class LogPrintChecker {
    public static LogMode getLogPrint() {
        return switch (GlobalVars.globalSettings.logPrintMode) {
            case 1 -> LogMode.FILE;
            case 2 -> LogMode.CLOSE;
            default -> LogMode.XPOSED;
        };
    }

    public enum LogMode {
        XPOSED,
        FILE,
        CLOSE
    }
}
