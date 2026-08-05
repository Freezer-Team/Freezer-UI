package nep.timeline.freezer.provide;

import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import nep.timeline.freezer.binders.VerificationInterface;
import nep.timeline.freezerUI.binder.BinderService;

public class VerificationBinder extends VerificationInterface.Stub {
    public static VerificationInterface getInstance() {
        IBinder binder = BinderService.getBinder("Verification");
        return VerificationInterface.Stub.asInterface(binder);
    }

    @Override
    public String verification(String requestType, String username, String password, List<String> accounts) throws RemoteException { throw new UnsupportedOperationException(); }
}
