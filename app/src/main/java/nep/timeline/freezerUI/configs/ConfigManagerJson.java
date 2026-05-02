package nep.timeline.freezerUI.configs;

import android.os.RemoteException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import nep.timeline.freezerUI.GlobalVars;
import nep.timeline.freezer.binders.DataInterface;
import nep.timeline.freezer.binders.FileInterface;
import nep.timeline.freezerUI.configs.settings.ApplicationSettings;
import nep.timeline.freezerUI.configs.settings.GlobalSettings;
import nep.timeline.freezer.provide.DataBinder;
import nep.timeline.freezer.provide.FileBinder;

public class ConfigManagerJson implements ConfigManagerInterface {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private final String globalSettingsName = "GlobalSettings.json";
    private final String applicationSettingsName = "ApplicationSettings.json";

    public void readConfigWithBinder() {
        FileInterface fileInterface = FileBinder.getInstance();
        if (fileInterface == null) {
            GlobalVars.globalSettings = new GlobalSettings();
            GlobalVars.applicationSettings = new ApplicationSettings();
            return;
        }

        try {
            if (!fileInterface.fileIsExists(GlobalVars.CONFIG_DIR + "/" + globalSettingsName)) {
                GlobalVars.globalSettings = new GlobalSettings();
                saveConfigWithBinder(false);
            } else {
                String globalData = fileInterface.readString(GlobalVars.CONFIG_DIR + "/" + globalSettingsName);
                GlobalVars.globalSettings = gson.fromJson(globalData, GlobalSettings.class);
                if (GlobalVars.globalSettings == null) {
                    GlobalVars.globalSettings = new GlobalSettings();
                    saveConfigWithBinder(false);
                }
            }
            if (!fileInterface.fileIsExists(GlobalVars.CONFIG_DIR + "/" + applicationSettingsName)) {
                GlobalVars.applicationSettings = new ApplicationSettings();
                saveConfigWithBinder(false);
            } else {
                String applicationData = fileInterface.readString(GlobalVars.CONFIG_DIR + "/" + applicationSettingsName);
                GlobalVars.applicationSettings = gson.fromJson(applicationData, ApplicationSettings.class);
                if (GlobalVars.applicationSettings == null) {
                    GlobalVars.applicationSettings = new ApplicationSettings();
                    saveConfigWithBinder(false);
                }
            }
        } catch (RemoteException ignored) {

        } catch (JsonSyntaxException | JsonIOException e) {
            GlobalVars.globalSettings = new GlobalSettings();
            GlobalVars.applicationSettings = new ApplicationSettings();
            saveConfigWithBinder(false);
        }
    }

    public void saveConfigWithBinder(boolean refresh) {
        FileInterface fileInterface = FileBinder.getInstance();
        if (fileInterface == null) {
            GlobalVars.globalSettings = new GlobalSettings();
            GlobalVars.applicationSettings = new ApplicationSettings();
            return;
        }

        try {
            GlobalSettings globalSettings = GlobalVars.globalSettings;
            if (globalSettings != null) {
                String globalConfigStr = gson.toJson(GlobalVars.globalSettings);
                fileInterface.writeString(GlobalVars.CONFIG_DIR + "/" + globalSettingsName, globalConfigStr);
            }
            ApplicationSettings applicationSettings = GlobalVars.applicationSettings;
            if (applicationSettings != null) {
                String applicationConfigStr = gson.toJson(GlobalVars.applicationSettings);
                fileInterface.writeString(GlobalVars.CONFIG_DIR + "/" + applicationSettingsName, applicationConfigStr);
            }
            if (refresh) {
                DataInterface dataInterface = DataBinder.getInstance();
                if (dataInterface != null)
                    dataInterface.get("Refresh_Config");
            }
        } catch (RemoteException ignored) {

        }
    }
}