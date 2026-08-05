@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme

@Composable
fun MMSettingsPage(
    active: Boolean,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val navigator = LocalNavigator.current

    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = "Memory \u2192 " + stringResource(R.string.settings),
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

                        val compactMode =
                            remember { mutableIntStateOf(GlobalVars.globalSettings.compactMode) }
                        AnimatedVisibility(
                            label = "CompactMode",
                            visible = memoryCompact.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.compact_mode),
                                    items = listOf(
                                        stringResource(R.string.compact_disable),
                                        stringResource(R.string.compact_anon),
                                        stringResource(R.string.compact_file),
                                        stringResource(R.string.compact_full)
                                    ),
                                    selectedIndex = compactMode.intValue,
                                    onSelectedIndexChange = {
                                        compactMode.intValue = it
                                        GlobalVars.globalSettings.compactMode = it
                                        ConfigManager.saveConfigWithBinder()
                                        WindowUtils.showToast(R.string.config_set_after_reboot)
                                    }
                                )

                                AnimatedVisibility(
                                    label = "CompactDelay",
                                    visible = compactMode.intValue != 0,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    val compactDelay = remember { mutableIntStateOf(GlobalVars.globalSettings.compactDelay) }

                                    Column {
                                        Text(
                                            text = stringResource(R.string.compact_delay) + " | " + compactDelay.intValue + "s",
                                            modifier = Modifier.padding(17.dp)
                                        )

                                        Slider(
                                            value = compactDelay.intValue.toFloat(),
                                            onValueChange = { newProgress ->
                                                compactDelay.intValue = newProgress.toInt()
                                                GlobalVars.globalSettings.compactDelay =
                                                    newProgress.toInt()
                                                ConfigManager.saveConfigWithBinder()
                                            },
                                            valueRange = 0f..30f,
                                            steps = 0,
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp)
                                                .padding(bottom = 12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        val memoryManagement =
                            remember { mutableIntStateOf(GlobalVars.globalSettings.memoryManagementMode) }
                        OverlayDropdownPreference(
                            title = stringResource(R.string.memory_management),
                            items = listOf(
                                stringResource(R.string.memory_management_close),
                                stringResource(R.string.memory_management_freezer),
                                stringResource(R.string.memory_management_compact)
                            ),
                            selectedIndex = memoryManagement.intValue,
                            onSelectedIndexChange = {
                                memoryManagement.intValue = it
                                GlobalVars.globalSettings.memoryManagementMode = it
                                ConfigManager.saveConfigWithBinder()
                                WindowUtils.showToast(R.string.config_set_after_reboot)
                            }
                        )

                        AnimatedVisibility(
                            label = "Threshold",
                            visible = memoryManagement.intValue != 0,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                val keepAllProcessAlive =
                                    remember { mutableStateOf(GlobalVars.globalSettings.keepAllProcessAlive) }
                                SwitchPreference(
                                    title = stringResource(R.string.memory_keep_alive),
                                    checked = keepAllProcessAlive.value,
                                    onCheckedChange = {
                                        keepAllProcessAlive.value = it
                                        GlobalVars.globalSettings.keepAllProcessAlive = it
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )

                                AnimatedVisibility(
                                    label = "KillThreshold",
                                    visible = !keepAllProcessAlive.value,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        val memoryThreshold =
                                            remember { mutableIntStateOf(GlobalVars.globalSettings.memoryThreshold) }

                                        Text(
                                            text = stringResource(R.string.kill_threshold) + " | " + memoryThreshold.intValue + "%",
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

                                val memoryReclaimThreshold =
                                    remember { mutableIntStateOf(GlobalVars.globalSettings.memoryReclaimThreshold) }

                                Text(
                                    text = stringResource(R.string.memory_reclaim) + " " + stringResource(R.string.threshold) + " | " + memoryReclaimThreshold.intValue + "%",
                                    modifier = Modifier.padding(17.dp)
                                )

                                Slider(
                                    value = memoryReclaimThreshold.intValue.toFloat(),
                                    onValueChange = { newProgress ->
                                        memoryReclaimThreshold.intValue = newProgress.toInt()
                                        GlobalVars.globalSettings.memoryReclaimThreshold =
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
