package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.RemoteException;

import java.util.Map;

import nep.timeline.freezer.binders.FrozenCountInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class FrozenCountBinder extends FrozenCountInterface.Stub {
    public static FrozenCountInterface getInstance() {
        IBinder binder = BinderService.getBinder("FrozenCount");
        return FrozenCountInterface.Stub.asInterface(binder);
    }

    @Override
    public Map<String, String> getHourlyCounts() throws RemoteException { throw new UnsupportedOperationException(); }
}
