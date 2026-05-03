@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.topjohnwu.superuser.Shell
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.checkers.BinderModeChecker
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.WindowUtils
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun FreezerSettingsPage(
    active: Boolean,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current

    val backdrop = rememberBlurBackdrop()
    val blurActive = false//backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = "Freezer -> " + stringResource(R.string.settings),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor
                )
            }
        },
    ) { innerPadding ->
        FreezerSettingsContent(
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
private fun FreezerSettingsContent(
    active: Boolean,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val lazyListState = rememberLazyListState()
    //val imageUri = BackgroundManager.currentUri

    val contentPadding = pageContentPadding(padding, padding, isWideScreen)
    Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
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
                    ) {
                        val fullyAutomatic = remember { mutableStateOf(GlobalVars.globalSettings.fullyAutomatic) }

                        Column {
                            val freezeMethod =
                                remember { mutableIntStateOf(GlobalVars.globalSettings.freezeMethod) }
                            OverlayDropdownPreference(
                                title = stringResource(R.string.freeze_method),
                                items = listOf(
                                    stringResource(R.string.auto),
                                    "API",
                                    "V2",
                                    "V1",
                                    "SIGSTOP",
                                    "SIGTSTP"
                                ),
                                selectedIndex = freezeMethod.intValue,
                                onSelectedIndexChange = {
                                    freezeMethod.intValue = it
                                    GlobalVars.globalSettings.freezeMethod = it
                                    ConfigManager.saveConfigWithBinder()
                                    WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            )

                            AnimatedVisibility(
                                label = "Prefer",
                                visible = freezeMethod.intValue == 2,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                val preferV2Mode =
                                    remember { mutableIntStateOf(GlobalVars.globalSettings.preferV2Mode) }
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.prefer_v2_mode),
                                    items = listOf("FROZEN", "UID"),
                                    summary = stringResource(R.string.prefer_v2_mode_desc),
                                    selectedIndex = preferV2Mode.intValue,
                                    onSelectedIndexChange = {
                                        preferV2Mode.intValue = it
                                        GlobalVars.globalSettings.preferV2Mode = it
                                        ConfigManager.saveConfigWithBinder()
                                        WindowUtils.showToast(R.string.config_set_after_reboot)
                                    }
                                )
                            }
                        }

                        SwitchPreference(
                            title = stringResource(R.string.fully_automatic),
                            summary = stringResource(R.string.fully_automatic_desc),
                            checked = fullyAutomatic.value,
                            onCheckedChange = {
                                fullyAutomatic.value = it
                                GlobalVars.globalSettings.fullyAutomatic = it
                                ConfigManager.saveConfigWithBinder()
                            }
                        )

                        val isProMode = SettingsConfigChecker.isProMode()
                        //val isDebugMode = SettingsConfigChecker.isDebugMode()
                        val liteMode =
                            remember { mutableStateOf(GlobalVars.globalSettings.liteMode) }

                        AnimatedVisibility(
                            visible = !fullyAutomatic.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                if (isProMode) {
                                    val bootFreeze =
                                        remember { mutableStateOf(GlobalVars.globalSettings.bootFreeze) }
                                    SwitchPreference(
                                        title = stringResource(R.string.boot_freeze),
                                        summary = stringResource(R.string.boot_freeze_desc),
                                        checked = bootFreeze.value,
                                        onCheckedChange = {
                                            bootFreeze.value = it
                                            GlobalVars.globalSettings.bootFreeze = it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )

                                    AnimatedVisibility(
                                        label = "Root",
                                        visible = Shell.getShell().isRoot,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        val suExecute =
                                            remember { mutableStateOf(GlobalVars.globalSettings.suExecute) }
                                        SwitchPreference(
                                            title = stringResource(R.string.root_mode),
                                            checked = suExecute.value,
                                            onCheckedChange = {
                                                suExecute.value = it
                                                GlobalVars.globalSettings.suExecute = it
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        val binderItems = listOf(
                            stringResource(R.string.binder_thaw_auto),
                            stringResource(R.string.binder_thaw_native),
                            stringResource(R.string.binder_thaw_android),
                            stringResource(R.string.binder_thaw_kernel),
                            stringResource(R.string.binder_thaw_ebpf),
                            stringResource(R.string.binder_thaw_disable)
                        )
                        val binderMode =
                            remember { mutableIntStateOf(GlobalVars.globalSettings.binderMode) }
                        OverlayDropdownPreference(
                            title = stringResource(R.string.binder_thaw),
                            items = binderItems,
                            selectedIndex = binderMode.intValue,
                            onSelectedIndexChange = {
                                binderMode.intValue = it
                                GlobalVars.globalSettings.binderMode = it
                                ConfigManager.saveConfigWithBinder()
                                when (it) {
                                    3 -> WindowUtils.showToast(R.string.config_request_re_kernel)
                                    4 -> WindowUtils.showToast(R.string.config_request_nkbinder)
                                    else -> WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            }
                        )

                        AnimatedVisibility(
                            label = "RotationUnfreeze",
                            visible = BinderModeChecker.getBinderMode() == BinderModeChecker.BinderMode.DISABLE,
                        ) {
                            Column {
                                val rotationUnfreeze =
                                    remember { mutableStateOf(GlobalVars.globalSettings.rotationUnfreeze) }
                                SwitchPreference(
                                    title = stringResource(R.string.rotation_unfreeze),
                                    checked = rotationUnfreeze.value,
                                    onCheckedChange = {
                                        rotationUnfreeze.value = it
                                        GlobalVars.globalSettings.rotationUnfreeze = it
                                        ConfigManager.saveConfigWithBinder()
                                        WindowUtils.showToast(R.string.config_set_after_reboot)
                                    }
                                )

                                AnimatedVisibility(
                                    label = "Rotation",
                                    visible = rotationUnfreeze.value,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        val rotationUnfreezeDelay =
                                            remember { mutableIntStateOf(GlobalVars.globalSettings.rotationUnfreezeDelay) }

                                        Text(
                                            text = stringResource(R.string.rotation_unfreeze_delay) + " | " + rotationUnfreezeDelay.intValue + " Min",
                                            modifier = Modifier.padding(17.dp)
                                        )

                                        Slider(
                                            value = rotationUnfreezeDelay.intValue.toFloat(),
                                            onValueChange = { newProgress ->
                                                rotationUnfreezeDelay.intValue =
                                                    newProgress.toInt()
                                                GlobalVars.globalSettings.rotationUnfreezeDelay =
                                                    newProgress.toInt()
                                                ConfigManager.saveConfigWithBinder()
                                            },
                                            valueRange = 1f..15f,
                                            steps = 0,
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp)
                                                .padding(bottom = 12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = !fullyAutomatic.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                val binderFreeze =
                                    remember { mutableStateOf(GlobalVars.globalSettings.binderFreeze) }
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                                    AnimatedVisibility(
                                        label = "BinderFreeze",
                                        visible = !liteMode.value,
                                    ) {
                                        SwitchPreference(
                                            title = stringResource(R.string.binder_freeze),
                                            summary = stringResource(R.string.feature_warning),
                                            checked = binderFreeze.value,
                                            onCheckedChange = {
                                                binderFreeze.value = it
                                                GlobalVars.globalSettings.binderFreeze = it
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }
                                }

                                AnimatedVisibility(
                                    label = "Lite",
                                    visible = !liteMode.value,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        val ignoreXposedModule =
                                            remember { mutableStateOf(GlobalVars.globalSettings.ignoreXposedModule) }
                                        SwitchPreference(
                                            title = stringResource(R.string.ignore_xposed_module),
                                            summary = stringResource(R.string.ignore_xposed_module_desc),
                                            checked = ignoreXposedModule.value,
                                            onCheckedChange = {
                                                ignoreXposedModule.value = it
                                                GlobalVars.globalSettings.ignoreXposedModule = it
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )

                                        val notificationKeep =
                                            remember { mutableStateOf(GlobalVars.globalSettings.notificationKeep) }
                                        SwitchPreference(
                                            title = stringResource(R.string.global_notification_keep),
                                            checked = notificationKeep.value,
                                            onCheckedChange = {
                                                notificationKeep.value = it
                                                GlobalVars.globalSettings.notificationKeep = it
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }
                                }

                                if (isProMode) {
                                    val foregroundLastAppWhenScreenOff =
                                        remember { mutableStateOf(GlobalVars.globalSettings.foregroundLastAppWhenScreenOff) }
                                    SwitchPreference(
                                        title = stringResource(R.string.foreground_last_app_when_screen_off),
                                        checked = foregroundLastAppWhenScreenOff.value,
                                        onCheckedChange = {
                                            foregroundLastAppWhenScreenOff.value = it
                                            GlobalVars.globalSettings.foregroundLastAppWhenScreenOff =
                                                it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )

                                    val backgroundIntent =
                                        remember { mutableStateOf(GlobalVars.globalSettings.backgroundIntent) }
                                    SwitchPreference(
                                        title = stringResource(R.string.background_intent),
                                        checked = backgroundIntent.value,
                                        onCheckedChange = {
                                            backgroundIntent.value = it
                                            GlobalVars.globalSettings.backgroundIntent = it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )
                                }

                                /* if (isDebugMode) {
                                     val safetyFrozen =
                                         remember { mutableStateOf(GlobalVars.globalSettings.safetyFrozen) }
                                     SwitchPreference(
                                         title = stringResource(R.string.safety_frozen),
                                         checked = safetyFrozen.value,
                                         onCheckedChange = {
                                             safetyFrozen.value = it
                                             GlobalVars.globalSettings.safetyFrozen = it
                                             ConfigManager.saveConfigWithBinder()
                                         }
                                     )
                                 }*/

                                if (isProMode) {
                                    val getProviderProcessingMode =
                                        remember { mutableIntStateOf(GlobalVars.globalSettings.getProviderProcessingMode) }
                                    OverlayDropdownPreference(
                                        title = stringResource(R.string.get_provider_process_mode),
                                        items = listOf(
                                            stringResource(R.string.get_provider_process_mode_ignore),
                                            stringResource(R.string.get_provider_process_mode_block),
                                            stringResource(R.string.get_provider_process_mode_unfreeze),
                                            stringResource(R.string.get_provider_process_mode_only_block_documents),
                                            stringResource(R.string.get_provider_process_mode_only_unfreeze_documents)
                                        ),
                                        selectedIndex = getProviderProcessingMode.intValue,
                                        onSelectedIndexChange = {
                                            getProviderProcessingMode.intValue = it
                                            GlobalVars.globalSettings.getProviderProcessingMode = it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )
                                } else {
                                    val allowNonHighAccuracyLocation =
                                        remember { mutableStateOf(GlobalVars.globalSettings.allowNonHighAccuracyLocation) }
                                    SwitchPreference(
                                        title = stringResource(R.string.allow_non_high_accuracy_location),
                                        summary = stringResource(R.string.allow_non_high_accuracy_location_desc),
                                        checked = allowNonHighAccuracyLocation.value,
                                        onCheckedChange = {
                                            allowNonHighAccuracyLocation.value = it
                                            GlobalVars.globalSettings.allowNonHighAccuracyLocation =
                                                it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )
                                }

                                AnimatedVisibility(
                                    label = "Compatible",
                                    visible = !liteMode.value,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        val compatibleMode =
                                            remember { mutableStateOf(GlobalVars.globalSettings.compatibleMode) }
                                        SwitchPreference(
                                            title = stringResource(R.string.compatible_mode),
                                            checked = compatibleMode.value,
                                            onCheckedChange = {
                                                compatibleMode.value = it
                                                GlobalVars.globalSettings.compatibleMode = it
                                                ConfigManager.saveConfigWithBinder()
                                                WindowUtils.showToast(R.string.config_set_after_reboot)
                                            }
                                        )

                                        val useDoze =
                                            remember { mutableStateOf(GlobalVars.globalSettings.useDoze) }
                                        SwitchPreference(
                                            title = stringResource(R.string.use_doze),
                                            checked = useDoze.value,
                                            onCheckedChange = {
                                                useDoze.value = it
                                                GlobalVars.globalSettings.useDoze = it
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }
                                }

                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                                    if (!isProMode) {
                                        val checkForegroundService =
                                            remember { mutableStateOf(GlobalVars.globalSettings.checkForegroundService) }
                                        SwitchPreference(
                                            title = stringResource(R.string.check_foreground_service),
                                            checked = checkForegroundService.value,
                                            onCheckedChange = {
                                                checkForegroundService.value = it
                                                GlobalVars.globalSettings.checkForegroundService =
                                                    it
                                                ConfigManager.saveConfigWithBinder()
                                                WindowUtils.showToast(R.string.config_set_after_reboot)
                                            }
                                        )
                                    }
                                }

                                val disableClearTask =
                                    remember { mutableStateOf(GlobalVars.globalSettings.disableClearTask) }
                                SwitchPreference(
                                    title = stringResource(R.string.disable_clear_task),
                                    checked = disableClearTask.value,
                                    onCheckedChange = {
                                        disableClearTask.value = it
                                        GlobalVars.globalSettings.disableClearTask = it
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )

                                if (!isProMode)
                                    SwitchPreference(
                                        title = stringResource(R.string.lite_mode),
                                        checked = liteMode.value,
                                        onCheckedChange = {
                                            liteMode.value = it
                                            GlobalVars.globalSettings.liteMode = it
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )
                            }
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
