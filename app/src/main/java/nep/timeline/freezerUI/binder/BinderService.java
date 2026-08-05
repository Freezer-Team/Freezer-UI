package nep.timeline.freezerUI.binder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import nep.timeline.freezerUI.GlobalVars;

public class BinderService {
    public static final Map<String, IBinder> binders = new HashMap<>();

    public static IBinder getBinder(String name) {
        return binders.get(name);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public static void register(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter(GlobalVars.TAG + "-Binder"));
        if (registerReceiver == null || registerReceiver.getExtras() == null)
            return;

        Bundle extras = registerReceiver.getExtras();
        synchronized (binders) {
            for (String name : extras.keySet())
                binders.put(name, extras.getBinder(name));
        }
    }
}
