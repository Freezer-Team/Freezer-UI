package nep.timeline.freezer.binders;

public interface LogInterface extends android.os.IInterface
{
    /** Default implementation for LogInterface. */
    public static class Default implements LogInterface
    {
        @Override public void i(java.lang.String msg) throws android.os.RemoteException
        {
        }
        @Override public void e(java.lang.String msg) throws android.os.RemoteException
        {
        }
        @Override public void d(java.lang.String msg) throws android.os.RemoteException
        {
        }
        @Override public void w(java.lang.String msg) throws android.os.RemoteException
        {
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements LogInterface
    {
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.LogInterface interface,
         * generating a proxy if needed.
         */
        public static LogInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof LogInterface))) {
                return ((LogInterface)iin);
            }
            return new LogInterface.Stub.Proxy(obj);
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
                case TRANSACTION_i:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.i(_arg0);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_e:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.e(_arg0);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_d:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.d(_arg0);
                    reply.writeNoException();
                    break;
                }
                case TRANSACTION_w:
                {
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.w(_arg0);
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
        protected static class Proxy implements LogInterface
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
            @Override public void i(java.lang.String msg) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(msg);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_i, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void e(java.lang.String msg) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(msg);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_e, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void d(java.lang.String msg) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(msg);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_d, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void w(java.lang.String msg) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(msg);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_w, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
        static final int TRANSACTION_i = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_e = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_d = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_w = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    }
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.LogInterface";
    public void i(java.lang.String msg) throws android.os.RemoteException;
    public void e(java.lang.String msg) throws android.os.RemoteException;
    public void d(java.lang.String msg) throws android.os.RemoteException;
    public void w(java.lang.String msg) throws android.os.RemoteException;
}
