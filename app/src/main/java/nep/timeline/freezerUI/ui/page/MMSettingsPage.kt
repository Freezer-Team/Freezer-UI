@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

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
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.ConfigManager
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
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MMSettingsPage(
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
                    title = "Memory -> " + stringResource(R.string.settings),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor
                )
            }
        },
    ) { innerPadding ->
        MMSettingsContent(
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
private fun MMSettingsContent(
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
                        if (SettingsConfigChecker.isProMode()) {
                            val computeOOM =
                                remember { mutableStateOf(GlobalVars.globalSettings.computeOOM) }
                            SwitchPreference(
                                title = stringResource(R.string.compute_oom),
                                checked = computeOOM.value,
                                onCheckedChange = {
                                    computeOOM.value = it
                                    GlobalVars.globalSettings.computeOOM = it
                                    ConfigManager.saveConfigWithBinder()
                                    WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            )
                        }
                        if (!SettingsConfigChecker.isFullyAutomatic()) {
                            val memoryCompact =
                                remember { mutableStateOf(GlobalVars.globalSettings.memoryCompact) }
                            SwitchPreference(
                                title = stringResource(R.string.memory_compact),
                                checked = memoryCompact.value,
                                onCheckedChange = {
                                    memoryCompact.value = it
                                    GlobalVars.globalSettings.memoryCompact = it
                                    ConfigManager.saveConfigWithBinder()
                                    WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            )

                            val memoryManagement =
                                remember { mutableStateOf(GlobalVars.globalSettings.memoryManagement) }
                            SwitchPreference(
                                title = stringResource(R.string.memory_management),
                                checked = memoryManagement.value,
                                onCheckedChange = {
                                    memoryManagement.value = it
                                    GlobalVars.globalSettings.memoryManagement = it
                                    ConfigManager.saveConfigWithBinder()
                                    WindowUtils.showToast(R.string.config_set_after_reboot)
                                }
                            )

                            AnimatedVisibility(
                                label = "Threshold",
                                visible = memoryManagement.value,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column {
                                    val memoryThreshold =
                                        remember { mutableIntStateOf(GlobalVars.globalSettings.memoryThreshold) }

                                    Text(
                                        text = stringResource(R.string.threshold) + " | " + memoryThreshold.intValue + "%",
                                        modifier = Modifier.padding(17.dp)
                                    )

                                    Slider(
                                        value = memoryThreshold.intValue.toFloat(),
                                        onValueChange = { newProgress ->
                                            memoryThreshold.intValue = newProgress.toInt()
                                            GlobalVars.globalSettings.memoryThreshold =
                                                newProgress.toInt()
                                            ConfigManager.saveConfigWithBinder()
                                        },
                                        valueRange = 0f..100f,
                                        steps = 0,
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp)
                                            .padding(bottom = 12.dp)
                                    )
                                }
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
