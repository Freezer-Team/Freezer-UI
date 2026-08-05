@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.custom.BackNavigationIcon
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.BackgroundManager
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.WindowUtils
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.utils.textureBlur
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme

@Composable
fun BatterySettingsPage(
    active: Boolean,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val navigator = LocalNavigator.current

    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = "Battery \u2192 " + stringResource(R.string.settings),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        BackNavigationIcon(
                            onClick = { navigator.pop() },
                        )
                    }
                )
            }
        },
    ) { innerPadding ->
        BatterySettingsContent(
            active = active,
            padding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            backdrop = backdrop,
            scrollEndHaptic = scrollEndHaptic
        )
    }
}

@Composable
private fun BatterySettingsContent(
    active: Boolean,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val lazyListState = rememberLazyListState()
    val imageUri = BackgroundManager.currentUri
    val cardBackdrop = rememberBlurBackdrop()
    val cardBlurActive = cardBackdrop != null && imageUri != null

    val painter = BackgroundManager.readImage()

    val cardColors = if (cardBlurActive) CardColors(
        color = colorScheme.surfaceContainer.copy(BackgroundManager.cardAlpha),
        contentColor = colorScheme.onSurfaceContainer.copy(BackgroundManager.cardAlpha)
    ) else CardColors(
        color = colorScheme.surfaceContainer,
        contentColor = colorScheme.onSurfaceContainer
    )

    val contentPadding = pageContentPadding(padding, padding, isWideScreen)
    Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
        if (imageUri != null)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (cardBackdrop != null) Modifier.layerBackdrop(cardBackdrop) else Modifier),
                contentScale = ContentScale.Crop
            )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.pageScrollModifiers(
                scrollEndHaptic,
                true,
                topAppBarScrollBehavior,
            ),
            contentPadding = contentPadding
        ) {
            if (active) {
                item {
                    Card(
                        modifier = Modifier.padding(12.dp)
                            .then(
                                if (cardBlurActive)
                                    Modifier.clip(RoundedCornerShape(CardDefaults.CornerRadius))
                                        .textureBlur(
                                            backdrop = cardBackdrop,
                                            blurRadius = BackgroundManager.cardBlurRadius,
                                            colors = BlurColors(
                                                blendColors = listOf(
                                                    BlendColorEntry(color = colorScheme.surface.copy(BackgroundManager.cardBlurAlpha)),
                                                ),
                                            )
                                        )
                                else Modifier
                            ), colors = cardColors
                    ) {
                        val disablePowerSaveMode =
                            remember { mutableStateOf(GlobalVars.globalSettings.disablePowerSaveMode) }
                        if (!SettingsConfigChecker.isLiteMode()) {
                            SwitchPreference(
                                title = stringResource(R.string.disable_power_save_mode),
                                checked = disablePowerSaveMode.value,
                                onCheckedChange = {
                                    disablePowerSaveMode.value = it
                                    GlobalVars.globalSettings.disablePowerSaveMode = it
                                    ConfigManager.saveConfigWithBinder()
                                }
                            )
                        }

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            val superStandbyMode =
                                remember { mutableStateOf(GlobalVars.globalSettings.superStandbyMode) }
                            AnimatedVisibility(
                                label = "SuperStandby",
                                visible = !disablePowerSaveMode.value || SettingsConfigChecker.isLiteMode()
                            ) {
                                SwitchPreference(
                                    title = stringResource(R.string.super_standby_mode),
                                    summary = stringResource(R.string.extreme_standby_mode_desc),
                                    checked = superStandbyMode.value,
                                    onCheckedChange = {
                                        superStandbyMode.value = it
                                        GlobalVars.globalSettings.superStandbyMode = it
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )
                            }

                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R)
                                AnimatedVisibility(
                                    label = "ExtremeStandby",
                                    visible = (!disablePowerSaveMode.value || SettingsConfigChecker.isLiteMode()) && superStandbyMode.value
                                ) {
                                    val extremeStandbyMode =
                                        remember { mutableStateOf(GlobalVars.globalSettings.extremeStandbyMode) }
                                    SwitchPreference(
                                        title = stringResource(R.string.extreme_standby_mode),
                                        summary = stringResource(R.string.extreme_standby_mode_desc),
                                        checked = extremeStandbyMode.value,
                                        onCheckedChange = {
                                            extremeStandbyMode.value = it
                                            GlobalVars.globalSettings.extremeStandbyMode =
                                                it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )
                                }
                        }

                        if (SettingsConfigChecker.isProMode() || GlobalVars.globalSettings.batteryOptIgnoreProMode) {
                            val batteryOptControlMode =
                                remember { mutableIntStateOf(GlobalVars.globalSettings.batteryOptControlMode) }
                            OverlayDropdownPreference(
                                title = stringResource(R.string.battery_opt),
                                items = listOf(
                                    stringResource(R.string.battery_opt_disable),
                                    stringResource(R.string.battery_opt_auto),
                                    stringResource(R.string.battery_opt_full),
                                    stringResource(R.string.battery_opt_all_whitelist)
                                ),
                                selectedIndex = batteryOptControlMode.intValue,
                                onSelectedIndexChange = {
                                    batteryOptControlMode.intValue = it
                                    GlobalVars.globalSettings.batteryOptControlMode = it
                                    ConfigManager.saveConfigWithBinder()
                                    WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(lazyListState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            trackPadding = contentPadding,
        )
    }
}
