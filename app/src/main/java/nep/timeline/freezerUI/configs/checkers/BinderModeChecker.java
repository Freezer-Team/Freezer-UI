package nep.timeline.freezerUI.configs.checkers;

import nep.timeline.freezerUI.GlobalVars;

public class BinderModeChecker {
    public static BinderMode getBinderMode() {
        return switch (GlobalVars.globalSettings.binderMode) {
            case 1 -> BinderMode.NATIVE;
            case 2 -> BinderMode.ANDROID;
            case 3 -> BinderMode.KERNEL;
            case 4 -> BinderMode.EBPF;
            case 5 -> BinderMode.DISABLE;
            default -> BinderMode.AUTO;
        };
    }

    public enum BinderMode {
        AUTO,
        NATIVE,
        ANDROID,
        KERNEL,
        EBPF,
        DISABLE
    }
}
