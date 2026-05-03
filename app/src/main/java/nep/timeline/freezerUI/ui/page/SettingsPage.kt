@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nep.timeline.freezer.provide.DataBinder
import nep.timeline.freezer.provide.FileBinder
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.app.LocalUpdateAppState
import nep.timeline.freezerUI.ui.navigation3.Route
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.BackupUtils
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.UserUtils
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.viewModel.LoginCardView
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.isRenderEffectSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.math.min

@Composable
fun SettingsPage(
    active: Boolean,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = stringResource(R.string.settings),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor
                )
            }
        },
    ) { innerPadding ->
        SettingsContent(
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
private fun SettingsContent(
    active: Boolean,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val updateAppState = LocalUpdateAppState.current
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current
    //val imageUri = BackgroundManager.currentUri

    val fileBinder = FileBinder.getInstance()
    UserUtils.init()
    val userData = remember { mutableStateOf(UserUtils.userData) }
    val state = remember { mutableIntStateOf(UserUtils.state) }

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
            item(key = "user") {
                if (fileBinder != null) {
                    LoginCardView(state, userData)
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(top = 12.dp, bottom = 6.dp),
                        insideMargin = PaddingValues(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(start = 6.dp),
                                imageVector = Icons.Rounded.ErrorOutline,
                                tint = MiuixTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                            Column(modifier = Modifier.padding(start = 24.dp)) {
                                Text(
                                    text = "404 NOT FOUND",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.not_active)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            if (active) {
                item(key = "settingsUi") {
                    SmallTitle(text = stringResource(R.string.settings))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                    ) {
                        ArrowPreference(
                            title = "Freezer -> " + stringResource(R.string.settings),
                            onClick = {
                                navigator.push(Route.FreezerSettings)
                            }
                        )

                        if (!SettingsConfigChecker.isFullyAutomatic()) {
                            ArrowPreference(
                                title = "Memory -> " + stringResource(R.string.settings),
                                onClick = {
                                    navigator.push(Route.MMSettings)
                                }
                            )

                            ArrowPreference(
                                title = "Battery -> " + stringResource(R.string.settings),
                                onClick = {
                                    navigator.push(Route.BatterySettings)
                                }
                            )
                        }

                        ArrowPreference(
                            title = "PUSH -> " + stringResource(R.string.settings),
                            onClick = {
                                navigator.push(Route.PUSHSettings)
                            }
                        )

                        val proMode =
                            remember { mutableStateOf(GlobalVars.globalSettings.proMode) }
                        AnimatedVisibility(
                            label = "ProMode",
                            visible = proMode.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            SwitchPreference(
                                title = stringResource(R.string.pro_mode),
                                checked = proMode.value,
                                onCheckedChange = {
                                    proMode.value = it
                                    GlobalVars.globalSettings.proMode = it
                                    ConfigManager.saveConfigWithBinder()
                                    if (!it)
                                        AppContext.playSmile()
                                }
                            )
                        }
                    }
                }
                item(key = "settingsLog") {
                    SmallTitle(text = stringResource(R.string.log_print))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                    ) {
                        val logPrintMode =
                            remember { mutableIntStateOf(GlobalVars.globalSettings.logPrintMode) }
                        OverlayDropdownPreference(
                            title = stringResource(R.string.log_print),
                            items = listOf(
                                stringResource(R.string.log_xposed),
                                stringResource(R.string.log_file),
                                stringResource(R.string.log_close)
                            ),
                            selectedIndex = logPrintMode.intValue,
                            onSelectedIndexChange = {
                                logPrintMode.intValue = it
                                GlobalVars.globalSettings.logPrintMode = it
                                ConfigManager.saveConfigWithBinder()
                            }
                        )

                        AnimatedVisibility(
                            label = "LogPrint",
                            visible = logPrintMode.intValue != 2,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                val logLevelMode =
                                    remember { mutableIntStateOf(GlobalVars.globalSettings.logLevelMode) }
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.log_level),
                                    items = listOf(
                                        stringResource(R.string.log_info),
                                        stringResource(R.string.log_debug)
                                    ),
                                    selectedIndex = logLevelMode.intValue,
                                    onSelectedIndexChange = {
                                        logLevelMode.intValue = it
                                        GlobalVars.globalSettings.logLevelMode = it
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )

                                val logLanguageMode =
                                    remember { mutableIntStateOf(GlobalVars.globalSettings.logLanguageMode) }
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.log_language),
                                    items = listOf(
                                        stringResource(R.string.log_english),
                                        stringResource(R.string.log_simplified_chinese),
                                        stringResource(R.string.log_japanese)
                                    ),
                                    selectedIndex = logLanguageMode.intValue,
                                    onSelectedIndexChange = {
                                        logLanguageMode.intValue = it
                                        GlobalVars.globalSettings.logLanguageMode = it
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )
                            }
                        }
                    }
                }
                item(key = "settingsConfig") {
                    SmallTitle(text = stringResource(R.string.config))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                    ) {
                        ArrowPreference(
                            title = stringResource(R.string.backup_config),
                            summary = stringResource(R.string.backup_config_desc),
                            onClick = { BackupUtils.backup(BackupUtils.activity) }
                        )

                        ArrowPreference(
                            title = stringResource(R.string.restore_config),
                            summary = stringResource(R.string.restore_config_desc),
                            onClick = { BackupUtils.restore(BackupUtils.activity) }
                        )

                        ArrowPreference(
                            title = stringResource(R.string.refresh_config),
                            summary = stringResource(R.string.refresh_config_desc),
                            onClick = {
                                if (DataBinder.getInstance().get("Refresh_Config")
                                        .equals("DONE", true)
                                )
                                    ConfigManager.readConfigWithBinder()
                            }
                        )
                    }
                }
            }
            item(key = "settingsPersonalization") {
                /*val context = LocalContext.current
            val getContent = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri: Uri? ->
                    if (uri != null) {
                        val savedUri = saveImageToAppDirectory(context, uri)
                        if (savedUri != null)
                            BackgroundManager.refresh(savedUri)
                    }
                }
            )*/
                SmallTitle(text = stringResource(R.string.personalization))
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp),
                ) {
                    //ArrowPreference(
                    //    title = stringResource(R.string.custom_background),
                    //    summary = stringResource(R.string.custom_background_desc),
                    //    onClick = { getContent.launch("image/*") }
                    //)

                    /*AnimatedVisibility(visible = imageUri != null) {
                    ArrowPreference(
                        title = stringResource(R.string.remove_background),
                        summary = stringResource(R.string.remove_background_desc),
                        onClick = {
                            BackgroundManager.remove()
                        }
                    )
                }*/

                    val navigationStyle =
                        remember {
                            mutableIntStateOf(
                                if (isRenderEffectSupported()) GlobalVars.globalSettings.navigationStyle else min(
                                    GlobalVars.globalSettings.navigationStyle,
                                    1
                                )
                            )
                        }
                    OverlayDropdownPreference(
                        title = stringResource(R.string.navigation_style),
                        items = if (isRenderEffectSupported()) listOf(
                            stringResource(R.string.normal),
                            stringResource(R.string.floating),
                            stringResource(R.string.apple_floating)
                        ) else listOf(
                            stringResource(R.string.normal),
                            stringResource(R.string.floating)
                        ),
                        selectedIndex = navigationStyle.intValue,
                        onSelectedIndexChange = {
                            navigationStyle.intValue = it
                            GlobalVars.globalSettings.navigationStyle = it
                            updateAppState { state -> state.copy(navigationStyle = it) }
                            ConfigManager.saveConfigWithBinder()
                        }
                    )

                    if (isRenderEffectSupported()) {
                        val blurUi =
                            remember { mutableStateOf(GlobalVars.globalSettings.blurUI) }
                        SwitchPreference(
                            title = stringResource(R.string.blur_ui),
                            summary = stringResource(R.string.blur_ui_desc),
                            checked = blurUi.value,
                            onCheckedChange = {
                                blurUi.value = it
                                GlobalVars.globalSettings.blurUI = it
                                ConfigManager.saveConfigWithBinder()
                                updateAppState { state -> state.copy(blur = it) }
                            }
                        )
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

/*fun saveImageToAppDirectory(context: Context, imageUri: Uri): Uri? {
    try {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val file = File(context.filesDir, "background.jpg")
        if (file.exists())
            file.delete()

        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            outputStream.write(buffer, 0, length)
        }
        inputStream.close()
        outputStream.close()

        return Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}*/