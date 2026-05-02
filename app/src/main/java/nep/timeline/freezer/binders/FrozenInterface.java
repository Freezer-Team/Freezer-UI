package nep.timeline.freezer.binders;

public interface FrozenInterface extends android.os.IInterface
{
    /** Default implementation for FrozenInterface. */
    public static class Default implements FrozenInterface
    {
        @Override public void temporaryUnfreezeWithUID(int uid, java.lang.String reason) throws android.os.RemoteException
        {
        }
        @Override public void temporaryUnfreeze(java.lang.String packageName, int userId, java.lang.String reason) throws android.os.RemoteException
        {
        }
        @Override public void temporaryUnfreezeWithUIDAndInterval(int uid, java.lang.String reason, long interval) throws android.os.RemoteException
        {
        }
        @Override public void temporaryUnfreezeWithInterval(java.lang.String packageName, int userId, java.lang.String reason, long interval) throws android.os.RemoteException
        {
        }
        @Override public void freezerWithUID(int uid) throws android.os.RemoteException
        {
        }
        @Override public void freezer(java.lang.String packageName, int userId) throws android.os.RemoteException
        {
        }
        @Override public void thawWithUID(int uid) throws android.os.RemoteException
        {
        }
        @Override public void thaw(java.lang.String packageName, int userId) throws android.os.RemoteException
        {
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements FrozenInterface
    {
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.FrozenInterface interface,
         * generating a proxy if needed.
         */
        public static FrozenInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof FrozenInterface))) {
                return ((FrozenInterface)iin);
            }
            return new FrozenInterface.Stub.Proxy(obj);
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
                case TRANSACTION_temporaryUnfreezeWithUID:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    java.lang.String _arg1;
                    _arg1 = data.readString();
                    this.temporaryUnfreezeWithUID(_arg0, _arg1);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_temporaryUnfreeze:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    java.lang.String _arg2;
                    _arg2 = data.readString();
                    this.temporaryUnfreeze(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_temporaryUnfreezeWithUIDAndInterval:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    java.lang.String _arg1;
                    _arg1 = data.readString();
                    long _arg2;
                    _arg2 = data.readLong();
                    this.temporaryUnfreezeWithUIDAndInterval(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_temporaryUnfreezeWithInterval:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    java.lang.String _arg2;
                    _arg2 = data.readString();
                    long _arg3;
                    _arg3 = data.readLong();
                    this.temporaryUnfreezeWithInterval(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_freezerWithUID:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    this.freezerWithUID(_arg0);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_freezer:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    this.freezer(_arg0, _arg1);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_thawWithUID:
                {
                    int _arg0;
                    _arg0 = data.readInt();
                    this.thawWithUID(_arg0);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_thaw:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    this.thaw(_arg0, _arg1);
                    reply.writeNoException();
                    break;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }
        protected static class Proxy implements FrozenInterface
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
            @Override public void temporaryUnfreezeWithUID(int uid, java.lang.String reason) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(reason);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_temporaryUnfreezeWithUID, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void temporaryUnfreeze(java.lang.String packageName, int userId, java.lang.String reason) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_temporaryUnfreeze, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void temporaryUnfreezeWithUIDAndInterval(int uid, java.lang.String reason, long interval) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(reason);
                    _data.writeLong(interval);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_temporaryUnfreezeWithUIDAndInterval, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void temporaryUnfreezeWithInterval(java.lang.String packageName, int userId, java.lang.String reason, long interval) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    _data.writeLong(interval);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_temporaryUnfreezeWithInterval, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void freezerWithUID(int uid) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_freezerWithUID, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void freezer(java.lang.String packageName, int userId) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_freezer, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void thawWithUID(int uid) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_thawWithUID, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void thaw(java.lang.String packageName, int userId) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_thaw, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
        static final int TRANSACTION_temporaryUnfreezeWithUID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_temporaryUnfreeze = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_temporaryUnfreezeWithUIDAndInterval = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_temporaryUnfreezeWithInterval = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_freezerWithUID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_freezer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
        static final int TRANSACTION_thawWithUID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
        static final int TRANSACTION_thaw = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    }
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.FrozenInterface";
    public void temporaryUnfreezeWithUID(int uid, java.lang.String reason) throws android.os.RemoteException;
    public void temporaryUnfreeze(java.lang.String packageName, int userId, java.lang.String reason) throws android.os.RemoteException;
    public void temporaryUnfreezeWithUIDAndInterval(int uid, java.lang.String reason, long interval) throws android.os.RemoteException;
    public void temporaryUnfreezeWithInterval(java.lang.String packageName, int userId, java.lang.String reason, long interval) throws android.os.RemoteException;
    public void freezerWithUID(int uid) throws android.os.RemoteException;
    public void freezer(java.lang.String packageName, int userId) throws android.os.RemoteException;
    public void thawWithUID(int uid) throws android.os.RemoteException;
    public void thaw(java.lang.String packageName, int userId) throws android.os.RemoteException;
}
