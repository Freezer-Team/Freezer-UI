package nep.timeline.freezer.binders;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;

import java.util.List;

import rikka.parcelablelist.ParcelableListSlice;

interface PackageInterface {
    ParcelableListSlice<ResolveInfo> queryIntentActivitiesAsUser(in Intent intent, int flags, in UserHandle user);

    ParcelableListSlice<PackageInfo> getInstalledPackagesAsUser(int flags, int userId);

    PackageInfo getPackageInfoAsUser(String packageName, int flags, int userId);

    String[] getPackagesForUid(int uid);
}