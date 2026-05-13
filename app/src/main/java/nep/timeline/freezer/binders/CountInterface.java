package nep.timeline.freezer.binders;
public interface CountInterface extends android.os.IInterface
{
    /** Default implementation for CountInterface. */
    public static class Default implements nep.timeline.freezer.binders.CountInterface
    {
        @Override public java.util.Map<java.lang.String,java.lang.String> getHourlyCounts() throws android.os.RemoteException
        {
            return null;
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements nep.timeline.freezer.binders.CountInterface
    {
        /** Construct the stub and attach it to the interface. */
        @SuppressWarnings("this-escape")
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an nep.timeline.freezer.binders.CountInterface interface,
         * generating a proxy if needed.
         */
        public static nep.timeline.freezer.binders.CountInterface asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof nep.timeline.freezer.binders.CountInterface))) {
                return ((nep.timeline.freezer.binders.CountInterface)iin);
            }
            return new nep.timeline.freezer.binders.CountInterface.Stub.Proxy(obj);
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
                case TRANSACTION_getHourlyCounts:
                {
                    java.util.Map<java.lang.String,java.lang.String> _result = this.getHourlyCounts();
                    reply.writeNoException();
                    if (_result == null) {
                        reply.writeInt(-1);
                    } else {
                        reply.writeInt(_result.size());
                        _result.forEach((k, v) -> {
                            reply.writeString(k);
                            reply.writeString(v);
                        });
                    }
                    break;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            return true;
        }
        protected static class Proxy implements nep.timeline.freezer.binders.CountInterface
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
            @Override public java.util.Map<java.lang.String,java.lang.String> getHourlyCounts() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.Map<java.lang.String,java.lang.String> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getHourlyCounts, _data, _reply, 0);
                    _reply.readException();
                    {
                        int N = _reply.readInt();
                        _result = N < 0 ? null : new java.util.HashMap<>();
                        java.util.stream.IntStream.range(0, N).forEach(i -> {
                            String k = _reply.readString();
                            java.lang.String v;
                            v = _reply.readString();
                            _result.put(k, v);
                        });
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_getHourlyCounts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "nep.timeline.freezer.binders.CountInterface";
    public java.util.Map<java.lang.String,java.lang.String> getHourlyCounts() throws android.os.RemoteException;
}
