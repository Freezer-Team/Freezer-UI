# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class nep.timeline.freezerUI.** {*;}

# 请务必保持下方规则
-keep class com.google.gson.Gson {*;}
-keep class com.google.gson.GsonBuilder {*;}
-keep class nep.timeline.freezer.data.** {*;}
-keep class nep.timeline.freezer.core.jni.NativeMethods {
    java.lang.String O0o0o0oo0o00oo00(java.lang.String, java.lang.String, java.lang.String, java.util.Set);
}
-keep class nep.timeline.freezer.binders.PropInterface {*;}
-keep class nep.timeline.freezer.binders.FileInterface {*;}
-keep class nep.timeline.freezer.provide.PropBinder {*;}
-keep class nep.timeline.freezer.provide.FileBinder {*;}
