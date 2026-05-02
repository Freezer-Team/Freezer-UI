package nep.timeline.freezerUI.ui.viewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.PopTip
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.entity.AppItem
import nep.timeline.freezerUI.ui.custom.CustomBasicComponent
import nep.timeline.freezerUI.ui.utils.AppContext
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun FrozenAppItemCompose(
    app: AppItem
) {
    val message = stringResource(R.string.system_not_flagged_but_frozen)

    CustomBasicComponent(
        title = app.appName,
        subtitle = buildString {
            append(app.applicationProcessCount.toString() + " " + stringResource(R.string.process))
            if (app.frozenProcessCount > 0)
                append(" " + app.frozenProcessCount.toString() + " " + stringResource(R.string.is_frozen))
        },
        summary = getMemSize(app.rss),
        leftAction = {
            Image(
                painter = rememberDrawablePainter(drawable = app.appIcon),
                contentDescription = app.appName,
                modifier = Modifier.size(58.dp).padding(end = 16.dp)
            )
        },
        rightActions = {
            if (app.frozenType != null)
                /*Badge(
                    containerColor = MiuixTheme.colorScheme.onPrimaryVariant
                ) {
                    Text(
                        text = if (app.frozenType.equals("SYSTEM_NOT_FLAGGED_BUT_FROZEN")) stringResource(R.string.frozen_wrong) else (app.frozenType + " " + stringResource(R.string.freezing)),
                        style = MiuixTheme.textStyles.footnote2,
                        color = MiuixTheme.colorScheme.primaryVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }*/
                StatusTag(
                    label = if (app.frozenType.equals("SYSTEM_NOT_FLAGGED_BUT_FROZEN")) stringResource(R.string.frozen_wrong) else (app.frozenType + " " + stringResource(R.string.freezing)),
                    backgroundColor = MiuixTheme.colorScheme.onPrimaryVariant,
                    contentColor = MiuixTheme.colorScheme.primaryVariant
                )
        },
        modifier = Modifier.combinedClickable(
            onLongClick = {
                AppContext.enterAppPage(app)
            },
            onClick = {
                if (app.frozenType != null && app.frozenType.equals("SYSTEM_NOT_FLAGGED_BUT_FROZEN"))
                    PopTip.build().setTheme(DialogX.THEME.AUTO).setMessage(message).show()
                else if (!app.isFrozen) {
                    val notification = "\uD83D\uDCE2"
                    val audio = "\uD83C\uDFB5"
                    val media = "\uD83C\uDF9E"
                    val camera = "\uD83D\uDCF7"
                    val accessibility = "\u267F"
                    val xposed = "\uD83E\uDDE9"
                    val autofill = "\uD83E\uDDFB"
                    val credential = "\uD83D\uDD11"
                    val backup = "\uD83D\uDCC4"
                    val input = "\u2328"
                    val netTrans = "\uD83D\uDEDC"
                    val location = "\uD83D\uDCCD"
                    val recording = "\uD83C\uDF99️"
                    val vpn = "\uD83C\uDF10"
                    val visible = "\uD83D\uDC41"
                    val window = "\uD83E\uDE9F"
                    val system = "\uD83D\uDD12"
                    val foreground = "\uD83E\uDDBE"
                    val broadcasting = "\uD83D\uDCE2"
                    val coldStart = "\uD83E\uDDCA"
                    val push = "\uD83D\uDCF0"
                    val waiting = "\u23F3"
                    val reason = when (app.notFrozenReason) {
                        "WHITELIST" -> "白名单"
                        "NOTIFICATION" -> "常驻通知中 $notification"
                        "AUDIO" -> "播放音频中 $audio"
                        "MEDIA" -> "播放媒体中 $media"
                        "CAMERA" -> "调用摄像头中 $camera"
                        "ACCESSIBILITY" -> "使用无障碍服务中 $accessibility"
                        "SERVICE" -> "服务执行中"
                        "XPOSED" -> "Xposed模块 $xposed"
                        "AUTO_FILL" -> "使用自动填充服务中 $autofill"
                        "CREDENTIAL" -> "使用通行密钥服务中 $credential"
                        "BACKUP" -> "备份中 $backup"
                        "INPUT" -> "输入法 $input"
                        "NET_TRANS" -> "网络传输中 $netTrans"
                        "TCP_SOCKET" -> "等待网络响应 $netTrans"
                        "LOCATION" -> "定位中 $location"
                        "RECORDING" -> "录音中 $recording"
                        "WAITING_PUSH_RESPONSE" -> "等待推送响应 $push"
                        "VPN" -> "使用VPN服务中 $vpn"
                        "VISIBLE" -> "应用前台 $visible"
                        "WINDOW" -> "窗口可见 $window"
                        "SYSTEM" -> "系统应用 $system"
                        "FORE_GROUND" -> "前台服务 $foreground"
                        "BROADCASTING" -> "广播中 $broadcasting"
                        "COLD_START" -> "冷启动中 $coldStart"
                        "WAITING_FROZEN" -> "等待冻结 $waiting"
                        else -> "未知"
                    }
                    PopTip.build().setTheme(DialogX.THEME.AUTO).setMessage(reason).show()
                }
            }
        ),
        insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    )
}

private fun getMemSize(mem: Long): String {
    val bigDecimal = BigDecimal(mem)
    if (mem < 1000) return mem.toString() + "KB"
    if (mem < 1024000) return bigDecimal.divide(BigDecimal(1024), 0, RoundingMode.HALF_UP)
        .toString() + "MB"
    return bigDecimal.divide(BigDecimal(1048576), 2, RoundingMode.HALF_UP).toString() + "GB"
}