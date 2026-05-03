@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.PauseCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nep.timeline.freezerUI.BuildConfig
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.MainActivity.AppListViewModelSingleton.appListViewModel
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.configs.ConfigManager
import nep.timeline.freezerUI.configs.checkers.BinderModeChecker
import nep.timeline.freezerUI.utils.VersionUtils
import nep.timeline.freezer.provide.DataBinder
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.navigation3.Route
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.WindowUtils
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.isDynamicColor
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import java.text.SimpleDateFormat
import java.util.Date

fun getImg(id: Int): Drawable? {
    return AppCompatResources.getDrawable(AppContext.context, id)
}

var clickNum = 0
var lastClickTime = 0L
val fool = SimpleDateFormat("MMdd").format(Date()).equals("0401")

@Composable
fun InfoPage(
    callback: (Int) -> Unit,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = stringResource(R.string.app_name),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor
                )
            }
        },
    ) { innerPadding ->
        InfoContent(
            padding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            backdrop = backdrop,
            scrollEndHaptic = scrollEndHaptic,
            callback = callback
        )
    }
}

@Composable
private fun InfoContent(
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
    scrollEndHaptic: Boolean,
    callback: (Int) -> Unit
) {
    val isWideScreen = LocalIsWideScreen.current
    val lazyListState = rememberLazyListState()
    val contentPadding = pageContentPadding(padding, padding, isWideScreen)

    Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.pageScrollModifiers(
                scrollEndHaptic,
                true,
                topAppBarScrollBehavior,
            ),
            contentPadding = contentPadding,
        ) {
            item {
                val dataBinder = DataBinder.getInstance()
                if (dataBinder == null) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        WarningCard(stringResource(R.string.not_active))
                        StatusCard(
                            active = false,
                            working = false,
                            version = stringResource(R.string.unknown),
                            onClickStatus = {

                            },
                            onClickWhitelist = {
                                appListViewModel.updateByQuery(type = 0)
                                callback(1)
                            },
                            onClickBlacklist = {
                                appListViewModel.updateByQuery(type = 1)
                                callback(1)
                            }
                        )
                        InfoCard(false)
                        LearnMoreCard()
                    }
                    return@item
                }

                val version = dataBinder.get("Version")
                Column(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    val rekernelStatus = dataBinder.get("Re_Kernel_Status")
                    val nkbinderStatus = dataBinder.get("Nk_Binder_Status")
                    val warningCount = dataBinder.get("WARNING_COUNT").toInt()
                    val errorCount = dataBinder.get("ERROR_COUNT").toInt()

                    if (fool)
                        WarningCard(stringResource(R.string.fools_day))
                    if (BinderModeChecker.getBinderMode() == BinderModeChecker.BinderMode.KERNEL && rekernelStatus == "IDLE")
                        WarningCard(stringResource(R.string.rekernel_connect_error))
                    if (BinderModeChecker.getBinderMode() == BinderModeChecker.BinderMode.EBPF && nkbinderStatus == "IDLE")
                        WarningCard(stringResource(R.string.nkbinder_connect_error))
                    if (errorCount > 0)
                        WarningCard(stringResource(R.string.internal_error))
                    StatusCard(
                        active = true,
                        working = true,
                        version = version,
                        onClickStatus = {

                        },
                        onClickWhitelist = {
                            appListViewModel.updateByQuery(type = 0)
                            callback(1)
                        },
                        onClickBlacklist = {
                            appListViewModel.updateByQuery(type = 1)
                            callback(1)
                        }
                    )
                    InfoCard(true)
                    LearnMoreCard()
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }

        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(lazyListState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            trackPadding = contentPadding,
        )
    }
}

