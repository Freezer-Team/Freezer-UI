package nep.timeline.freezer.data;

public class ResetData {
    public final String username;
    public final String new_password;
    public final String deviceId;

    public ResetData(String username, String new_password, String deviceId) {
        this.username = username;
        this.new_password = new_password;
        this.deviceId = deviceId;
    }
}
