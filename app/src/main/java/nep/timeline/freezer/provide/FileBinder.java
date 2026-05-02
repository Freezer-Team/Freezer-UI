package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.FileInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class FileBinder extends FileInterface.Stub {
    public static FileInterface getInstance() {
        IBinder binder = BinderService.getBinder("File");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof FileInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public boolean fileIsExists(String path) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public boolean makeDir(String name) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public String readString(String name) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public boolean writeString(String name, String value) throws RemoteException { throw new UnsupportedOperationException(); }
}