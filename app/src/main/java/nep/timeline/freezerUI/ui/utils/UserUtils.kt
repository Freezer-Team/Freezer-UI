package nep.timeline.freezerUI.ui.utils

import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezer.core.jni.NativeMethodsContrast
import nep.timeline.freezer.provide.DataBinder
import nep.timeline.freezer.provide.FileBinder

object UserUtils {
    var userData = ""
    var state = -1

    fun init() {
        val fileBinder = FileBinder.getInstance()
        if (fileBinder == null || state != -1)
            return

        if (!fileBinder.fileIsExists(GlobalVars.CONFIG_DIR + "/verification"))
            fileBinder.writeString(GlobalVars.CONFIG_DIR + "/verification", "")
        if (!fileBinder.fileIsExists(GlobalVars.CONFIG_DIR + "/user.data"))
            fileBinder.writeString(GlobalVars.CONFIG_DIR + "/user.data", "")
        val userString = fileBinder.readString(GlobalVars.CONFIG_DIR + "/user.data").trim()
        userData = userString
        val isVerify = userString.isNotEmpty() && DataBinder.getInstance().get("VERIFICATION->"
                + fileBinder.readString(GlobalVars.CONFIG_DIR + "/verification").replace("\n", "")) == "true"
        state = if (isVerify) 0 else if (userString.isNotEmpty()) 1 else 2
    }
}