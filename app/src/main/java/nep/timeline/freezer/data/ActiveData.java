package nep.timeline.freezer.data;

import java.util.Set;

public class ActiveData {
    public final String username;
    public final String password;
    public final Set<String> accounts;
    public final String deviceId;

    public ActiveData(String username, String password, Set<String> accounts, String deviceId) {
        this.username = username;
        this.password = password;
        this.accounts = accounts;
        this.deviceId = deviceId;
    }
}
