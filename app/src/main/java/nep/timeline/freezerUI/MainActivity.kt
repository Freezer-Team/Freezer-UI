package nep.timeline.freezerUI

import android.app.ComponentCaller
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import nep.timeline.freezerUI.binder.BinderService
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.settings.ApplicationSettings
import nep.timeline.freezerUI.configs.settings.GlobalSettings
import nep.timeline.freezer.provide.DataBinder
import nep.timeline.freezer.provide.FileBinder
import nep.timeline.freezerUI.ui.app.App
import nep.timeline.freezerUI.ui.dialog.ActiveDialog
import nep.timeline.freezerUI.ui.dialog.ExceptionDialog
import nep.timeline.freezerUI.ui.dialog.RootDialog
import nep.timeline.freezerUI.ui.dialog.XPActiveDialog
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.BackgroundManager
import nep.timeline.freezerUI.ui.utils.BackupUtils
import nep.timeline.freezerUI.ui.viewModel.AppListViewModel
import nep.timeline.freezerUI.utils.EnvUtils
import nep.timeline.freezerUI.verification.Verification
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.Base64
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    object AppListViewModelSingleton {
        val appListViewModel: AppListViewModel by lazy {
            AppListViewModel()
        }
    }

    private val gson: Gson = GsonBuilder().serializeNulls().create()

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (resultCode == RESULT_OK && data != null) {
            val uri = data.data ?: return

            if (requestCode == BackupUtils.CREATE_DOCUMENT_CODE) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
                    bufferedWriter.write(Base64.getEncoder().encodeToString((gson.toJson(GlobalVars.globalSettings) + "|" + gson.toJson(GlobalVars.applicationSettings)).toByteArray(StandardCharsets.UTF_8)))
                    bufferedWriter.close()
                    AppContext.showToast(R.string.saved_config)
                } catch (_: Throwable) {
                    AppContext.showToast(R.string.config_write_failed)
                }
            } else if (requestCode == BackupUtils.OPEN_DOCUMENT_CODE) {
                try {
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line = bufferedReader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = bufferedReader.readLine()
                    }
                    val content = String(Base64.getDecoder().decode(stringBuilder.toString()), StandardCharsets.UTF_8).split("|")
                    if (content.size > 1) {
                        val globalSettingsData = content[0]
                        val applicationSettingsData = content[1]

                        val globalSettings = gson.fromJson(globalSettingsData, GlobalSettings::class.java)
                        val applicationSettings = gson.fromJson(applicationSettingsData, ApplicationSettings::class.java)

                        GlobalVars.globalSettings = globalSettings
                        GlobalVars.applicationSettings = applicationSettings
                        ConfigManager.saveConfigWithBinder()

                        AppContext.showToast(R.string.read_config)
                        this.recreate()
                    } else AppContext.showToast(R.string.read_config_failed)
                } catch (_: Throwable) {
                    AppContext.showToast(R.string.read_config_failed)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContext.init(this)
        BackgroundManager.init(this)
        BackupUtils.init(this)

        Verification.runActivity()

        File(this.filesDir.parentFile, "Updater").delete()

        setContent {
            val darkMode = isSystemInDarkTheme()

            DisposableEffect(darkMode) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkMode },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkMode },
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false // Xiaomi moment, this code must be here
                }

                onDispose {}
            }

            BinderService.register(this)

            val binder = DataBinder.getInstance()

            val notActive = remember { mutableStateOf(binder == null) }
            XPActiveDialog(notActive)

            if (!notActive.value) {
                var success = false
                try {
                    val isExists = FileBinder.getInstance().fileIsExists(GlobalVars.CONFIG_DIR)
                    if (!isExists) {
                        FileBinder.getInstance().makeDir(GlobalVars.CONFIG_DIR)
                        Toast.makeText(
                            this,
                            resources.getString(R.string.first_use),
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(
                            Intent().setAction("android.intent.action.VIEW")
                                .setData("https://docs.sakion.top".toUri())
                        )
                        exitProcess(0)
                    }

                    ConfigManager.readConfigWithBinder()
                    success = true
                } catch (_: RemoteException) {
                    ActiveDialog(remember { mutableStateOf(true) })
                } catch (_: SecurityException) {
                    ActiveDialog(remember { mutableStateOf(true) })
                } catch (_: NullPointerException) {
                    ActiveDialog(remember { mutableStateOf(true) })
                } catch (e: Exception) {
                    val clip = ClipData.newPlainText("text", Log.getStackTraceString(e))
                    val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    manager.setPrimaryClip(clip)
                    ExceptionDialog(remember { mutableStateOf(true) })
                }

                if (success) {
                    val isNotRoot = remember { mutableStateOf(!EnvUtils.checkRoot() && GlobalVars.globalSettings.suExecute) }
                    RootDialog(isNotRoot)
                } else {
                    GlobalVars.globalSettings = GlobalSettings()
                }
            } else {
                GlobalVars.globalSettings = GlobalSettings()
            }

            App(!notActive.value)
        }
    }
}