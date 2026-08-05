package nep.timeline.freezerUI.utils;

import static nep.timeline.freezerUI.GlobalVars.TAG;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.topjohnwu.superuser.Shell;

public class EnvUtils {
    public static boolean isSystem() {
        return Process.myUid() == Process.SYSTEM_UID;
    }

    public static boolean checkRoot() {
        return Shell.getShell().isRoot();
    }

    public static Handler makeHandler(String str) {
        HandlerThread handlerThread = new HandlerThread(TAG + "-" + str);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }
}
