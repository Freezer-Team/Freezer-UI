package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.PropInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class PropBinder extends PropInterface.Stub {
    public static PropInterface getInstance() {
        IBinder binder = BinderService.getBinder("Prop");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof PropInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public String get(String prop) throws RemoteException { throw new UnsupportedOperationException(); }
}