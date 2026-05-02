@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import nep.timeline.freezerUI.MainActivity.AppListViewModelSingleton.appListViewModel
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.utils.AdaptiveTopAppBar
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.pageContentPadding
import nep.timeline.freezerUI.ui.utils.pageScrollModifiers
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.viewModel.AppItemCompose
import nep.timeline.freezerUI.ui.viewModel.AppListViewModel
import nep.timeline.freezerUI.ui.viewModel.FrozenAppItemCompose
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.Search
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.shapes.SmoothUnevenRoundedCornerShape
import top.yukonga.miuix.kmp.theme.LocalDismissState
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import top.yukonga.miuix.kmp.window.WindowListPopup

private val AppListTopShape = SmoothUnevenRoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
private val AppListBottomShape = SmoothUnevenRoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)

@Composable
fun TopBarActions() {
    val showTopPopup = remember { mutableStateOf(false) }
    val topPopupHoldDown = remember { mutableStateOf(false) }
    val type = appListViewModel.type.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current
    IconButton(
        onClick = {
            showTopPopup.value = true
            topPopupHoldDown.value = true
        },
        holdDownState = topPopupHoldDown.value,
    ) {
        Icon(
            imageVector = MiuixIcons.More,
            contentDescription = "WindowListPopup",
            tint = colorScheme.onBackground,
        )
    }
    WindowListPopup(
        show = showTopPopup.value,
        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
        alignment = PopupPositionProvider.Align.TopEnd,
        onDismissRequest = {
            showTopPopup.value = false
        },
        onDismissFinished = {
            topPopupHoldDown.value = false
        },
        content = {
            val state = LocalDismissState.current
            val items = stringArrayResource(R.array.dropdownOptions)
            ListPopupColumn {
                items.forEachIndexed { index, string ->
                    key(index) {
                        DropdownImpl(
                            text = string,
                            optionSize = items.size,
                            isSelected = type.value == index,
                            onSelectedIndexChange = { selectedIdx ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                                appListViewModel.updateByQuery(type = selectedIdx)
                                state?.invoke()
                            },
                            index = index
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun AppPage(
    viewModel: AppListViewModel,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val scrollBehavior = MiuixScrollBehavior()
    val isWideScreen = LocalIsWideScreen.current

    val appList = viewModel.cacheFilterApps.collectAsState()
    val filteredApps = remember { derivedStateOf { appList } }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchValue = viewModel.search.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val type = viewModel.type.collectAsState()
    val updatedApps = viewModel.updatedApps.collectAsState()
    val isLoading = viewModel.filterApps.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val isActive = remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.update()
            while (!updatedApps.value)
                delay(500)
            isRefreshing = false
        }
    }

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
        if (type.value != 2)
            appListViewModel.getFilterApps()
        while (isActive.value) {
            if (type.value == 2)
                appListViewModel.getFilterApps()
            delay(1500)
        }
    }

    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, scrollBehavior) {
                AdaptiveTopAppBar(
                    title = stringResource(R.string.app_list),
                    isWideScreen = isWideScreen,
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    actions = {
                        TopBarActions()
                    }
                )
            }
        },
        popupHost = {},
    ) { innerPadding ->
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            AnimatedVisibility(
                visible = isLoading.value.isNotEmpty() && updatedApps.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PullToRefresh(
                    isRefreshing = isRefreshing,
                    onRefresh = { isRefreshing = true },
                    pullToRefreshState = pullToRefreshState,
                    topAppBarScrollBehavior = scrollBehavior,
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 12.dp,
                        bottom = if (isWideScreen) {
                            WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        } else {
                            0.dp
                        }
                    ),
                ) {
                    val lazyListState = rememberLazyListState()
                    val contentPadding =
                        pageContentPadding(innerPadding, padding, isWideScreen, extraTop = 12.dp)
                    Box {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.pageScrollModifiers(
                                scrollEndHaptic,
                                true,
                                scrollBehavior,
                            ),
                            contentPadding = contentPadding,
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
                                            InputField(
                                                query = searchValue.value,
                                                onQueryChange = {
                                                    viewModel.updateByQuery(it, type.value)
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
                                                }
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
                                                    viewModel.updateByQuery("", type.value)
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

                                SmallTitle(
                                    text = when (type.value) {
                                        2 -> stringResource(R.string.frozen_info)
                                        else -> stringResource(R.string.app_info)
                                    }
                                )
                            }

                            val appCount = filteredApps.value.value.size
                            items(
                                count = appCount,
                                key = { "app_$it" }) { i ->
                                AnimatedVisibility(
                                    visible = filteredApps.value.value.isNotEmpty() && updatedApps.value,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    if (appCount == 1) {
                                        Card(modifier = Modifier.padding(horizontal = 12.dp)) {
                                            if (type.value == 2 && updatedApps.value)
                                                FrozenAppItemCompose(filteredApps.value.value[i])
                                            else
                                                AppItemCompose(filteredApps.value.value[i], type.value)
                                        }
                                    } else {
                                        val isFirst = i == 0
                                        val isLast = i == appCount - 1
                                        val shape = when {
                                            isFirst -> AppListTopShape
                                            isLast -> AppListBottomShape
                                            else -> RectangleShape
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp)
                                                .clip(shape)
                                                .background(colorScheme.surfaceContainer),
                                        ) {
                                            if (type.value == 2 && updatedApps.value)
                                                FrozenAppItemCompose(filteredApps.value.value[i])
                                            else
                                                AppItemCompose(filteredApps.value.value[i], type.value)
                                        }
                                    }
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
            }

            AnimatedVisibility(
                visible = isLoading.value.isEmpty() || !updatedApps.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        InfiniteProgressIndicator(
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically),
                        )
                    }
                }
            }
        }
    }
}