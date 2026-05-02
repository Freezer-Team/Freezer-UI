package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.FrozenInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class FrozenBinder extends FrozenInterface.Stub {
    public static FrozenInterface getInstance() {
        IBinder binder = BinderService.getBinder("Frozen");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof FrozenInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public void temporaryUnfreezeWithUID(int uid, String reason) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void temporaryUnfreeze(String packageName, int userId, String reason) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void temporaryUnfreezeWithUIDAndInterval(int uid, String reason, long interval) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void temporaryUnfreezeWithInterval(String packageName, int userId, String reason, long interval) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void freezerWithUID(int uid) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void freezer(String packageName, int userId) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void thawWithUID(int uid) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public void thaw(String packageName, int userId) throws RemoteException { throw new UnsupportedOperationException(); }
}