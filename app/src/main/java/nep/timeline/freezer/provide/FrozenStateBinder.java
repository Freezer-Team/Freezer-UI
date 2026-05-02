package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.FrozenStateInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class FrozenStateBinder extends FrozenStateInterface.Stub {
    public static FrozenStateInterface getInstance() {
        IBinder binder = BinderService.getBinder("FrozenState");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof FrozenStateInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public String isFrozen(String packageName, int userId) throws RemoteException { throw new UnsupportedOperationException(); }
}