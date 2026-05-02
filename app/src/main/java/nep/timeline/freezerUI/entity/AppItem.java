package nep.timeline.freezerUI.entity;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import lombok.Data;
import nep.timeline.freezerUI.utils.PackageUtils;

@Data
public class AppItem {
    // Application Info
    public String appName;
    public String packageName;
    public int userId;
    public Drawable appIcon;
    public PackageInfo packageInfo;
    // Application Settings
    public int oomLevel;
    public boolean white;
    public boolean black;
    public int backgroundLevel;
    public boolean intervalUnfreeze;
    public int intervalUnfreezeDelay;
    public boolean binderFreeze;
    public boolean backgroundPlay;
    public boolean backgroundIntent;
    public boolean notificationKeep;
    public int locationCheck;
    public boolean ignoreRecording;
    public boolean bluetoothCheck;
    public boolean ignoreBinder;
    public boolean socket;
    public boolean netReceive;
    public boolean networkCheck;
    public boolean idle;
    public int killProcCount;
    public int whiteProcCount;
    // Application Category
    public final Category category;
    // Application Frozen State
    public boolean isFrozen;
    public String frozenType;
    public String notFrozenReason;
    public long rss;
    public int applicationProcessCount;
    public int frozenProcessCount;
    // Application Info Type
    public final boolean isFrozenData;
    // Misc
    public final boolean no = false;

    private AppItem(String appName, String packageName, int userId, Drawable appIcon, PackageInfo packageInfo, int oomLevel, boolean white, boolean black, int backgroundLevel, boolean intervalUnfreeze, int intervalUnfreezeDelay, boolean binderFreeze, boolean backgroundPlay, boolean backgroundIntent, boolean notificationKeep, int locationCheck, boolean ignoreRecording, boolean bluetoothCheck, boolean ignoreBinder, boolean socket, boolean netReceive, boolean networkCheck, boolean idle, int killProcCount, int whiteProcCount) {
        this.category = PackageUtils.categoryUndefined(packageInfo.applicationInfo) ? Category.Undefined : (PackageUtils.isGame(packageInfo.applicationInfo) ? Category.Game : (PackageUtils.isAudioOrVideo(packageInfo.applicationInfo) ? Category.Music : (PackageUtils.isMap(packageInfo.applicationInfo) ? Category.Map : (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2 && PackageUtils.isAccessibility(packageInfo.applicationInfo) ? Category.Accessibility : Category.Other))));
        this.appName = appName;
        this.packageName = packageName;
        this.userId = userId;
        this.appIcon = appIcon;
        this.packageInfo = packageInfo;
        this.oomLevel = oomLevel;
        this.white = white;
        this.black = black;
        this.backgroundLevel = backgroundLevel;
        this.intervalUnfreeze = intervalUnfreeze;
        this.intervalUnfreezeDelay = intervalUnfreezeDelay;
        this.binderFreeze = binderFreeze;
        this.backgroundPlay = backgroundPlay;
        this.backgroundIntent = backgroundIntent;
        this.notificationKeep = notificationKeep;
        this.locationCheck = locationCheck;
        this.ignoreRecording = ignoreRecording;
        this.bluetoothCheck = bluetoothCheck;
        this.ignoreBinder = ignoreBinder;
        this.socket = socket;
        this.netReceive = netReceive;
        this.networkCheck = networkCheck;
        this.idle = idle;
        this.killProcCount = killProcCount;
        this.whiteProcCount = whiteProcCount;
        this.isFrozenData = false;
    }

    public AppItem(String appName, String packageName, int userId, Drawable appIcon, PackageInfo packageInfo, boolean isFrozen, String frozenType, String notFrozenReason, long rss, int applicationProcessCount, int frozenProcessCount) {
        this.category = PackageUtils.categoryUndefined(packageInfo.applicationInfo) ? Category.Undefined : (PackageUtils.isGame(packageInfo.applicationInfo) ? Category.Game : (PackageUtils.isAudioOrVideo(packageInfo.applicationInfo) ? Category.Music : (PackageUtils.isMap(packageInfo.applicationInfo) ? Category.Map : (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2 && PackageUtils.isAccessibility(packageInfo.applicationInfo) ? Category.Accessibility : Category.Other))));
        this.appName = appName;
        this.packageName = packageName;
        this.userId = userId;
        this.appIcon = appIcon;
        this.packageInfo = packageInfo;
        this.isFrozen = isFrozen;
        this.frozenType = frozenType;
        this.notFrozenReason = notFrozenReason;
        this.rss = rss;
        this.applicationProcessCount = applicationProcessCount;
        this.frozenProcessCount = frozenProcessCount;
        this.isFrozenData = true;
    }

    public static AppItem createApp(String appName, String packageName, int userId, Drawable appIcon, PackageInfo packageInfo, int oomLevel, boolean white, boolean black, int backgroundLevel, boolean intervalUnfreeze, int intervalUnfreezeDelay, boolean binderFreeze, boolean backgroundPlay, boolean backgroundIntent, boolean notificationKeep, int locationCheck, boolean ignoreRecording, boolean bluetoothCheck, boolean ignoreBinder, boolean socket, boolean netReceive, boolean networkCheck, boolean idle, int killProcCount, int whiteProcCount) {
        return new AppItem(appName, packageName, userId, appIcon, packageInfo, oomLevel, white, black, backgroundLevel, intervalUnfreeze, intervalUnfreezeDelay, binderFreeze, backgroundPlay, backgroundIntent, notificationKeep, locationCheck, ignoreRecording, bluetoothCheck, ignoreBinder, socket, netReceive, networkCheck, idle, killProcCount, whiteProcCount);
    }

    public static AppItem createFrozen(String appName, String packageName, int userId, Drawable appIcon, PackageInfo packageInfo, boolean isFrozen, String frozenType, String notFrozenReason, long rss, int applicationProcessCount, int frozenProcessCount) {
        return new AppItem(appName, packageName, userId, appIcon, packageInfo, isFrozen, frozenType, notFrozenReason, rss, applicationProcessCount, frozenProcessCount);
    }

    public String getPackageNameWithUser() {
        return PackageUtils.toKey(userId, packageName);
    }

    public enum Category {
        Game,
        Music,
        Map,
        Accessibility,
        Other,
        Undefined
    }
}
