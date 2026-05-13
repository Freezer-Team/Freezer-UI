package nep.timeline.freezer.binders;
public interface FileInterface extends android.os.IInterface
{
    /** Default implementation for FileInterface. */
    public static class Default implements nep.timeline.freezer.binders.FileInterface
    {
        @Override public boolean fileIsExists(java.lang.String path) throws android.os.RemoteException
        {
            return false;
        }
        @Override public boolean makeDir(java.lang.String name) throws android.os.RemoteException
        {
            return false;
        }
        @Override public java.lang.String readString(java.lang.String name) throws android.os.RemoteException
        {
            return null;
        }
        @Override public rikka.parcelablelist.StringListSlice readLargeString(java.lang.String name) throws android.os.RemoteException
        {
            return null;
        }
        @Override public boolean writeString(java.lang.String name, java.lang.String value) throws android.os.RemoteException
        {
            return false;
        }
        @Override public java.util.List<java.lang.String> ls(java.lang.String path) throws android.os.RemoteException
        {
            return null;
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements nep.timeline.freezer.binders.FileInterface
    {
        /** Construct the stub and attach it to the interface. */
        @SuppressWarnings("this-escape")
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.FileInterface interface,
         * generating a proxy if needed.
         */
        public static nep.timeline.freezer.binders.FileInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof nep.timeline.freezer.binders.FileInterface))) {
                return ((nep.timeline.freezer.binders.FileInterface)iin);
            }
            return new nep.timeline.freezer.binders.FileInterface.Stub.Proxy(obj);
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
            if (code == INTERFACE_TRANSACTION) {
                reply.writeString(descriptor);
                return true;
            }
            switch (code)
            {
                case TRANSACTION_fileIsExists:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    boolean _result = this.fileIsExists(_arg0);
                    reply.writeNoException();
                    reply.writeInt(((_result)?(1):(0)));
                    break;
                }
                case TRANSACTION_makeDir:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    boolean _result = this.makeDir(_arg0);
                    reply.writeNoException();
                    reply.writeInt(((_result)?(1):(0)));
                    break;
                }
                case TRANSACTION_readString:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    java.lang.String _result = this.readString(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    break;
                }
                case TRANSACTION_readLargeString:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    rikka.parcelablelist.StringListSlice _result = this.readLargeString(_arg0);
                    reply.writeNoException();
                    _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    break;
                }
                case TRANSACTION_writeString:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    java.lang.String _arg1;
                    _arg1 = data.readString();
                    boolean _result = this.writeString(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(((_result)?(1):(0)));
                    break;
                }
                case TRANSACTION_ls:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    java.util.List<java.lang.String> _result = this.ls(_arg0);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    break;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }
        protected static class Proxy implements nep.timeline.freezer.binders.FileInterface
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
            @Override public boolean fileIsExists(java.lang.String path) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_fileIsExists, _data, _reply, 0);
                    _reply.readException();
                    _result = (0!=_reply.readInt());
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public boolean makeDir(java.lang.String name) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_makeDir, _data, _reply, 0);
                    _reply.readException();
                    _result = (0!=_reply.readInt());
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public java.lang.String readString(java.lang.String name) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_readString, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public rikka.parcelablelist.StringListSlice readLargeString(java.lang.String name) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                rikka.parcelablelist.StringListSlice _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_readLargeString, _data, _reply, 0);
                    _reply.readException();
                    _result = _Parcel.readTypedObject(_reply, rikka.parcelablelist.StringListSlice.CREATOR);
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public boolean writeString(java.lang.String name, java.lang.String value) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(value);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_writeString, _data, _reply, 0);
                    _reply.readException();
                    _result = (0!=_reply.readInt());
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public java.util.List<java.lang.String> ls(java.lang.String path) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<java.lang.String> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_ls, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createStringArrayList();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_fileIsExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_makeDir = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_readString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_readLargeString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_writeString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_ls = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.FileInterface";
    public boolean fileIsExists(java.lang.String path) throws android.os.RemoteException;
    public boolean makeDir(java.lang.String name) throws android.os.RemoteException;
    public java.lang.String readString(java.lang.String name) throws android.os.RemoteException;
    public rikka.parcelablelist.StringListSlice readLargeString(java.lang.String name) throws android.os.RemoteException;
    public boolean writeString(java.lang.String name, java.lang.String value) throws android.os.RemoteException;
    public java.util.List<java.lang.String> ls(java.lang.String path) throws android.os.RemoteException;
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
