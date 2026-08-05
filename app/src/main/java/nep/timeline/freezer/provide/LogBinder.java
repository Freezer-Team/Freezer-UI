package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.RemoteException;

import nep.timeline.freezer.binders.LogInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class LogBinder extends LogInterface.Stub {
    public static LogInterface getInstance() {
        IBinder binder = BinderService.getBinder("Log");
        return LogInterface.Stub.asInterface(binder);
    }

    @Override
    public void e(String msg) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void i(String msg) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void w(String msg) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void d(String msg) throws RemoteException { throw new UnsupportedOperationException(); }
}