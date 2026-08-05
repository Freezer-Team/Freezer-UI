package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.RemoteException;

import nep.timeline.freezer.binders.DataInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class DataBinder extends DataInterface.Stub {
    public static DataInterface getInstance() {
        IBinder binder = BinderService.getBinder("Data");
        return DataInterface.Stub.asInterface(binder);
    }

    @Override
    public String get(String name) throws RemoteException { throw new UnsupportedOperationException(); }
}
