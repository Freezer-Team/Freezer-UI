package nep.timeline.freezerUI.entity;

import android.content.pm.PackageInfo;

import java.util.Set;

import lombok.Data;

@Data
public class AppInfo {
    public PackageInfo packageInfo;
    public boolean system;
    public boolean white;
    public boolean black;
    public int oomLevel;
    public int backgroundLevel;
    public boolean intervalUnfreeze;
    public int intervalUnfreezeDelay;
    public boolean binderFreeze;
    public boolean backgroundPlay;
    public boolean backgroundIntent;
    public boolean notificationKeep;
    public int locationCheck;
    public boolean ignoreRecording;
    public boolean bluetoothCheck;
    public boolean ignoreBinder;
    public boolean socket;
    public boolean netReceive;
    public boolean networkCheck;
    public boolean idle;
    public Set<String> processSet;
    public Set<String> killProcessSet;
    public Set<String> whiteProcessSet;
}
