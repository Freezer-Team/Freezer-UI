package nep.timeline.freezer.binders;

interface FrozenInterface {
    void temporaryUnfreezeWithUID(int uid, String reason);

    void temporaryUnfreeze(String packageName, int userId, String reason);

    void temporaryUnfreezeWithUIDAndInterval(int uid, String reason, long interval);

    void temporaryUnfreezeWithInterval(String packageName, int userId, String reason, long interval);

    void freezerWithUID(int uid);

    void freezer(String packageName, int userId);

    void thawWithUID(int uid);

    void thaw(String packageName, int userId);
}