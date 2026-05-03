package nep.timeline.freezerUI.configs.settings;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationSettings {
    // 白名单APP.
    @SerializedName("whiteApps")
    public Set<String> whiteApps = new HashSet<>(Set.of("com.tencent.mm#0", "com.tencent.mm#999"));
    // 前台检测APP.
    @SerializedName("backgroundLevelApps")
    public Map<String, Integer> backgroundLevelApps = new HashMap<>();
    // 系统黑名单APP.
    @SerializedName("blackSystemApps")
    public Set<String> blackSystemApps = new HashSet<>();
    // 白名单进程.
    @SerializedName("whiteProcessList")
    public Set<String> whiteProcessList = new HashSet<>();
    // 杀死进程.
    @SerializedName("killProcessList")
    public Set<String> killProcessList = new HashSet<>();
    // 保持连接.
    @SerializedName("socketApps")
    public Set<String> socketApps = new HashSet<>();
    // 网络解冻.
    @SerializedName("netReceiveApps")
    public Set<String> netReceiveApps = new HashSet<>();
    // 网络检测.
    @SerializedName("networkCheckApps")
    public Set<String> networkCheckApps = new HashSet<>();
    @SerializedName("idleApps")
    public Set<String> idleApps = new HashSet<>();
    @SerializedName("intervalUnfreezeApps")
    public Map<String, Integer> intervalUnfreezeApps = new HashMap<>();
    @SerializedName("binderFreezeApps")
    public Set<String> binderFreezeApps = new HashSet<>();
    @SerializedName("backgroundPlayApps")
    public Set<String> backgroundPlayApps = new HashSet<>();
    @SerializedName("backgroundIntentApps")
    public Set<String> backgroundIntentApps = new HashSet<>();
    @SerializedName("notificationKeepApps")
    public Set<String> notificationKeepApps = new HashSet<>();
    @SerializedName("locationCheckApps")
    public Map<String, Integer> locationCheckApps = new HashMap<>();
    @SerializedName("ignoreRecordingApps")
    public Set<String> ignoreRecordingApps = new HashSet<>();
    @SerializedName("oomPriorityApps")
    public Map<String, Integer> oomPriorityApps = new HashMap<>();
    @SerializedName("bluetoothCheckApps")
    public Set<String> bluetoothCheckApps = new HashSet<>();
    @SerializedName("ignoreBinderApps")
    public Set<String> ignoreBinderApps = new HashSet<>();
    @SerializedName("allowAPIInjectionApps")
    public Set<String> allowAPIInjectionApps = new HashSet<>();
}
