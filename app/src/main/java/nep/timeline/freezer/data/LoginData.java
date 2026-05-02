package nep.timeline.freezer.data;

public class LoginData {
    public final String username;
    public final String password;
    public final String deviceId;

    public LoginData(String username, String password, String deviceId) {
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
    }
}
