@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.utils.StringUtils
import nep.timeline.freezer.provide.FileBinder
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.custom.BackNavigationIcon
import nep.timeline.freezerUI.ui.custom.CustomInputField
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.BackgroundManager
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.utils.textureBlur
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.Search
import top.yukonga.miuix.kmp.icon.extended.Filter
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FreezerLogPage(
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

    val ls = remember { FileBinder.getInstance().ls(GlobalVars.LOG_DIR).reversed() }

    val logList = remember { mutableStateOf(ls.toList()) }
    val selectedLog = remember { mutableIntStateOf((ls.size - 1).coerceAtLeast(0)) }
    var selectedTypes by remember { mutableStateOf(setOf(0, 1, 2, 3)) }
    val logData = remember { mutableStateOf(emptyList<String>()) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val isActive = remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                isActive.value = true
            else if (event == Lifecycle.Event.ON_STOP)
                isActive.value = false
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isActive.value) {
        while (isActive.value) {
            val (newList, newData) = withContext(Dispatchers.IO) {
                val files = FileBinder.getInstance().ls(GlobalVars.LOG_DIR).reversed().toList()
                if (files.isEmpty()) {
                    files to emptyList<String>()
                } else {
                    val idx = selectedLog.intValue.coerceIn(0, files.lastIndex)
                    val data = FileBinder.getInstance()
                        .readLargeString(GlobalVars.LOG_DIR + "/" + files[idx]).list.reversed()
                    files to data
                }
            }
            logList.value = newList
            logData.value = newData
            delay(1500.milliseconds)
        }
    }

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = stringResource(R.string.log_print),
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        BackNavigationIcon(
                            onClick = { navigator.pop() },
                        )
                    },
                    actions = {
                        Box {
                            val showFilterPopup = remember { mutableStateOf(false) }
                            OverlayListPopup(
                                show = showFilterPopup.value,
                                alignment = PopupPositionProvider.Align.End,
                                onDismissRequest = {
                                    showFilterPopup.value = false
                                },
                                content = {
                                    ListPopupColumn {
                                        val items = listOf(stringResource(R.string.log_debug), stringResource(R.string.log_info), stringResource(R.string.warning), stringResource(R.string.error))
                                        items.forEachIndexed { index, string ->
                                            key(index) {
                                                DropdownImpl(
                                                    text = string,
                                                    optionSize = items.size,
                                                    isSelected = index in selectedTypes,
                                                    onSelectedIndexChange = {
                                                        selectedTypes = if (selectedTypes.contains(index))
                                                            selectedTypes - index
                                                        else
                                                            selectedTypes + index
                                                    },
                                                    index = index
                                                )
                                            }
                                        }
                                    }
                                },
                            )
                            IconButton(
                                onClick = { showFilterPopup.value = true },
                                holdDownState = showFilterPopup.value,
                            ) {
                                Icon(
                                    imageVector = MiuixIcons.Filter,
                                    tint = colorScheme.onSurface,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        val realPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = padding.calculateBottomPadding(),
        )

        val isWideScreen = LocalIsWideScreen.current
        val keyboardController = LocalSoftwareKeyboardController.current
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

        var searchValue by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        // 整段日志的过滤只在 日志/搜索词/类型 变化时重算一次,不再随每次重组、滚动重扫
        val filteredLogs = remember(logData.value, searchValue, selectedTypes) {
            logData.value.filter { data ->
                if (!data.contains(searchValue, ignoreCase = true)) return@filter false

                val info = data.contains("信息 ->") || data.contains("情報 ->") || data.contains("info ->")
                val error = data.contains("错误 ->") || data.contains("間違い ->") || data.contains("error ->")
                val warn = data.contains("警告 ->") || data.contains("警告する ->") || data.contains("warn ->")
                val debug = data.contains("调试 ->") || data.contains("デバッグ ->") || data.contains("debug ->")

                if (info && !selectedTypes.contains(1)) return@filter false
                if (warn && !selectedTypes.contains(2)) return@filter false
                if (error && !selectedTypes.contains(3)) return@filter false
                if (debug && !selectedTypes.contains(0)) return@filter false

                true
            }
        }
        val logCount = filteredLogs.size

        val contentPadding = pageContentPadding(realPadding, realPadding, isWideScreen)
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
                item(key = "appSearch") {
                    SearchBar(
                        modifier = Modifier
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                top = 12.dp,
                                bottom = 6.dp
                            ),
                        inputField = {
                            CustomInputField(
                                query = searchValue,
                                onQueryChange = {
                                    searchValue = it
                                },
                                onSearch = {
                                    keyboardController?.hide()
                                },
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                label = stringResource(R.string.search),
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .padding(start = 12.dp, end = 8.dp)
                                            .size(20.dp)
                                            .alpha(0.4f),
                                        imageVector = MiuixIcons.Basic.Search,
                                        tint = colorScheme.onSurfaceContainer,
                                        contentDescription = "Search"
                                    )
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .then(
                                        if (cardBlurActive) Modifier.textureBlur(
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
                                            ),
                                            enabled = true
                                        ) else Modifier
                                    ),
                                color = if (cardBlurActive) Color.Transparent else colorScheme.surfaceContainerHigh
                            )
                        },
                        outsideEndAction = {
                            Text(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clickable(
                                        interactionSource = null,
                                        indication = null,
                                    ) {
                                        expanded = false

                                    },
                                text = stringResource(R.string.cancel),
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = colorScheme.primary,
                            )
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                    }
                }
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
                            OverlayDropdownPreference(
                                title = stringResource(R.string.log_file),
                                items = logList.value,
                                selectedIndex = selectedLog.intValue,
                                onSelectedIndexChange = {
                                    selectedLog.intValue = it
                                }
                            )
                        }
                    }
                }

                if (!filteredLogs.isEmpty())
                    items(
                        count = logCount,
                        key = { "log_$it" }) { i ->
                        val data = filteredLogs[i]
                        if (!data.contains(searchValue, true))
                            return@items

                        val language = if (data.contains("信息 ->") || data.contains("错误 ->") || data.contains("警告 ->") || data.contains("调试 ->")) 0 else (if (data.contains("情報 ->") || data.contains("間違い ->") || data.contains("警告する ->") || data.contains("デバッグ ->")) 1 else 2)

                        val info = data.contains("信息 ->") || data.contains("情報 ->") || data.contains("info ->")
                        val error = data.contains("错误 ->") || data.contains("間違い ->") || data.contains("error ->")
                        val warn = data.contains("警告 ->") || data.contains("警告する ->") || data.contains("warn ->")
                        val debug = data.contains("调试 ->") || data.contains("デバッグ ->") || data.contains("debug ->")

                        val timeAndType = StringUtils.getSubString(data, "", " -> ").split(" ")
                        if (timeAndType.size < 3)
                            return@items
                        val logInfo = StringUtils.getSubString(data, " -> ", System.lineSeparator())

                        val date = timeAndType[0]
                        val time = timeAndType[1]
                        val type = timeAndType[2]

                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
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
                                ),
                            colors = cardColors
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(width = 15.dp, height = 15.dp)
                                            .background(
                                                color = if (debug) Color(0xFF8DFD74) else if (warn) Color(
                                                    0xFFEFF842
                                                ) else if (error) Color(0xFFFD7484) else Color(
                                                    0xFF74D7FD
                                                ),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    ) {
                                        Text(
                                            text = if (debug) "D" else if (warn) "W" else if (error) "E" else "I",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = type,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "$date $time",
                                        fontSize = 10.sp,
                                        color = colorScheme.onSurfaceVariantSummary
                                    )
                                }

                                Text(
                                    text = logInfo,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
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
}
