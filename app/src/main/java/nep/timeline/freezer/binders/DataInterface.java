package nep.timeline.freezer.binders;
public interface DataInterface extends android.os.IInterface
{
    /** Default implementation for DataInterface. */
    public static class Default implements nep.timeline.freezer.binders.DataInterface
    {
        @Override public java.lang.String get(java.lang.String name) throws android.os.RemoteException
        {
            return null;
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements nep.timeline.freezer.binders.DataInterface
    {
        /** Construct the stub and attach it to the interface. */
        @SuppressWarnings("this-escape")
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.DataInterface interface,
         * generating a proxy if needed.
         */
        public static nep.timeline.freezer.binders.DataInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof nep.timeline.freezer.binders.DataInterface))) {
                return ((nep.timeline.freezer.binders.DataInterface)iin);
            }
            return new nep.timeline.freezer.binders.DataInterface.Stub.Proxy(obj);
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
                case TRANSACTION_get:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    java.lang.String _result = this.get(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    break;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }
        protected static class Proxy implements nep.timeline.freezer.binders.DataInterface
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
            @Override public java.lang.String get(java.lang.String name) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_get, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_get = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.DataInterface";
    public java.lang.String get(java.lang.String name) throws android.os.RemoteException;
}
