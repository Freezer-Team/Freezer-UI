package nep.timeline.freezerUI.ui.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import java.io.File

object BackgroundManager {
    var currentUri by mutableStateOf<Uri?>(null)
    var painter : AsyncImagePainter? = null

    const val topAppBarBlurAlpha = 0.65f
    const val cardBlurAlpha = 0.65f
    const val cardAlpha = 0.35f
    const val forceSmallTop = false

    val topAppBarBlurRadius = 45f
    val cardBlurRadius = 45f

    fun init(context: Context) {
        val file = File(context.filesDir, "background.jpg")
        currentUri = if (file.exists()) Uri.fromFile(file) else null
    }

    @Composable
    fun readImage(): AsyncImagePainter =
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentUri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        )

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