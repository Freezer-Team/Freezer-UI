package nep.timeline.freezerUI.ui.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import java.io.File

object BackgroundManager {
    var currentUri by mutableStateOf<Uri?>(null)

    const val topAppBarAlpha = 0.55f
    const val cardAlpha = 0.55f
    const val forceSmallTop = false

    val topAppBarBlurRadius = 15.dp
    val cardBlurRadius = 15.dp

    fun init(context: Context) {
        val file = File(context.filesDir, "background.jpg")
        currentUri = if (file.exists()) Uri.fromFile(file) else null
    }

    fun refresh(newUri: Uri) {
        currentUri = newUri.buildUpon()
            .appendQueryParameter(
                "rev",
                System.currentTimeMillis().toString()
            )
            .build()
    }

    fun remove() {
        if (currentUri != null)
            File(currentUri?.path!!).delete()
        currentUri = null
    }
}