package nep.timeline.freezer.provide;

import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import nep.timeline.freezer.binders.PackageInterface;
import nep.timeline.freezerUI.binder.BinderService;
import rikka.parcelablelist.ParcelableListSlice;

public class PackageBinder extends PackageInterface.Stub {
    public static PackageInterface getInstance() {
        IBinder binder = BinderService.getBinder("Package");
        if (binder == null)
            return null;
        IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
        return !(localInterface instanceof PackageInterface) ? new Proxy(binder) : (Proxy) localInterface;
    }

    @Override
    public ParcelableListSlice<PackageInfo> getInstalledPackagesAsUser(int flags, int userId) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public PackageInfo getPackageInfoAsUser(String packageName, int flags, int userId) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public String[] getPackagesForUid(int uid) throws RemoteException { throw new UnsupportedOperationException(); }
}
