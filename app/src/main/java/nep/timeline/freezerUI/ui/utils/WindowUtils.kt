package nep.timeline.freezerUI.ui.utils

import android.os.Handler
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.PopTip
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.utils.EnvUtils

object WindowUtils {
    val handler: Handler = EnvUtils.makeHandler("UI")

    fun showToast(string: String) {
        PopTip.build().setIconResId(R.drawable.ic_logo_foreground).setTheme(DialogX.THEME.AUTO).setMessage(string).show()
    }

    fun showToast(resourceId: Int) {
        PopTip.build().setIconResId(R.drawable.ic_logo_foreground).setTheme(DialogX.THEME.AUTO).setMessage(resourceId).show()
    }
}