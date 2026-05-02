package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

import nep.timeline.freezer.binders.ApplicationInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class ApplicationBinder extends ApplicationInterface.Stub {
    public static ApplicationInterface getInstance() {
        IBinder binder = BinderService.getBinder("Application");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof ApplicationInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public List<String> getRunningApplication() throws RemoteException { throw new UnsupportedOperationException(); }
}
