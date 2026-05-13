package nep.timeline.freezer.binders;

public interface PackageInterface extends android.os.IInterface
{
    /** Default implementation for PackageInterface. */
    public static class Default implements nep.timeline.freezer.binders.PackageInterface
    {
        @Override public rikka.parcelablelist.ParcelableListSlice<android.content.pm.PackageInfo> getInstalledPackagesAsUser(int flags, int userId) throws android.os.RemoteException
        {
            return null;
        }
        @Override public android.content.pm.PackageInfo getPackageInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
        {
            return null;
        }
        @Override public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException
        {
            return null;
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements nep.timeline.freezer.binders.PackageInterface
    {
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.PackageInterface interface,
         * generating a proxy if needed.
         */
        public static nep.timeline.freezer.binders.PackageInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof nep.timeline.freezer.binders.PackageInterface))) {
                return ((nep.timeline.freezer.binders.PackageInterface)iin);
            }
            return new nep.timeline.freezer.binders.PackageInterface.Stub.Proxy(obj);
        }
        @Override public android.os.IBinder asBinder()
        {
            return this;
        }
        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
        {
            java.lang.String descriptor = DESCRIPTOR;
            if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
                data.enforceInterface(descriptor);
            }
            switch (code)
            {
                case INTERFACE_TRANSACTION:
                {
                    reply.writeString(descriptor);
                    return true;
                }
            }
            switch (code)
            {
                case TRANSACTION_getInstalledPackagesAsUser:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    int _arg1;
                    _arg1 = data.readInt();
                    rikka.parcelablelist.ParcelableListSlice<android.content.pm.PackageInfo> _result = this.getInstalledPackagesAsUser(_arg0, _arg1);
                    reply.writeNoException();
                    _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    break;
                }
                case TRANSACTION_getPackageInfoAsUser:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    int _arg2;
                    _arg2 = data.readInt();
                    android.content.pm.PackageInfo _result = this.getPackageInfoAsUser(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    break;
                }
                case TRANSACTION_getPackagesForUid:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    java.lang.String[] _result = this.getPackagesForUid(_arg0);
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    break;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }
        protected static class Proxy implements nep.timeline.freezer.binders.PackageInterface
        {
            private android.os.IBinder mRemote;
            public Proxy(android.os.IBinder remote)
            {
                mRemote = remote;
            }
            @Override public android.os.IBinder asBinder()
            {
                return mRemote;
            }
            public java.lang.String getInterfaceDescriptor()
            {
                return DESCRIPTOR;
            }
            @Override public rikka.parcelablelist.ParcelableListSlice<android.content.pm.PackageInfo> getInstalledPackagesAsUser(int flags, int userId) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                rikka.parcelablelist.ParcelableListSlice<android.content.pm.PackageInfo> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPackagesAsUser, _data, _reply, 0);
                    _reply.readException();
                    _result = _Parcel.readTypedObject(_reply, rikka.parcelablelist.ParcelableListSlice.CREATOR);
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public android.content.pm.PackageInfo getPackageInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.content.pm.PackageInfo _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageInfoAsUser, _data, _reply, 0);
                    _reply.readException();
                    _result = _Parcel.readTypedObject(_reply, android.content.pm.PackageInfo.CREATOR);
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String[] _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagesForUid, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createStringArray();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_getInstalledPackagesAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getPackageInfoAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_getPackagesForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    }
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.PackageInterface";
    public rikka.parcelablelist.ParcelableListSlice<android.content.pm.PackageInfo> getInstalledPackagesAsUser(int flags, int userId) throws android.os.RemoteException;
    public android.content.pm.PackageInfo getPackageInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;
    public java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException;
    /** @hide */
    static class _Parcel {
        static private <T> T readTypedObject(
                android.os.Parcel parcel,
                android.os.Parcelable.Creator<T> c) {
            if (parcel.readInt() != 0) {
                return c.createFromParcel(parcel);
            } else {
                return null;
            }
        }
        static private <T extends android.os.Parcelable> void writeTypedObject(
                android.os.Parcel parcel, T value, int parcelableFlags) {
            if (value != null) {
                parcel.writeInt(1);
                value.writeToParcel(parcel, parcelableFlags);
            } else {
                parcel.writeInt(0);
            }
        }
    }
}
