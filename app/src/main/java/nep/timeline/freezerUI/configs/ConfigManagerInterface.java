package nep.timeline.freezerUI.configs;

public interface ConfigManagerInterface {
    void readConfigWithBinder();

    void saveConfigWithBinder(boolean refresh);
}
