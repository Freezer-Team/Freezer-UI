package nep.timeline.freezerUI.ui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StaticFieldLeak")
object BackupUtils {
    lateinit var activity: Activity

    fun init(activity: Activity) {
        BackupUtils.activity = activity
    }

    const val CREATE_DOCUMENT_CODE: Int = 255774
    const val OPEN_DOCUMENT_CODE: Int = 277451
    const val BACKUP_FILE_NAME: String = "FREEZER_SETTINGS_BACKUP"

    fun backup(activity: Activity) {
        val backupFileName = BACKUP_FILE_NAME + SimpleDateFormat("_yyyy-MM-dd-HH-mm-ss", Locale.CHINA).format(Date())
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, backupFileName)
        activity.startActivityForResult(intent, CREATE_DOCUMENT_CODE)
    }

    fun restore(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        activity.startActivityForResult(intent, OPEN_DOCUMENT_CODE)
    }
}