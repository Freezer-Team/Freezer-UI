package nep.timeline.freezer.binders;

import java.util.List;

interface VerificationInterface {
    String verification(String requestType, String username, String password, in List<String> accounts);
}