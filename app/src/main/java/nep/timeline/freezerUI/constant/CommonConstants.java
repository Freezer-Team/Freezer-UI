package nep.timeline.freezerUI.constant;

import java.util.Set;

import nep.timeline.freezerUI.BuildConfig;
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker;

public interface CommonConstants {
    String NATIVE_PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    String SHELL = "com.android.shell";
    String ANDROID = "android";
    Set<String> systemApps = Set.of(
            "moe.shizuku.privileged.api", /* Shizuku */
            "com.google.android.gsf", /* 谷歌服务框架 */
            "com.xiaomi.xmsf", /* 小米服务框架 */
            "com.huawei.hwid", /* HMS核心 */
            "io.heckel.ntfy", /* NTFY */
            "app.revanced.android.gms", /* MicroG */
            "com.xiaomi.smarthome", /* 米家 */
            "com.mfashiongallery.emag", /* 小米画报 */
            "com.miui.miwallpaper.moon", /* 月球超级壁纸 */
            "com.miui.miwallpaper.snowmountain", /* 雪山超级壁纸 */
            "com.miui.miwallpaper.saturn", /* 土星超级壁纸 */
            "com.miui.miwallpaper.geometry", /* 几何超级壁纸 */
            "com.miui.miwallpaper.mars", /* 火星超级壁纸 */
            "com.miui.miwallpaper.earth", /* 地球超级壁纸 */
            "com.xiaomi.mibrain.speech", /* 系统语音引擎 */
            "com.miui.securitymanager", /* 手机管家 */
            "com.miui.home", /* 小米桌面 */
            "com.mi.android.globallauncher", /* POCO桌面 */
            "com.miui.gallery", /* 小米相册 */
            "com.miui.mediaeditor", /* 小米相册 - 编辑 */
            "com.android.deskclock", /* 时钟 */
            "com.android.soundrecorder", /* 录音机 */
            "com.android.calendar", /* 日历 */
            "com.miui.screenrecorder", /* 小米屏幕录制 */
            "com.miui.weather2", /* 小米天气 */
            "com.miui.notes", /* 小米笔记 */
            "com.mi.earphone", /* 小米耳机 */
            "com.mi.health", /* 小米健康 */
            "com.edith.os", /* OPPO Edith OS */
            "com.coloros.alarmclock", /* OPPO时钟 */
            "com.coloros.soundrecorder", /* OPPO录音机 */
            "com.coloros.calendar", /* OPPO日历 */
            "com.unionpay.tsmservice", /* 银联可信服务安全组件 */
            "com.finshell.wallet", /* OPPO钱包 */
            "com.coloros.translate", /* OPPO语音翻译 */
            "com.heytap.health", /* OPPO健康 */
            "com.heytap.uwbconnect", /* OPPO云 */
            "com.coloros.familyguard", /* OPPO家人守护 */
            "com.coloros.oppopods", /* OPPO耳机 */
            "com.coloros.shortcuts", /* 小布指令 */
            "com.coloros.compass2", /* OPPO指南针 */
            "com.coloros.note", /* OPPO便签 */
            "com.coloros.filemanager", /* OPPO文件管理 */
            "com.coloros.backuprestore", /* OPPO手机搬家 */
            "com.coloros.weather2", /* OPPO天气 */
            "com.color.otaassistant", /* OPPO OTA助手 */
            "com.oplus.caseflash", /* 一加CaseFlash */
            "com.oplus.riderMode", /* 一加骑手模式 */
            "andes.oplus.documentsreader", /* 一加文档 */
            "com.oplus.onet", /* 一加O+互联 */
            "com.oplus.screenrecorder", /* 一加录屏 */
            "com.oplus.games", /* 一加游戏助手 */
            "com.oplus.consumerIRApp", /* 一加红外遥控 */
            "com.oplus.melody", /* OPPO无线耳机 */
            "com.oneplus.brickmode", /* 一加禅定空间 */
            "com.oneplus.ctsprepare", /* 一加CTS准备 */
            "com.realme.link", /* 真我互联 */
            "com.realme.linkcn", /* 真我互联大陆 */
            "com.android.email", /* 邮件 */
            "com.google.android.contactkeys", /* Android System Key Verifier */
            "com.google.android.safetycore", /* Android System SafetyCore */
            "com.google.android.apps.weather", /* Pixel 天气 */
            "com.google.android.googlequicksearchbox", /* Google */
            "io.github.vvb2060.magisk", /* Magisk Alpha */
            "io.github.vvb2060.magisk.lite", /* Magisk Lite */
            "io.github.huskydg.magisk", /* Magisk Delta / Kitsune Mask */
            "com.topjohnwu.magisk", /* Magisk */
            "com.thehitman7.cygisk", /* Cygisk */
            "com.samsung.android.rampart", /* 自动拦截程序 */
            "com.samsung.android.messaging",
            "com.samsung.android.app.telephonyui",
            "com.samsung.android.dialer",
            "com.samsung.android.incallui",
            "com.samsung.android.smartcallprovider",
            "com.samsung.android.intellivoiceservice",
            "com.qti.qcc",
            "com.sec.epdg",
            "com.sec.imsservice"
    );
    Set<String> whiteApps = Set.of(
            "com.android.systemui", /* 系统界面 */
            "miui.systemui.plugin"  /* 系统界面组件 */
    );

    static boolean isImportantSystemApps(String packageName) {
        return packageName.equalsIgnoreCase(SHELL) || packageName.equalsIgnoreCase(ANDROID);
    }

    static boolean isSystemApps(String packageName) {
        return systemApps.contains(packageName);
    }

    static boolean isWhitelistApps(String packageName) {
        return whiteApps.contains(packageName) || (NATIVE_PACKAGE_NAME.equals(packageName) && SettingsConfigChecker.isSuExecute());
    }
}
