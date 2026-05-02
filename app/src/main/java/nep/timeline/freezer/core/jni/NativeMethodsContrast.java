package nep.timeline.freezer.core.jni;

import java.io.IOException;
import java.util.Set;

public class NativeMethodsContrast {
    // for Application
    public static String verification(String requestType, String username, String password, Set<String> accounts) throws IOException {
        return NativeMethods.O0o0o0oo0o00oo00(requestType, username, password, accounts);
    }
}
