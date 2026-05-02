package nep.timeline.freezer.binders;

public interface ApplicationInterface extends android.os.IInterface
{
    /** Default implementation for ApplicationInterface. */
    public static class Default implements ApplicationInterface
    {
        @Override public java.util.List<java.lang.String> getRunningApplication() throws android.os.RemoteException
        {
            return null;
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements ApplicationInterface
    {
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.ApplicationInterface interface,
         * generating a proxy if needed.
         */
        public static ApplicationInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof ApplicationInterface))) {
                return ((ApplicationInterface)iin);
            }
            return new ApplicationInterface.Stub.Proxy(obj);
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
                case TRANSACTION_getRunningApplication:
                {
                    java.util.List<java.lang.String> _result = this.getRunningApplication();
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
        protected static class Proxy implements ApplicationInterface
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
            @Override public java.util.List<java.lang.String> getRunningApplication() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<java.lang.String> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningApplication, _data, _reply, 0);
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
        static final int TRANSACTION_getRunningApplication = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.ApplicationInterface";
    public java.util.List<java.lang.String> getRunningApplication() throws android.os.RemoteException;
}
