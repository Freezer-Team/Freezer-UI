package nep.timeline.freezerUI.configs.settings;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class GlobalSettings {
    @SerializedName("mainType")
    public int mainType;
    @SerializedName("appListIgnoreSystem")
    public boolean appListIgnoreSystem;
    @SerializedName("navigationStyle")
    public int navigationStyle;
    @SerializedName("blurUI")
    public boolean blurUI;
    @SerializedName("fullyAutomatic")
    public boolean fullyAutomatic;
    @SerializedName("preferV2Mode")
    public int preferV2Mode;
    @SerializedName("freezeMethod")
    public int freezeMethod;
    @SerializedName("logPrintMode")
    public int logPrintMode;
    @SerializedName("logLevelMode")
    public int logLevelMode;
    @SerializedName("logLanguageMode")
    public int logLanguageMode;
    @SerializedName("rotationUnfreeze")
    public boolean rotationUnfreeze;
    @SerializedName("rotationUnfreezeDelay")
    public int rotationUnfreezeDelay;
    @SerializedName("bootFreeze")
    public boolean bootFreeze;
    @SerializedName("liveUpdate")
    public boolean liveUpdate;
    @SerializedName("commandInject")
    public boolean commandInject;
    @SerializedName("allowNonHighAccuracyLocation")
    public boolean allowNonHighAccuracyLocation = true;
    @SerializedName("ignoreXposedModule")
    public boolean ignoreXposedModule = true;
    @SerializedName("notificationKeep")
    public boolean notificationKeep = true;
    @SerializedName("foregroundLastAppWhenScreenOff")
    public boolean foregroundLastAppWhenScreenOff;
    @SerializedName("binderFreezeMode")
    public int binderFreezeMode;
    @SerializedName("binderMode")
    public int binderMode;
    @SerializedName("compactMode")
    public int compactMode = 0;
    @SerializedName("compactDelay")
    public int compactDelay = 8;
    @SerializedName("ignorePushBroadcasts")
    public boolean ignorePushBroadcasts;
    //@SerializedName("safetyFrozen")
    //public boolean safetyFrozen;
    @SerializedName("getProviderProcessingMode")
    public int getProviderProcessingMode;
    @SerializedName("useDoze")
    public boolean useDoze;
    @SerializedName("fcmfix")
    public boolean fcmfix;
    @SerializedName("blockAutoClear")
    public boolean blockAutoClear;
    @SerializedName("relaunchOnNotificationClick")
    public boolean relaunchOnNotificationClick;
    @SerializedName("checkForegroundService")
    public boolean checkForegroundService;
    @SerializedName("disableClearTask")
    public boolean disableClearTask;
    @SerializedName("memoryCompact")
    public boolean memoryCompact;
    @SerializedName("memoryManagementMode")
    public int memoryManagementMode;
    @SerializedName("keepAllProcessAlive")
    public boolean keepAllProcessAlive;
    @SerializedName("memoryThreshold")
    public int memoryThreshold = 75;
    @SerializedName("memoryReclaimThreshold")
    public int memoryReclaimThreshold = 60;
    @SerializedName("memoryThresholdWhitelistApp")
    public Set<String> memoryThresholdWhitelistApp = new HashSet<>(Set.of("com.tencent.mm", "com.tencent.mobileqq"));
    @SerializedName("batteryOptControlMode")
    public int batteryOptControlMode;
    @SerializedName("suExecute")
    public boolean suExecute;
    //@SerializedName("disableForceTotalFreeze")
    //public boolean disableForceTotalFreeze;
    @SerializedName("backgroundIntent")
    public boolean backgroundIntent;
    //@SerializedName("debugMode")
    //public boolean debugMode;
    @SerializedName("computeOOM")
    public boolean computeOOM;
    @SerializedName("compatibleMode")
    public boolean compatibleMode = true;
    @SerializedName("disablePowerSaveMode")
    public boolean disablePowerSaveMode;
    @SerializedName("extremeStandbyMode")
    public boolean extremeStandbyMode;
    @SerializedName("superStandbyMode")
    public boolean superStandbyMode = true;
    //@SerializedName("useOldIntent")
    //public boolean useOldIntent;
    //@SerializedName("ignoreSync")
    //public boolean ignoreSync;
    @SerializedName("dumpThaw")
    public boolean dumpThaw;
    @SerializedName("freezeInterval")
    public long freezeInterval = 3000;
    @SerializedName("temporaryInterval")
    public long temporaryInterval = 3000;
    @SerializedName("binderInterval")
    public long binderInterval = 3000;
    @SerializedName("flymeInterval")
    public long flymeInterval = 5000;
    //@SerializedName("skipBroadcast")
    //public boolean skipBroadcast;
    @SerializedName("batteryOptIgnoreProMode")
    public boolean batteryOptIgnoreProMode;
    @SerializedName("proMode")
    public boolean proMode;
    @SerializedName("liteMode")
    public boolean liteMode = true;
    @SerializedName("netlinkUnit")
    public int netlinkUnit;
    @SerializedName("searchNetlinkUnit")
    public boolean searchNetlinkUnit = true;
    @SerializedName("allowBroadcastIntents")
    public Set<String> allowBroadcastIntents = new HashSet<>();
}
