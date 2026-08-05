package nep.timeline.freezer.binders;

import nep.timeline.freezer.binders.UIComponentData;

import rikka.parcelablelist.ParcelableListSlice;
import rikka.parcelablelist.StringListSlice;

interface UIServiceInterface {
    StringListSlice getScripts();

    StringListSlice getPackages(String script);

    StringListSlice getPages(String script, String packageName);

    ParcelableListSlice<UIComponentData> getComponents(String script, String packageName, String page);
}