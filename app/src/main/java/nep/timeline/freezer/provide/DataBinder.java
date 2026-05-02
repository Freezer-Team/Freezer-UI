package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.DataInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class DataBinder extends DataInterface.Stub {
    public static DataInterface getInstance() {
        IBinder binder = BinderService.getBinder("Data");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof DataInterface) ? new DataInterface.Stub.Proxy(binder) : (DataInterface.Stub.Proxy) localInterface;
    }

    @Override
    public String get(String name) throws RemoteException { throw new UnsupportedOperationException(); }
}
