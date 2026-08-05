@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui

import android.os.Build
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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavDisplayTransitionEffects
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import nep.timeline.freezer.provide.FrozenBinder
import nep.timeline.freezerUI.ui.modules.ModuleRegister
import nep.timeline.freezer.ui.navigation3.ApplicationRoute
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.SubActivity
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.checkers.BatteryOptModeChecker
import nep.timeline.freezerUI.configs.checkers.BinderModeChecker
import nep.timeline.freezerUI.configs.checkers.SettingsConfigChecker
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.custom.BackNavigationIcon
import nep.timeline.freezerUI.ui.navigation3.Navigator
import nep.timeline.freezerUI.ui.page.ScriptSettingsPage
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.BackgroundManager
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.utils.shouldShowSplitPane
import nep.timeline.freezerUI.ui.utils.textureBlur
import nep.timeline.freezerUI.utils.PackageUtils
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
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme

@Composable
fun ApplicationHome(activity: SubActivity) {
    val scrollBehavior = MiuixScrollBehavior()
    val isWideScreen = shouldShowSplitPane()

    val appName = activity.intent.getStringExtra("appName")!!
    val packageName = activity.intent.getStringExtra("packageName")!!
    var userId = activity.intent.getStringExtra("userId")
    if (userId == null)
        userId = "0"

    val serializersModule = remember {
        SerializersModule {
            polymorphic(NavKey::class) {
                subclass(ApplicationRoute.Main::class)
                subclass(ApplicationRoute.ScriptSettings::class)
            }
        }
    }

    val savedStateConfig = remember(serializersModule) {
        SavedStateConfiguration {
            this.serializersModule = serializersModule
        }
    }

    val backStack = rememberNavBackStack(
        configuration = savedStateConfig,
        ApplicationRoute.Main,
    )
    val navigator = remember { Navigator(backStack) }

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = backStack.size > 1,
        onBackCompleted = {
            navigator.pop()
        },
    )

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalIsWideScreen provides isWideScreen
    ) {
        val entryProvider = remember(backStack) {
            entryProvider<NavKey> {
                entry<ApplicationRoute.Main> {
                    AppHomeMainContent(
                        activity = activity,
                        appName = appName,
                        packageName = packageName,
                        userId = userId,
                        scrollBehavior = scrollBehavior,
                        isWideScreen = isWideScreen,
                    )
                }
                entry<ApplicationRoute.ScriptSettings> {
                    ScriptSettingsPage(
                        packageName = packageName,
                        userId = userId,
                        active = true,
                        padding = PaddingValues(0.dp),
                        scrollEndHaptic = true
                    )
                }
            }
        }

        val entries = rememberDecoratedNavEntries(
            backStack = backStack,
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            entryProvider = entryProvider,
        )

        val transitionEffects = remember(
            true,
            true,
            true,
            false,
        ) {
            NavDisplayTransitionEffects(
                enableCornerClip = true,
                dimAmount = 0.5f,
                blockInputDuringTransition = true,
                popDirectionFollowsSwipeEdge = false,
            )
        }

        NavDisplay(
            entries = entries,
            onBack = { navigator.pop() },
            transitionEffects = transitionEffects,
        )
    }
}

