package nep.timeline.freezerUI.configs;

public class ConfigManager {
    private static final ConfigManagerInterface manager = new ConfigManagerJson();

    public static void readConfigWithBinder() {
        manager.readConfigWithBinder();
    }

    public static void saveConfigWithBinder(boolean refresh) {
        manager.saveConfigWithBinder(refresh);
        manager.readConfigWithBinder();
    }

    public static void saveConfigWithBinder() {
        saveConfigWithBinder(true);
    }
}
