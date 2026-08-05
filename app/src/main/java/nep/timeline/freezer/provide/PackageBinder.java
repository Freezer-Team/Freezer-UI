package nep.timeline.freezer.provide;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;

import nep.timeline.freezer.binders.PackageInterface;
import nep.timeline.freezerUI.binder.BinderService;
import rikka.parcelablelist.ParcelableListSlice;

public class PackageBinder extends PackageInterface.Stub {
    public static PackageInterface getInstance() {
        IBinder binder = BinderService.getBinder("Package");
        return PackageBinder.Stub.asInterface(binder);
    }

    @Override
    public ParcelableListSlice<ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int flags, UserHandle user) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public ParcelableListSlice<PackageInfo> getInstalledPackagesAsUser(int flags, int userId) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public PackageInfo getPackageInfoAsUser(String packageName, int flags, int userId) throws RemoteException { throw new UnsupportedOperationException(); }

    @Override
    public String[] getPackagesForUid(int uid) throws RemoteException { throw new UnsupportedOperationException(); }
}
