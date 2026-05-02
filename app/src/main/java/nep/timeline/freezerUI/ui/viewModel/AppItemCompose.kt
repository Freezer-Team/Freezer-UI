package nep.timeline.freezerUI.ui.viewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.checkers.BatteryOptModeChecker
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker
import nep.timeline.freezerUI.entity.AppItem
import nep.timeline.freezerUI.ui.custom.CustomBasicComponent
import nep.timeline.freezerUI.ui.utils.AppContext
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun AppItemCompose(
    app: AppItem,
    type: Int
) {
    var subtitle: String? = null
    var subtitleColor = Color(60, 179, 113)
    if ((!app.white || type == 1) && (app.black || type == 0)) {
        val otherConfig = if (SettingsConfigChecker.isFullyAutomatic()) app.bluetoothCheck else (((app.whiteProcCount + app.killProcCount > 0) || app.ignoreRecording || app.intervalUnfreeze || (app.binderFreeze && !SettingsConfigChecker.isBinderFreeze()) || (app.backgroundIntent && !app.backgroundPlay) || app.notificationKeep || app.oomLevel != 0) && !SettingsConfigChecker.isLiteMode()) || app.bluetoothCheck || app.networkCheck || app.socket
        if (SettingsConfigChecker.isProMode()) {
            subtitleColor = Color(255, 140, 0)
            if (app.backgroundPlay)
                subtitle = stringResource(R.string.background_play)
            else if (app.locationCheck != 0)
                subtitle = stringResource(R.string.location_check)
            else if (app.backgroundLevel == 1)
                subtitle = stringResource(R.string.direct_app)
            else if (app.backgroundLevel == 2)
                subtitle = stringResource(R.string.foreground_service)
            else if (app.idle && (BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.AUTO || BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.FULL))
                subtitle = stringResource(R.string.battery_opt)
            else if (otherConfig) {
                subtitle = stringResource(R.string.other_config)
                subtitleColor = Color(60, 179, 113)
            }
        } else if (app.idle && (BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.AUTO || BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.FULL))
            subtitle = stringResource(R.string.battery_opt)
        else if (otherConfig)
            subtitle = stringResource(R.string.other_config)
    }

    CustomBasicComponent(
        title = app.appName,
        subtitle = subtitle,
        subtitleColor = subtitleColor,
        summary = app.packageName + if (app.userId != 0) "#" + app.userId else "",
        leftAction = {
            Image(
                painter = rememberDrawablePainter(drawable = app.appIcon),
                contentDescription = app.appName,
                modifier = Modifier.size(58.dp).padding(end = 16.dp)
            )
        },
        rightActions = {
            if ((app.white && type == 0) || (app.black && type == 1) || app.category != AppItem.Category.Undefined) {
                /*Badge(
                    containerColor = if (app.white && type == 0) (if (isSystemInDarkTheme()) Color.White else Color.Black) else if (app.black && type == 1) Color.Red else Color(60, 179, 113)
                ) {
                    Text(
                        text = if (app.white && type == 0) stringResource(R.string.white_app) else if (app.black && type == 1) stringResource(R.string.black_app) else (if (app.category == AppItem.Category.Game) stringResource(R.string.game) else (if (app.category == AppItem.Category.Music) stringResource(R.string.music) else (if (app.category == AppItem.Category.Map) stringResource(R.string.map) else (if (app.category == AppItem.Category.Accessibility) stringResource(R.string.accessibility) else stringResource(R.string.other))))),
                        style = MiuixTheme.textStyles.footnote2,
                        color = if (app.white && isSystemInDarkTheme()) Color.Black else Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }*/
                StatusTag(
                    label = if (app.white && type == 0) stringResource(R.string.white_app) else if (app.black && type == 1) stringResource(R.string.black_app) else (if (app.category == AppItem.Category.Game) stringResource(R.string.game) else (if (app.category == AppItem.Category.Music) stringResource(R.string.music) else (if (app.category == AppItem.Category.Map) stringResource(R.string.map) else (if (app.category == AppItem.Category.Accessibility) stringResource(R.string.accessibility) else stringResource(R.string.other))))),
                    backgroundColor = if (app.white && type == 0) (if (isSystemInDarkTheme()) Color.White else Color.Black) else if (app.black && type == 1) Color.Red else Color(60, 179, 113),
                    contentColor = if (app.white && isSystemInDarkTheme()) Color.Black else Color.White
                )
            }
        },
        onClick = {
            AppContext.enterAppPage(app)
        },
        insideMargin = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Composable
fun StatusTag(
    label: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            text = label,
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight(750),
            maxLines = 1,
            softWrap = false
        )
    }
}