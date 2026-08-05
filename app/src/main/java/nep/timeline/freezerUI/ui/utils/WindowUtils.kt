package nep.timeline.freezerUI.ui.utils

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

fun Activity.hideSystemUI() {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowInsetsController
            .apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

fun Activity.showSystemUI() {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowInsetsController
            .apply {
                show(WindowInsetsCompat.Type.systemBars())
            }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}