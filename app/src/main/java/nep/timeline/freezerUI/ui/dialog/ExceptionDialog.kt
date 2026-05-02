package nep.timeline.freezerUI.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import nep.timeline.freezerUI.R
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import kotlin.system.exitProcess

@Composable
fun ExceptionDialog(showDialog: MutableState<Boolean>) {
    val isShow = rememberSaveable { mutableStateOf(false) }
    if (showDialog.value && !isShow.value) {
        isShow.value = !isShow.value
        OverlayDialog(
            title = stringResource(R.string.warning),
            summary = "警告: 出现异常! 异常已复制至剪贴板",
            show = showDialog.value,
            onDismissRequest = {
                showDialog.value = false
                exitProcess(0)
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.ok),
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                    onClick = {
                        showDialog.value = false
                        exitProcess(0)
                    }
                )
            }
        }
    }
}