@Composable
private fun AppHomeMainContent(
    activity: SubActivity,
    appName: String,
    packageName: String,
    userId: String,
    scrollBehavior: ScrollBehavior,
    isWideScreen: Boolean
) {
    val backdrop = rememberBlurBackdrop(GlobalVars.globalSettings.blurUI, true)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface

    val imageUri = BackgroundManager.currentUri
    val painter = BackgroundManager.readImage()

    val cardBackdrop = rememberBlurBackdrop(GlobalVars.globalSettings.blurUI, true)
    val cardBlurActive = cardBackdrop != null && imageUri != null

    val cardColors = if (cardBlurActive) CardColors(
        color = colorScheme.surfaceContainer.copy(BackgroundManager.cardAlpha),
        contentColor = colorScheme.onSurfaceContainer.copy(BackgroundManager.cardAlpha)
    ) else CardColors(
        color = colorScheme.surfaceContainer,
        contentColor = colorScheme.onSurfaceContainer
    )

    val navigator = LocalNavigator.current

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, scrollBehavior) {
                AdaptiveTopAppBar(
                    title = appName,
                    isWideScreen = isWideScreen,
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        BackNavigationIcon(
                            onClick = { activity.finish() }
                        )
                    }
                )
            }
        }
    ) { padding ->
        val lazyListState = rememberLazyListState()
        val contentPadding = pageContentPadding(padding, padding, isWideScreen)

        Box(
            modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
        ) {
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
                    enableScrollEndHaptic = true,
                    showTopAppBar = true,
                    topAppBarScrollBehavior = scrollBehavior,
                ),
                contentPadding = contentPadding
            ) {
                item(key = "applicationUi") {
                    val appInfo =
                        PackageUtils.getProcessSet(AppContext.context, packageName, userId)
                            ?: return@item

                    val key = PackageUtils.toConfigKey(userId, packageName, true)
                    val keys = HashSet<String>()

                    if (SettingsConfigChecker.isCompatibleMode()) {
                        for (keyCurrent in PackageUtils.getPackagesForUidBinder(
                            AppContext.context,
                            appInfo.packageInfo.applicationInfo!!.uid
                        )) {
                            val info =
                                keyCurrent.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            val pkgName = info[0]
                            keys.add(PackageUtils.toConfigKey(userId, pkgName, true))
                        }
                    } else {
                        keys.add(key)
                    }
                    Card(
                        modifier = Modifier
                            .padding(12.dp)
                            .then(
                                if (cardBlurActive)
                                    Modifier
                                        .clip(RoundedCornerShape(CardDefaults.CornerRadius))
                                        .textureBlur(
                                            backdrop = cardBackdrop,
                                            blurRadius = BackgroundManager.cardBlurRadius,
                                            colors = BlurColors(
                                                blendColors = listOf(
                                                    BlendColorEntry(
                                                        color = colorScheme.surface.copy(
                                                            BackgroundManager.cardBlurAlpha
                                                        )
                                                    ),
                                                ),
                                            )
                                        )
                                else Modifier
                            ),
                        colors = cardColors
                    ) {
                        val modules = ModuleRegister.fetchModules(packageName)
                        val hasModule = modules.values.any { pageMap ->
                            pageMap.values.any { moduleSet ->
                                moduleSet.isNotEmpty()
                            }
                        }
                        if (hasModule) {
                            ArrowPreference(
                                title = "Script \u2192 " + stringResource(R.string.settings),
                                onClick = {
                                    navigator.push(ApplicationRoute.ScriptSettings)
                                }
                            )
                        } else {
                            val fetchedMap = ModuleRegister.fetchModules(packageName).toMutableMap()

                            val withUserIdModules = ModuleRegister.fetchModules("$packageName#$userId")

                            for ((script, pageMap) in withUserIdModules) {
                                val existingPageMap = fetchedMap.getOrPut(script) { LinkedHashMap() }.toMutableMap()
                                existingPageMap.putAll(pageMap)
                                fetchedMap[script] = existingPageMap
                            }

                            val anyModules = ModuleRegister.fetchModules("ANY")

                            for ((script, pageMap) in anyModules) {
                                val existingPageMap = fetchedMap.getOrPut(script) { LinkedHashMap() }.toMutableMap()
                                existingPageMap.putAll(pageMap)
                                fetchedMap[script] = existingPageMap
                            }

                            val hasModule = fetchedMap.values.any { pageMap ->
                                pageMap.values.any { moduleSet ->
                                    moduleSet.isNotEmpty()
                                }
                            }

                            if (hasModule) {
                                ArrowPreference(
                                    title = "Script \u2192 " + stringResource(R.string.settings),
                                    onClick = {
                                        navigator.push(ApplicationRoute.ScriptSettings)
                                    }
                                )
                            }
                        }

                        val isProMode = SettingsConfigChecker.isProMode()
                        val isFullyAutomaticMode = SettingsConfigChecker.isFullyAutomatic()
                        val isLiteMode = SettingsConfigChecker.isLiteMode()

                        val blackOrWhite =
                            remember { mutableStateOf(if (appInfo.system) appInfo.black else appInfo.white) }
                        SwitchPreference(
                            title = if (appInfo.system) stringResource(R.string.system_black) else stringResource(
                                R.string.user_white
                            ),
                            checked = blackOrWhite.value,
                            onCheckedChange = {
                                blackOrWhite.value = it
                                if (it) {
                                    if (appInfo.system) {
                                        GlobalVars.applicationSettings.blackSystemApps.addAll(keys)
                                        GlobalVars.applicationSettings.idleApps.removeAll(keys)
                                        FrozenBinder.getInstance().freezer(packageName, userId.toInt())
                                    } else {
                                        GlobalVars.applicationSettings.whiteApps.addAll(keys)
                                        FrozenBinder.getInstance().thaw(packageName, userId.toInt())
                                    }
                                } else if (appInfo.system) {
                                    GlobalVars.applicationSettings.blackSystemApps.removeAll(keys)
                                    FrozenBinder.getInstance().thaw(packageName, userId.toInt())
                                } else {
                                    GlobalVars.applicationSettings.whiteApps.removeAll(keys)
                                    GlobalVars.applicationSettings.idleApps.removeAll(keys)
                                    FrozenBinder.getInstance().freezer(packageName, userId.toInt())
                                }

                                ConfigManager.saveConfigWithBinder()
                            }
                        )

                        if (BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.FULL || BatteryOptModeChecker.getBatteryOptMode() == BatteryOptModeChecker.BatteryOptMode.AUTO) {
                            val idle = remember { mutableStateOf(appInfo.idle) }
                            SwitchPreference(
                                title = if (if (appInfo.system) !appInfo.black else appInfo.white) stringResource(
                                    R.string.allow_battery_opt
                                ) else stringResource(R.string.no_battery_opt),
                                checked = idle.value,
                                onCheckedChange = {
                                    idle.value = it
                                    if (it)
                                        GlobalVars.applicationSettings.idleApps.addAll(keys)
                                    else
                                        GlobalVars.applicationSettings.idleApps.removeAll(keys)
                                    ConfigManager.saveConfigWithBinder()
                                }
                            )
                        }

                        AnimatedVisibility(
                            label = "White",
                            visible = if (appInfo.system) blackOrWhite.value else !blackOrWhite.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically() + shrinkVertically(),
                        ) {
                            Column {
                                val socket = remember { mutableStateOf(appInfo.socket) }
                                if (!isFullyAutomaticMode) {
                                    SwitchPreference(
                                        title = stringResource(R.string.keep_net),
                                        checked = socket.value,
                                        onCheckedChange = {
                                            socket.value = it
                                            if (it)
                                                GlobalVars.applicationSettings.socketApps.addAll(
                                                    keys
                                                )
                                            else
                                                GlobalVars.applicationSettings.socketApps.removeAll(
                                                    keys
                                                )
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )

                                    AnimatedVisibility(
                                        label = "Socket",
                                        visible = socket.value,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically() + shrinkVertically(),
                                    ) {
                                        val netReceive =
                                            remember { mutableStateOf(appInfo.netReceive) }
                                        SwitchPreference(
                                            title = stringResource(R.string.netreceive_unfreeze),
                                            checked = netReceive.value,
                                            onCheckedChange = {
                                                netReceive.value = it
                                                if (it)
                                                    GlobalVars.applicationSettings.netReceiveApps.addAll(
                                                        keys
                                                    )
                                                else
                                                    GlobalVars.applicationSettings.netReceiveApps.removeAll(
                                                        keys
                                                    )
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }

                                    val networkCheck =
                                        remember { mutableStateOf(appInfo.networkCheck) }
                                    SwitchPreference(
                                        title = stringResource(R.string.network_check),
                                        checked = networkCheck.value,
                                        onCheckedChange = {
                                            networkCheck.value = it
                                            if (it)
                                                GlobalVars.applicationSettings.networkCheckApps.addAll(
                                                    keys
                                                )
                                            else
                                                GlobalVars.applicationSettings.networkCheckApps.removeAll(
                                                    keys
                                                )
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )

                                    val intervalUnfreeze =
                                        remember { mutableStateOf(appInfo.intervalUnfreeze) }
                                    val intervalUnfreezeDelay =
                                        remember { mutableIntStateOf(appInfo.intervalUnfreezeDelay) }
                                    SwitchPreference(
                                        title = stringResource(R.string.interval_unfreeze),
                                        checked = intervalUnfreeze.value,
                                        onCheckedChange = {
                                            intervalUnfreeze.value = it
                                            if (it)
                                                keys.forEach { key ->
                                                    GlobalVars.applicationSettings.intervalUnfreezeApps[key] =
                                                        10
                                                    intervalUnfreezeDelay.intValue = 10
                                                }
                                            else
                                                keys.forEach { key ->
                                                    GlobalVars.applicationSettings.intervalUnfreezeApps.remove(
                                                        key
                                                    )
                                                }
                                            ConfigManager.saveConfigWithBinder()
                                        }
                                    )

                                    AnimatedVisibility(
                                        label = "Interval",
                                        visible = intervalUnfreeze.value,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically() + shrinkVertically(),
                                    ) {
                                        Column {
                                            Text(
                                                text = stringResource(R.string.interval_unfreeze_delay) + " | " + intervalUnfreezeDelay.intValue + " Min",
                                                modifier = Modifier.padding(17.dp)
                                            )
                                            Slider(
                                                value = intervalUnfreezeDelay.intValue.toFloat(),
                                                onValueChange = { newProgress ->
                                                    intervalUnfreezeDelay.intValue =
                                                        newProgress.toInt()
                                                    keys.forEach { key ->
                                                        GlobalVars.applicationSettings.intervalUnfreezeApps[key] =
                                                            newProgress.toInt()
                                                    }
                                                    ConfigManager.saveConfigWithBinder()
                                                },
                                                valueRange = 0f..60f,
                                                steps = 0,
                                                modifier = Modifier
                                                    .padding(horizontal = 12.dp)
                                                    .padding(bottom = 12.dp)
                                            )
                                        }
                                    }

                                    if (isProMode) {
                                        val backgroundPlay = remember { mutableStateOf(appInfo.backgroundPlay) }
                                        SwitchPreference(
                                            title = stringResource(R.string.background_play),
                                            checked = backgroundPlay.value,
                                            onCheckedChange = {
                                                backgroundPlay.value = it
                                                if (it)
                                                    GlobalVars.applicationSettings.backgroundPlayApps.addAll(keys)
                                                else
                                                    GlobalVars.applicationSettings.backgroundPlayApps.removeAll(keys)
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )

                                        val backgroundIntent =
                                            remember { mutableStateOf(appInfo.backgroundIntent) }
                                        SwitchPreference(
                                            title = if (GlobalVars.globalSettings.backgroundIntent) stringResource(
                                                R.string.background_intent_ignore_process
                                            ) else stringResource(R.string.background_intent_process),
                                            checked = backgroundIntent.value,
                                            onCheckedChange = {
                                                backgroundIntent.value = it
                                                if (it)
                                                    GlobalVars.applicationSettings.backgroundIntentApps.addAll(
                                                        keys
                                                    )
                                                else
                                                    GlobalVars.applicationSettings.backgroundIntentApps.removeAll(
                                                        keys
                                                    )
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }

                                    if (!isLiteMode) {
                                        val notificationKeep =
                                            remember { mutableStateOf(appInfo.notificationKeep) }
                                        SwitchPreference(
                                            title = if (GlobalVars.globalSettings.notificationKeep) stringResource(
                                                R.string.notification_keep_ignore
                                            ) else stringResource(R.string.notification_keep),
                                            checked = notificationKeep.value,
                                            onCheckedChange = {
                                                notificationKeep.value = it
                                                if (it)
                                                    GlobalVars.applicationSettings.notificationKeepApps.addAll(
                                                        keys
                                                    )
                                                else
                                                    GlobalVars.applicationSettings.notificationKeepApps.removeAll(
                                                        keys
                                                    )
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }

                                    if (isProMode) {
                                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                                            val locationCheck =
                                                remember { mutableIntStateOf(appInfo.locationCheck) }
                                            OverlayDropdownPreference(
                                                title = stringResource(R.string.location_check),
                                                items = listOf(
                                                    stringResource(R.string.location_close),
                                                    stringResource(R.string.location_only_high_accuracy),
                                                    stringResource(R.string.location_only_lower_accuracy),
                                                    stringResource(R.string.location_all)
                                                ),
                                                selectedIndex = locationCheck.intValue,
                                                onSelectedIndexChange = {
                                                    locationCheck.intValue = it
                                                    when (it) {
                                                        0 -> for (curKey in keys) GlobalVars.applicationSettings.locationCheckApps.remove(
                                                            curKey
                                                        )

                                                        else -> for (curKey in keys) GlobalVars.applicationSettings.locationCheckApps[curKey] =
                                                            it
                                                    }
                                                    ConfigManager.saveConfigWithBinder()
                                                }
                                            )
                                        } else {
                                            val locationCheck =
                                                remember { mutableStateOf(appInfo.locationCheck == 1) }
                                            SwitchPreference(
                                                title = stringResource(R.string.location_check),
                                                checked = locationCheck.value,
                                                onCheckedChange = {
                                                    locationCheck.value = it
                                                    if (it)
                                                        for (curKey in keys)
                                                            GlobalVars.applicationSettings.locationCheckApps[curKey] =
                                                                1
                                                    else for (curKey in keys)
                                                        GlobalVars.applicationSettings.locationCheckApps.remove(
                                                            curKey
                                                        )
                                                    ConfigManager.saveConfigWithBinder()
                                                }
                                            )
                                        }

                                        val ignoreRecording =
                                            remember { mutableStateOf(appInfo.ignoreRecording) }
                                        SwitchPreference(
                                            title = stringResource(R.string.ignore_recording),
                                            checked = ignoreRecording.value,
                                            onCheckedChange = {
                                                ignoreRecording.value = it
                                                if (it)
                                                    GlobalVars.applicationSettings.ignoreRecordingApps.addAll(
                                                        keys
                                                    )
                                                else
                                                    GlobalVars.applicationSettings.ignoreRecordingApps.removeAll(
                                                        keys
                                                    )
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }
                                }
                                val bluetoothCheck =
                                    remember { mutableStateOf(appInfo.bluetoothCheck) }
                                SwitchPreference(
                                    title = stringResource(R.string.bluetooth_check),
                                    checked = bluetoothCheck.value,
                                    onCheckedChange = {
                                        bluetoothCheck.value = it
                                        if (it)
                                            GlobalVars.applicationSettings.bluetoothCheckApps.addAll(
                                                keys
                                            )
                                        else
                                            GlobalVars.applicationSettings.bluetoothCheckApps.removeAll(
                                                keys
                                            )
                                        ConfigManager.saveConfigWithBinder()
                                    }
                                )
                                if (!isFullyAutomaticMode) {
                                    if (isProMode) {
                                        if (BinderModeChecker.getBinderMode() != BinderModeChecker.BinderMode.DISABLE) {
                                            val ignoreBinder =
                                                remember { mutableStateOf(appInfo.ignoreBinder) }
                                            SwitchPreference(
                                                title = stringResource(R.string.ignore_binder),
                                                checked = ignoreBinder.value,
                                                onCheckedChange = {
                                                    ignoreBinder.value = it
                                                    if (it)
                                                        GlobalVars.applicationSettings.ignoreBinderApps.addAll(
                                                            keys
                                                        )
                                                    else
                                                        GlobalVars.applicationSettings.ignoreBinderApps.removeAll(
                                                            keys
                                                        )
                                                    ConfigManager.saveConfigWithBinder()
                                                }
                                            )
                                        }

                                        /*if (GlobalVars.globalSettings.computeOOM) {
                                            val oomLevel =
                                                remember { mutableIntStateOf(appInfo.oomLevel) }
                                            OverlayDropdownPreference(
                                                title = stringResource(R.string.oom_level),
                                                items = listOf(
                                                    stringResource(R.string.low),
                                                    stringResource(R.string.medium),
                                                    stringResource(R.string.high)
                                                ),
                                                selectedIndex = oomLevel.intValue,
                                                onSelectedIndexChange = {
                                                    oomLevel.intValue = it
                                                    when (it) {
                                                        0 -> for (curKey in keys) GlobalVars.applicationSettings.oomPriorityApps.remove(
                                                            curKey
                                                        )

                                                        else -> for (curKey in keys) GlobalVars.applicationSettings.oomPriorityApps[curKey] =
                                                            it
                                                    }
                                                    ConfigManager.saveConfigWithBinder()
                                                }
                                            )
                                        }*/

                                        val backgroundLevel =
                                            remember { mutableIntStateOf(appInfo.backgroundLevel) }
                                        OverlayDropdownPreference(
                                            title = stringResource(R.string.background_level),
                                            items = listOf(
                                                stringResource(R.string.top_app),
                                                stringResource(R.string.direct_app),
                                                stringResource(R.string.foreground_service)
                                            ),
                                            selectedIndex = backgroundLevel.intValue,
                                            onSelectedIndexChange = {
                                                backgroundLevel.intValue = it
                                                when (it) {
                                                    0 -> for (curKey in keys) GlobalVars.applicationSettings.backgroundLevelApps.remove(
                                                        curKey
                                                    )

                                                    else -> for (curKey in keys) GlobalVars.applicationSettings.backgroundLevelApps[curKey] =
                                                        it
                                                }
                                                ConfigManager.saveConfigWithBinder()
                                            }
                                        )
                                    }

                                    if (!isLiteMode)
                                        for (process in appInfo.processSet) {
                                            if (process.split("#").size == 1) {
                                                val processKeyTitle =
                                                    PackageUtils.toConfigKey(userId, process, false)
                                                val processKey =
                                                    PackageUtils.toConfigKey(userId, process, true)

                                                val processSelected = remember {
                                                    mutableIntStateOf(
                                                        if (appInfo.killProcessSet.contains(
                                                                processKey
                                                            )
                                                        ) 1 else if (appInfo.whiteProcessSet.contains(
                                                                processKey
                                                            )
                                                        ) 2 else 0
                                                    )
                                                }
                                                OverlayDropdownPreference(
                                                    title = processKeyTitle,
                                                    items = listOf(
                                                        stringResource(R.string.freeze),
                                                        stringResource(R.string.kill),
                                                        stringResource(R.string.white_app)
                                                    ),
                                                    selectedIndex = processSelected.intValue,
                                                    onSelectedIndexChange = {
                                                        processSelected.intValue = it
                                                        when (it) {
                                                            0 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.remove(
                                                                    processKey
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.remove(
                                                                    processKey
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                                FrozenBinder.getInstance()
                                                                    .freezer(
                                                                        packageName,
                                                                        userId.toInt()
                                                                    )
                                                            }

                                                            1 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.remove(
                                                                    processKey
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.add(
                                                                    processKey
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                            }

                                                            2 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.add(
                                                                    processKey
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.remove(
                                                                    processKey
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                                FrozenBinder.getInstance()
                                                                    .temporaryUnfreeze(
                                                                        packageName,
                                                                        userId.toInt(),
                                                                        "CONFIG_UPDATED"
                                                                    )
                                                            }
                                                        }
                                                        ConfigManager.saveConfigWithBinder()
                                                    }
                                                )
                                            } else {
                                                val processSelected = remember {
                                                    mutableIntStateOf(
                                                        if (appInfo.killProcessSet.contains(process)) 1 else if (appInfo.whiteProcessSet.contains(
                                                                process
                                                            )
                                                        ) 2 else 0
                                                    )
                                                }
                                                OverlayDropdownPreference(
                                                    title = process,
                                                    items = listOf(
                                                        stringResource(R.string.freeze),
                                                        stringResource(R.string.kill),
                                                        stringResource(R.string.white_app)
                                                    ),
                                                    selectedIndex = processSelected.intValue,
                                                    onSelectedIndexChange = {
                                                        processSelected.intValue = it
                                                        when (it) {
                                                            0 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.remove(
                                                                    process
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.remove(
                                                                    process
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                                FrozenBinder.getInstance()
                                                                    .freezer(
                                                                        packageName,
                                                                        userId.toInt()
                                                                    )
                                                            }

                                                            1 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.remove(
                                                                    process
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.add(
                                                                    process
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                            }

                                                            2 -> {
                                                                GlobalVars.applicationSettings.whiteProcessList.add(
                                                                    process
                                                                )
                                                                GlobalVars.applicationSettings.killProcessList.remove(
                                                                    process
                                                                )
                                                                ConfigManager.saveConfigWithBinder()
                                                                FrozenBinder.getInstance()
                                                                    .temporaryUnfreeze(
                                                                        packageName,
                                                                        userId.toInt(),
                                                                        "CONFIG_UPDATED"
                                                                    )
                                                            }
                                                        }
                                                        ConfigManager.saveConfigWithBinder()
                                                    }
                                                )
                                            }
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
}