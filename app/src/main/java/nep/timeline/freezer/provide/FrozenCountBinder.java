package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.Map;

import nep.timeline.freezer.binders.CountInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class FrozenCountBinder extends CountInterface.Stub {
    public static CountInterface getInstance() {
        IBinder binder = BinderService.getBinder("FrozenCount");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof CountInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public Map<String, String> getHourlyCounts() throws RemoteException { throw new UnsupportedOperationException(); }
}
