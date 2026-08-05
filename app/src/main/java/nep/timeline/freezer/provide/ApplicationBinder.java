package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import nep.timeline.freezer.binders.ApplicationInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class ApplicationBinder extends ApplicationInterface.Stub {
    public static ApplicationInterface getInstance() {
        IBinder binder = BinderService.getBinder("Application");
        return ApplicationInterface.Stub.asInterface(binder);
    }

    @Override
    public List<String> getRunningApplication() throws RemoteException { throw new UnsupportedOperationException(); }
}
