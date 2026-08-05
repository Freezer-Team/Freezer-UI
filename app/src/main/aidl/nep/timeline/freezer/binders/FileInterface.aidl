package nep.timeline.freezer.binders;

import java.util.List;

import rikka.parcelablelist.StringListSlice;

interface FileInterface {
    boolean fileIsExists(String path);

    boolean makeDir(String name);

    String readString(String name);

    StringListSlice readLargeString(String name);

    boolean writeString(String name, String value);

    List<String> ls(String path);
}