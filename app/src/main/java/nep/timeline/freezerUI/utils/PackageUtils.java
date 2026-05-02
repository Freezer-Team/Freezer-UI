package nep.timeline.freezerUI.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import nep.timeline.freezerUI.configs.checkers.AppConfigs;
import nep.timeline.freezerUI.configs.checkers.BatteryOptModeChecker;
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker;
import nep.timeline.freezerUI.GlobalVars;
import nep.timeline.freezer.binders.PackageInterface;
import nep.timeline.freezerUI.constant.CommonConstants;
import nep.timeline.freezerUI.entity.AppInfo;
import nep.timeline.freezerUI.entity.AppItem;
import nep.timeline.freezer.provide.ApplicationBinder;
import nep.timeline.freezer.provide.FrozenStateBinder;
import nep.timeline.freezer.provide.PackageBinder;
import nep.timeline.freezerUI.ui.utils.AppContext;

public class PackageUtils {
    private static SigningInfo SYSTEM_SIGNATURE;

    public static SigningInfo getSystemSignature(Context context) {
        if (SYSTEM_SIGNATURE == null) {
            PackageManager manager = context.getPackageManager();
            if (manager == null)
                return null;

            try {
                SYSTEM_SIGNATURE = manager.getPackageInfo(CommonConstants.SHELL, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return SYSTEM_SIGNATURE;
    }

    public static PackageInfo getPackageInfoAsUser(PackageManager packageManager, String packageName, int flags, int userId)
    {
        try {
            PackageInterface packageInterface = PackageBinder.getInstance();
            if (packageInterface != null)
                return packageInterface.getPackageInfoAsUser(packageName, flags, userId);
            else
                return packageManager.getPackageInfo(packageName, flags);
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static boolean isSystemSignature(Context context, PackageInfo packageInfo) {
        if (context == null)
            return false;

        return PackageUtils.isSameSigner(getSystemSignature(context), packageInfo.signingInfo);
    }

    public static boolean isSameSigner(SigningInfo signingInfo, SigningInfo signingInfo2) {
        if (signingInfo == null || signingInfo2 == null)
            return false;

        Signature[] apkContentsSigners = signingInfo.getApkContentsSigners();
        Signature[] apkContentsSigners2 = signingInfo2.getApkContentsSigners();

        if (apkContentsSigners == null || apkContentsSigners.length == 0 || apkContentsSigners2 == null || apkContentsSigners2.length == 0)
            return false;

        return apkContentsSigners[0].toCharsString().equals(apkContentsSigners2[0].toCharsString());
    }

    public static List<PackageInfo> getInstalledPackagesAsUser(PackageManager packageManager, int flags, int userId)
    {
        try {
            PackageInterface packageInterface = PackageBinder.getInstance();
            if (packageInterface != null)
                return packageInterface.getInstalledPackagesAsUser(flags, userId).getList();
            else
                return packageManager.getInstalledPackages(flags);
        } catch (Throwable ignored) {

        }

        return null;
    }

    public static List<AppItem> getFrozenApplication(Context context) throws RemoteException {
        List<AppItem> appItemList = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();

        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);

        List<String> runningApplication = ApplicationBinder.getInstance().getRunningApplication();

        for (UserHandle userHandle : userManager.getUserProfiles()) {
            int userId = userHandle.hashCode();

            List<PackageInfo> installedPackages = getInstalledPackagesAsUser(packageManager, PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA | PackageManager.GET_SIGNING_CERTIFICATES, userId);
            if (installedPackages == null)
                return appItemList;

            for (PackageInfo installedPackage : installedPackages) {
                ApplicationInfo applicationInfo = installedPackage.applicationInfo;
                if (applicationInfo == null || !applicationInfo.enabled)
                    continue;

                String packageName = installedPackage.packageName;

                ActivityInfo[] activities = installedPackage.activities;
                boolean hasActivity = activities != null && activities.length > 0;

                if (!hasActivity || !runningApplication.contains(packageName + ":" + userId + ":" + applicationInfo.uid))
                    continue;

                if (GlobalVars.globalSettings.appListIgnoreSystem) {
                    if (CommonConstants.isWhitelistApps(packageName) || CommonConstants.isImportantSystemApps(packageName))
                        continue;

                    boolean isSystem = PackageUtils.isSystemUIChecker(context, installedPackage);
                    if (isSystem && !AppConfigs.isBlackSystemApp(packageName, userId))
                        continue;

                    if (!isSystem && (AppConfigs.isWhiteApp(packageName, userId) || (isXposed(applicationInfo) && SettingsConfigChecker.isIgnoreXposedModule())))
                        continue;
                }

                String appName = applicationInfo.loadLabel(packageManager).toString().trim();

                Drawable appIcon = packageManager.getUserBadgedIcon(applicationInfo.loadUnbadgedIcon(packageManager), userHandle);

                String frozenData = FrozenStateBinder.getInstance().isFrozen(packageName, userId);
                boolean frozenError = frozenData != null && frozenData.startsWith("FROZEN_ERROR");
                boolean isFrozen = frozenData != null && !frozenData.startsWith("NOT_FROZEN") && !frozenError;
                String frozenType = isFrozen ? StringUtils.getSubString(frozenData, "", "(") : null;
                String notFrozenReason = isFrozen ? null : (frozenData != null ? StringUtils.getSubString(frozenData, "[", "]") : null);
                int applicationProcessCount = isFrozen ? StringUtils.StringToInteger(StringUtils.getSubString(frozenData, "/", ")")) : (frozenData == null ? 0 : StringUtils.StringToInteger(StringUtils.getSubString(frozenData, "PROCESS_COUNT [", "]")));
                if (applicationProcessCount <= 0)
                    continue;

                int rss = StringUtils.StringToInteger(StringUtils.getSubString(frozenData, "RSS [", "]"));

                int frozenProcessCount = frozenError ? -1 : (isFrozen ? StringUtils.StringToInteger(StringUtils.getSubString(frozenData, "(", "/")) : 0);

                AppItem appItem = AppItem.createFrozen(appName, packageName, userId, appIcon, installedPackage, isFrozen, frozenType, notFrozenReason, rss, applicationProcessCount, frozenProcessCount);
                appItemList.add(appItem);
            }
        }

        if (!appItemList.isEmpty())
            appItemList = appItemList.stream().sorted(Comparator.comparing(AppItem::isFrozen).reversed()).collect(Collectors.toList());

        return appItemList;
    }

    public static List<AppItem> filter(int type) {
        return filter(type, "");
    }

    public static List<AppItem> filter(int type, String text) {
        Context context = AppContext.context;

        Set<String> blackAppSet = GlobalVars.applicationSettings.blackSystemApps;
        Set<String> whiteAppSet = GlobalVars.applicationSettings.whiteApps;
        Map<String, Integer> backgroundLevelAppMap = GlobalVars.applicationSettings.backgroundLevelApps;
        Set<String> socketAppSet = GlobalVars.applicationSettings.socketApps;
        Set<String> netReceiveAppSet = GlobalVars.applicationSettings.netReceiveApps;
        Set<String> networkCheckAppSet = GlobalVars.applicationSettings.networkCheckApps;
        Set<String> idleAppSet = GlobalVars.applicationSettings.idleApps;
        Set<String> killProcessSet = GlobalVars.applicationSettings.killProcessList;
        Set<String> whiteProcessSet = GlobalVars.applicationSettings.whiteProcessList;
        Map<String, Integer> intervalUnfreezeAppSet = GlobalVars.applicationSettings.intervalUnfreezeApps;
        Set<String> backgroundPlayAppSet = GlobalVars.applicationSettings.backgroundPlayApps;
        Set<String> backgroundIntentAppSet = GlobalVars.applicationSettings.backgroundIntentApps;
        Set<String> notificationKeepAppSet = GlobalVars.applicationSettings.notificationKeepApps;
        Map<String, Integer> locationCheckAppMap = GlobalVars.applicationSettings.locationCheckApps;
        Set<String> ignoreRecordingAppSet = GlobalVars.applicationSettings.ignoreRecordingApps;
        Set<String> bluetoothCheckAppSet = GlobalVars.applicationSettings.bluetoothCheckApps;
        Set<String> ignoreBinderAppSet = GlobalVars.applicationSettings.ignoreBinderApps;
        Set<String> binderFreezeAppSet = GlobalVars.applicationSettings.binderFreezeApps;
        Map<String, Integer> oomAppMap = GlobalVars.applicationSettings.oomPriorityApps;
        Map<String, List<String>> killProcessMap = killProcessSet.stream().collect(Collectors.groupingBy(proc -> proc.contains(":") ? proc.split(":")[0] + "#" + proc.split("#")[1] : proc));
        Map<String, List<String>> whiteProcessMap = whiteProcessSet.stream().collect(Collectors.groupingBy(proc -> proc.contains(":") ? proc.split(":")[0] + "#" + proc.split("#")[1] : proc));
        List<AppItem> appItemList = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();

        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);

        for (UserHandle userHandle : userManager.getUserProfiles()) {
            int userId = userHandle.hashCode();

            List<PackageInfo> installedPackages = getInstalledPackagesAsUser(packageManager, PackageManager.GET_META_DATA | PackageManager.GET_SERVICES | PackageManager.GET_SIGNING_CERTIFICATES, userId);
            if (installedPackages == null)
                return appItemList;

            for (PackageInfo installedPackage : installedPackages) {
                ApplicationInfo applicationInfo = installedPackage.applicationInfo;
                if (applicationInfo == null || !applicationInfo.enabled || isImportantSystemApp(applicationInfo))
                    continue;

                if (SettingsConfigChecker.isIgnoreXposedModule() && isXposed(applicationInfo))
                    continue;

                String packageName = installedPackage.packageName;
                if (CommonConstants.isWhitelistApps(packageName) || CommonConstants.isImportantSystemApps(packageName))
                    continue;

                boolean isSystem = PackageUtils.isSystemUIChecker(context, installedPackage);
                if ((type == 1 && isSystem) || (type == 2 && !isSystem))
                    continue;

                String appName = applicationInfo.loadLabel(packageManager).toString().trim();
                if (text != null && !text.isEmpty() && !appName.toLowerCase().contains(text.toLowerCase()) && !packageName.toLowerCase().contains(text.toLowerCase()))
                    continue;

                Drawable appIcon = packageManager.getUserBadgedIcon(applicationInfo.loadUnbadgedIcon(packageManager), userHandle);

                String key = toConfigKey(userId, packageName, true);
                boolean isWhite = whiteAppSet.contains(key);
                boolean isBlack = blackAppSet.contains(key);
                boolean isSocket = socketAppSet.contains(key);
                boolean isNetReceive = netReceiveAppSet.contains(key);
                boolean isNetworkCheck = networkCheckAppSet.contains(key);
                boolean isIdle = idleAppSet.contains(key);
                boolean isIntervalUnfreeze = intervalUnfreezeAppSet.containsKey(key);
                int intervalUnfreezeDelay = isIntervalUnfreeze ? Objects.requireNonNullElse(intervalUnfreezeAppSet.get(key), 0) : 0;
                boolean isBinder = binderFreezeAppSet.contains(key);
                boolean isBackgroundIntent = backgroundIntentAppSet.contains(key);
                boolean isBackgroundPlay = backgroundPlayAppSet.contains(key);
                boolean isNotificationKeep = notificationKeepAppSet.contains(key);
                boolean isIgnoreRecording = ignoreRecordingAppSet.contains(key);
                boolean isBluetoothCheck = bluetoothCheckAppSet.contains(key);
                boolean isIgnoreBinder = ignoreBinderAppSet.contains(key);

                int oom = 0;
                if (oomAppMap.containsKey(key)) {
                    Integer priority = oomAppMap.get(key);
                    oom = Objects.requireNonNullElse(priority, 0);
                }

                int backgroundLevel = 0;
                if (backgroundLevelAppMap.containsKey(key)) {
                    Integer level = backgroundLevelAppMap.get(key);
                    backgroundLevel = Objects.requireNonNullElse(level, 0);
                }

                int locationCheck = 0;
                if (locationCheckAppMap.containsKey(key)) {
                    Integer level = locationCheckAppMap.get(key);
                    locationCheck = Objects.requireNonNullElse(level, 0);
                }

                int killProcCount = killProcessMap.computeIfAbsent(key, k -> new ArrayList<>()).size();
                int whiteProcCount = whiteProcessMap.computeIfAbsent(key, k -> new ArrayList<>()).size();
                AppItem appItem = AppItem.createApp(appName, packageName, userId, appIcon, installedPackage, oom, isWhite, isBlack, backgroundLevel, isIntervalUnfreeze, intervalUnfreezeDelay, isBinder, isBackgroundPlay, isBackgroundIntent, isNotificationKeep, locationCheck, isIgnoreRecording, isBluetoothCheck, isIgnoreBinder, isSocket, isNetReceive, isNetworkCheck, isIdle, killProcCount, whiteProcCount);
                appItemList.add(appItem);
            }
        }

        if (!appItemList.isEmpty()) {
            boolean isGlobalBinderFreeze = SettingsConfigChecker.isBinderFreeze();
            boolean isGlobalBackgroundIntent = SettingsConfigChecker.isBackgroundIntent();
            boolean isProMode = SettingsConfigChecker.isProMode();
            boolean isLiteMode = SettingsConfigChecker.isLiteMode();
            
            Comparator<AppItem> comparator = SettingsConfigChecker.isFullyAutomatic() ? Comparator.comparing(AppItem::isNo)
                    .thenComparing(item -> !isSystemUIChecker(context, item.getPackageInfo()) && item.isWhite())
                    .thenComparing(item -> isSystemUIChecker(context, item.getPackageInfo()) && item.isBlack())
                    .thenComparing(AppItem::isBluetoothCheck)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Game)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Music)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Map)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Accessibility)
                    .thenComparing(item -> item.getCategory() != AppItem.Category.Undefined)
                    .thenComparing(item -> item.getUserId() == getUserId(Process.myUid()))
                    : Comparator.comparing(AppItem::isNo)
                    .thenComparing(item -> !isSystemUIChecker(context, item.getPackageInfo()) && item.isWhite())
                    .thenComparing(item -> isSystemUIChecker(context, item.getPackageInfo()) && item.isBlack())
                    .thenComparing(item -> item.getBackgroundLevel() != 0 && isProMode)
                    .thenComparing(item -> item.isIntervalUnfreeze() && isProMode)
                    .thenComparing(item -> item.isBackgroundPlay() && isProMode)
                    .thenComparing(item -> item.isNotificationKeep() && !isLiteMode)
                    .thenComparing(AppItem::isBluetoothCheck)
                    .thenComparing(item -> item.isNetworkCheck() && !isLiteMode)
                    .thenComparing(item -> item.getLocationCheck() != 0 && isProMode)
                    .thenComparing(item -> item.isIgnoreRecording() && isProMode)
                    .thenComparing(AppItem::isIgnoreBinder)
                    .thenComparing(AppItem::isSocket)
                    .thenComparing(item -> item.isIdle() && (BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.AUTO || BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.FULL))
                    .thenComparing(item -> item.getOomLevel() != 0 && isProMode)
                    .thenComparing(item -> isLiteMode ? 0 : item.getWhiteProcCount())
                    .thenComparing(item -> isLiteMode ? 0 : item.getKillProcCount())
                    .thenComparing(item -> !isGlobalBinderFreeze && item.isBinderFreeze())
                    .thenComparing(item -> !isGlobalBackgroundIntent && item.isBackgroundIntent())
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Game)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Music)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Map)
                    .thenComparing(item -> item.getCategory() == AppItem.Category.Accessibility)
                    .thenComparing(item -> item.getCategory() != AppItem.Category.Undefined)
                    .thenComparing(item -> item.getUserId() == getUserId(Process.myUid()));

            appItemList = appItemList.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        }
        return appItemList;
    }

    public static AppInfo getProcessSet(Context context, String packageName, String userIdStr) {
        AppInfo appInfo = new AppInfo();
        try {
            int userId = Integer.parseInt(userIdStr);
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = getPackageInfoAsUser(packageManager, packageName, PackageManager.GET_SIGNING_CERTIFICATES |
                    PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES |
                    PackageManager.GET_RECEIVERS |
                    PackageManager.GET_PROVIDERS, userId);
            if (packageInfo == null)
                return null;
            appInfo.setPackageInfo(packageInfo);
            Set<String> processSet = new LinkedHashSet<>();
            List<ComponentInfo> componentInfoList = new ArrayList<>();
            if (packageInfo.activities != null) {
                componentInfoList.addAll(Arrays.asList(packageInfo.activities));
            }
            if (packageInfo.services != null) {
                componentInfoList.addAll(Arrays.asList(packageInfo.services));
            }
            if (packageInfo.receivers != null) {
                componentInfoList.addAll(Arrays.asList(packageInfo.receivers));
            }
            if (packageInfo.providers != null) {
                componentInfoList.addAll(Arrays.asList(packageInfo.providers));
            }
            for (ComponentInfo componentInfo : componentInfoList) {
                String processName = componentInfo.processName;
                if (processName != null) {
                    processSet.add(absoluteProcessName(packageName, processName));
                }
            }
            String key = toConfigKey(userId, packageName, true);
            appInfo.setProcessSet(processSet);
            boolean isSystem = PackageUtils.isSystemUIChecker(context, packageInfo);
            appInfo.setSystem(isSystem);
            if (isSystem) {
                Set<String> blackSystemAppConfig = GlobalVars.applicationSettings.blackSystemApps;
                appInfo.setBlack(blackSystemAppConfig.contains(key));
            }
            Map<String, Integer> oomPriorityAppMap = GlobalVars.applicationSettings.oomPriorityApps;
            Set<String> whiteAppConfig = GlobalVars.applicationSettings.whiteApps;
            Map<String, Integer> backgroundLevelAppMap = GlobalVars.applicationSettings.backgroundLevelApps;
            Set<String> socketAppConfig = GlobalVars.applicationSettings.socketApps;
            Set<String> netReceiveAppSet = GlobalVars.applicationSettings.netReceiveApps;
            Set<String> networkCheckAppSet = GlobalVars.applicationSettings.networkCheckApps;
            Set<String> idleAppConfig = GlobalVars.applicationSettings.idleApps;
            Set<String> backgroundPlayAppSet = GlobalVars.applicationSettings.backgroundPlayApps;
            Map<String, Integer> intervalUnfreezeAppSet = GlobalVars.applicationSettings.intervalUnfreezeApps;
            Set<String> backgroundIntentAppSet = GlobalVars.applicationSettings.backgroundIntentApps;
            Set<String> notificationKeepAppSet = GlobalVars.applicationSettings.notificationKeepApps;
            Map<String, Integer> locationCheckAppMap = GlobalVars.applicationSettings.locationCheckApps;
            Set<String> ignoreRecordingAppSet = GlobalVars.applicationSettings.ignoreRecordingApps;
            Set<String> bluetoothCheckAppSet = GlobalVars.applicationSettings.bluetoothCheckApps;
            Set<String> ignoreBinderAppSet = GlobalVars.applicationSettings.ignoreBinderApps;
            Set<String> binderFreezeAppSet = GlobalVars.applicationSettings.binderFreezeApps;

            int oom = 0;
            if (oomPriorityAppMap.containsKey(key)) {
                Integer priority = oomPriorityAppMap.get(key);
                oom = Objects.requireNonNullElse(priority, 0);
            }
            appInfo.setOomLevel(oom);

            int backgroundLevel = 0;
            if (backgroundLevelAppMap.containsKey(key)) {
                Integer level = backgroundLevelAppMap.get(key);
                backgroundLevel = Objects.requireNonNullElse(level, 0);
            }
            appInfo.setBackgroundLevel(backgroundLevel);

            int locationCheck = 0;
            if (locationCheckAppMap.containsKey(key)) {
                Integer level = locationCheckAppMap.get(key);
                locationCheck = Objects.requireNonNullElse(level, 0);
            }
            appInfo.setLocationCheck(locationCheck);
            appInfo.setBackgroundPlay(backgroundPlayAppSet.contains(key));
            appInfo.setWhite(whiteAppConfig.contains(key));
            appInfo.setSocket(socketAppConfig.contains(key));
            appInfo.setNetReceive(netReceiveAppSet.contains(key));
            appInfo.setNetworkCheck(networkCheckAppSet.contains(key));
            appInfo.setIdle(idleAppConfig.contains(key));
            boolean intervalUnfreeze = intervalUnfreezeAppSet.containsKey(key);
            appInfo.setIntervalUnfreeze(intervalUnfreeze);
            if (intervalUnfreeze)
                appInfo.setIntervalUnfreezeDelay(Objects.requireNonNullElse(intervalUnfreezeAppSet.get(key), 0));
            appInfo.setBinderFreeze(binderFreezeAppSet.contains(key));
            appInfo.setBackgroundIntent(backgroundIntentAppSet.contains(key));
            appInfo.setNotificationKeep(notificationKeepAppSet.contains(key));
            appInfo.setIgnoreRecording(ignoreRecordingAppSet.contains(key));
            appInfo.setBluetoothCheck(bluetoothCheckAppSet.contains(key));
            appInfo.setIgnoreBinder(ignoreBinderAppSet.contains(key));
            Set<String> whiteProcessConfig = GlobalVars.applicationSettings.whiteProcessList;
            appInfo.setWhiteProcessSet(whiteProcessConfig);
            Set<String> killProcessConfig = GlobalVars.applicationSettings.killProcessList;
            appInfo.setKillProcessSet(killProcessConfig);
        } catch (NullPointerException ignored) {
        }
        return appInfo;
    }

    /**
     * 绝对进程名
     *
     * @param packageName 包名
     * @param processName 进程名
     */
    public static String absoluteProcessName(String packageName, String processName) {
        // 相对进程名
        if (processName.startsWith(".")) {
            // 拼成绝对进程名
            return packageName + ":" + processName.substring(1);
        } else {
            // 是绝对进程直接返回
            return processName;
        }
    }

    /**
     * 转换成Key
     *
     * @param userId 用户ID
     * @param packageNameOrProcessName 包/进程名
     */
    public static String toKey(int userId, String packageNameOrProcessName) {
        return toKey(userId, packageNameOrProcessName, false);
    }

    public static String toKey(int userId, String packageNameOrProcessName, boolean keepUserId) {
        if (userId == 0 && !keepUserId)
            return packageNameOrProcessName;
        return packageNameOrProcessName + ":" + userId;
    }

    /**
     * 转换成配置文件Key
     *
     * @param userId 用户ID
     * @param packageNameOrProcessName 包/进程名
     */
    public static String toConfigKey(int userId, String packageNameOrProcessName, boolean keepUserId) {
        if (userId == 0 && !keepUserId)
            return packageNameOrProcessName;
        return packageNameOrProcessName + "#" + userId;
    }

    public static String toConfigKey(String userId, String packageNameOrProcessName, boolean keepUserId) {
        if (Integer.parseInt(userId) == 0 && !keepUserId)
            return packageNameOrProcessName;
        return packageNameOrProcessName + "#" + userId;
    }

    public static int getUserId(int uid) {
        return UserHandle.getUserHandleForUid(uid).hashCode();
    }

    public static boolean isSystemApp(ApplicationInfo applicationInfo) {
        return applicationInfo == null || (applicationInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0;
    }

    public static boolean isImportantSystemApp(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return true;
        return applicationInfo.uid < Process.FIRST_APPLICATION_UID;
    }

    private static ZipFile getModernModuleApk(ApplicationInfo info) {
        String[] apks;
        if (info.splitSourceDirs != null) {
            apks = Arrays.copyOf(info.splitSourceDirs, info.splitSourceDirs.length + 1);
            apks[info.splitSourceDirs.length] = info.sourceDir;
        } else apks = new String[]{info.sourceDir};
        ZipFile zip = null;
        for (var apk : apks) {
            try {
                zip = new ZipFile(apk);
                if (zip.getEntry("META-INF/xposed/java_init.list") != null) {
                    return zip;
                }
                zip.close();
                zip = null;
            } catch (IOException ignored) {
            }
        }
        return zip;
    }

    public static boolean isXposed(ApplicationInfo applicationInfo) {
        if (applicationInfo == null || CommonConstants.NATIVE_PACKAGE_NAME.equals(applicationInfo.packageName))
            return false;
        if (getModernModuleApk(applicationInfo) != null)
            return true;
        Bundle metaData = applicationInfo.metaData;
        if (metaData == null)
            return false;
        return metaData.containsKey("xposedminversion");
    }

    public static boolean categoryUndefined(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return false;
        return applicationInfo.category == ApplicationInfo.CATEGORY_UNDEFINED && !isGame(applicationInfo);
    }

    public static boolean isGame(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return false;
        return applicationInfo.category == ApplicationInfo.CATEGORY_GAME || (applicationInfo.flags & ApplicationInfo.FLAG_IS_GAME) != 0;
    }

    public static boolean isAudioOrVideo(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return false;
        return applicationInfo.category == ApplicationInfo.CATEGORY_AUDIO || applicationInfo.category == ApplicationInfo.CATEGORY_VIDEO;
    }

    public static boolean isMap(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return false;
        return applicationInfo.category == ApplicationInfo.CATEGORY_MAPS;
    }

    public static boolean isAccessibility(ApplicationInfo applicationInfo) {
        if (applicationInfo == null)
            return false;
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || applicationInfo.category == ApplicationInfo.CATEGORY_ACCESSIBILITY;
    }

    public static boolean isSystemUIChecker(Context context, PackageInfo packageInfo) {
        if (context == null || packageInfo == null || packageInfo.applicationInfo == null)
            return false;

        if (isSystemApp(packageInfo.applicationInfo) || CommonConstants.isSystemApps(packageInfo.applicationInfo.packageName))
            return true;

        try {
            return isSystemSignature(context, packageInfo);
        } catch (Throwable throwable) {
            return false;
        }
    }

    public static String[] getPackagesForUidBinder(Context context, int uid) {
        try {
            return PackageBinder.getInstance().getPackagesForUid(uid);
        } catch (Throwable ignored) {
        }

        if (context == null)
            return null;

        PackageManager packageManager = context.getPackageManager();
        return packageManager.getPackagesForUid(uid);
    }
}