@Composable
private fun StatusCard(
    modifier: Modifier = Modifier,
    active: Boolean,
    working: Boolean,
    version: String,
    onClickStatus: () -> Unit = {},
    onClickWhitelist: () -> Unit = {},
    onClickBlacklist: () -> Unit = {},
    alpha: Boolean = false,
    colors: CardColors = CardDefaults.defaultColors()
) {
    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.defaultColors(
                    color = when {
                        isDynamicColor -> if (alpha) colorScheme.secondaryContainer.copy(0.35f) else colorScheme.secondaryContainer
                        isSystemInDarkTheme() -> if (working && !fool) (if (alpha) Color(0xFF1A3825).copy(0.35f) else Color(0xFF1A3825)) else (if (alpha) Color(0xFF381A1A).copy(0.35f) else Color(0xFF381A1A))
                        else -> if (working && !fool) (if (alpha) Color(0xFFDFFAE4).copy(0.35f) else Color(0xFFDFFAE4)) else (if (alpha) Color(0xFFFADFDF).copy(0.35f) else Color(0xFFFADFDF))
                    }
                ),
                onClick = {
                    onClickStatus()
                },
                showIndication = true,
                pressFeedbackType = if (alpha) PressFeedbackType.None else PressFeedbackType.Tilt
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(38.dp, 45.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(
                            modifier = Modifier.size(170.dp),
                            imageVector = if (working) if (fool) Icons.Rounded.PauseCircleOutline else Icons.Rounded.CheckCircleOutline else Icons.Rounded.ErrorOutline,
                            tint = if (isDynamicColor) {
                                colorScheme.primary.copy(0.8f)
                            } else {
                                if (working && !fool) Color(0xFF36D167) else Color(0xFFD13636)
                            },
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 16.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (working) if (fool) stringResource(R.string.crying) else stringResource(R.string.working) else stringResource(R.string.error),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.version) + ": " + version,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    insideMargin = PaddingValues(16.dp),
                    onClick = { onClickWhitelist() },
                    showIndication = true,
                    pressFeedbackType = if (alpha) PressFeedbackType.None else PressFeedbackType.Tilt,
                    colors = colors
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.white_app),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = colorScheme.onSurfaceVariantSummary,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (!active || GlobalVars.applicationSettings == null || fool) "N/A" else GlobalVars.applicationSettings.whiteApps.size.toString(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface,
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    insideMargin = PaddingValues(16.dp),
                    onClick = { onClickBlacklist() },
                    showIndication = true,
                    pressFeedbackType = if (alpha) PressFeedbackType.None else PressFeedbackType.Tilt,
                    colors = colors
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.black_app),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = colorScheme.onSurfaceVariantSummary,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (!active || GlobalVars.applicationSettings == null || fool) "N/A" else GlobalVars.applicationSettings.blackSystemApps.size.toString(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WarningCard(
    message: String,
    modifier: Modifier = Modifier,
    alpha: Boolean = false,
    color: Color? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = {
            onClick?.invoke()
        },
        colors = CardDefaults.defaultColors(
            color = color ?: when {
                isDynamicColor -> if (alpha) colorScheme.errorContainer.copy(0.35f) else colorScheme.errorContainer
                isSystemInDarkTheme() -> if (alpha) Color(0XFF310808).copy(0.35f) else Color(0XFF310808)
                else -> if (alpha) Color(0XFF310808).copy(0.35f) else Color(0xFFF8E2E2)
            }
        ),
        modifier = modifier,
        showIndication = onClick != null,
        pressFeedbackType = if (alpha) PressFeedbackType.None else PressFeedbackType.Tilt
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = message,
                color = if (isDynamicColor) colorScheme.onErrorContainer else Color(0xFFF72727),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun LearnMoreCard(modifier: Modifier = Modifier, colors: CardColors = CardDefaults.defaultColors()) {
    val navigator = LocalNavigator.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = colors
    ) {
        ArrowPreference(
            title = stringResource(R.string.home_about_freezer),
            onClick = {
                navigator.push(Route.About)
            }
        )
    }
}

@Composable
private fun InfoCard(working: Boolean, modifier: Modifier = Modifier, colors: CardColors = CardDefaults.defaultColors()) {
    @Composable
    fun InfoText(
        title: String,
        content: String,
        bottomPadding: Dp = 24.dp,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = title,
            fontSize = MiuixTheme.textStyles.headline1.fontSize,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onSurface,
            modifier = modifier
        )
        Text(
            text = content,
            fontSize = MiuixTheme.textStyles.body2.fontSize,
            color = colorScheme.onSurfaceVariantSummary,
            modifier = modifier.padding(top = 2.dp, bottom = bottomPadding)
        )
    }
    Card(colors = colors, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            InfoText(
                title = stringResource(R.string.manager_version),
                content = "v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + "-" + BuildConfig.BUILD_TIME + ")",
                modifier = Modifier.pointerInput(UInt) {
                    detectTapGestures(
                        onTap = {
                            if (!GlobalVars.globalSettings.proMode && !GlobalVars.globalSettings.liteMode) {
                                val curTime = System.currentTimeMillis()
                                if (((curTime - lastClickTime) / 1000) > 2)
                                    clickNum = 0
                                clickNum++
                                lastClickTime = curTime
                                if (clickNum > 5) {
                                    clickNum = 0
                                    AppContext.playCry()
                                    WindowUtils.showToast("专业模式已开启")
                                    GlobalVars.globalSettings.proMode = true
                                    ConfigManager.saveConfigWithBinder()
                                }
                            }
                        }
                    )
                }
            )
            InfoText(
                title = stringResource(R.string.hook_type),
                content = if (working) DataBinder.getInstance().get("Hook_Type") else stringResource(R.string.unknown)
            )
            InfoText(
                title = stringResource(R.string.android_version),
                content = if (Build.VERSION.PREVIEW_SDK_INT != 0) (Build.VERSION.CODENAME + " Preview (API " + Build.VERSION.PREVIEW_SDK_INT + "/" + Build.VERSION.SDK_INT + ")") else (VersionUtils.getAndroidVersion() + " (API " + Build.VERSION.SDK_INT + ")")
            )
            InfoText(
                title = stringResource(R.string.xposed_version),
                content = if (working) DataBinder.getInstance().get("Xposed_API_Version_Code") else stringResource(R.string.unknown)
            )
            InfoText(
                title = stringResource(R.string.system_fingerprint),
                content = Build.FINGERPRINT,
                bottomPadding = 0.dp
            )
        }
    }
}