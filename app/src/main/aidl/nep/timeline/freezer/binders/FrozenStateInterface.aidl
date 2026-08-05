package nep.timeline.freezer.binders;

interface FrozenStateInterface {
    String isFrozen(String packageName, int userId);
}