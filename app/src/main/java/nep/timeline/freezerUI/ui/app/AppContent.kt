package nep.timeline.freezerUI.ui.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavDisplayTransitionEffects
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.kyant.backdrop.backdrops.layerBackdrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import nep.timeline.freezerUI.MainActivity.AppListViewModelSingleton.appListViewModel
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.ui.custom.FloatingBottomBar
import nep.timeline.freezerUI.ui.custom.FloatingBottomBarItem
import nep.timeline.freezerUI.ui.navigation3.Navigator
import nep.timeline.freezerUI.ui.navigation3.Route
import nep.timeline.freezerUI.ui.page.AboutPage
import nep.timeline.freezerUI.ui.page.AppPage
import nep.timeline.freezerUI.ui.page.BatterySettingsPage
import nep.timeline.freezerUI.ui.page.FreezerSettingsPage
import nep.timeline.freezerUI.ui.page.InfoPage
import nep.timeline.freezerUI.ui.page.MMSettingsPage
import nep.timeline.freezerUI.ui.page.PUSHSettingsPage
import nep.timeline.freezerUI.ui.page.SettingsPage
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.BlurredBar
import nep.timeline.freezerUI.ui.utils.rememberBlurBackdrop
import nep.timeline.freezerUI.ui.utils.shouldShowSplitPane
import nep.timeline.freezerUI.ui.utils.textureBlur
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarDisplayMode
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarItem
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarDisplayMode
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.NavigationRail
import top.yukonga.miuix.kmp.basic.NavigationRailDisplayMode
import top.yukonga.miuix.kmp.basic.NavigationRailItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.isRenderEffectSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.icon.extended.HorizontalSplit
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import kotlin.math.abs

private object UIConstants {
    val MAIN_PAGE_INDEX = 0
    var APP_PAGE_INDEX = 1
    var SETTINGS_PAGE_INDEX = 2
    var PAGE_COUNT = 3

    val PAGE_TITLES = listOf(AppContext.context.getString(R.string.main), AppContext.context.getString(R.string.app_list), AppContext.context.getString(R.string.settings))
}

enum class FloatingNavigationBarAlignment(val value: Int) {
    Center(0),
    Start(1),
    End(2),
    ;

    companion object {
        fun fromInt(value: Int) = entries.find { it.value == value } ?: Center
    }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> { error("No navigator found!") }
val LocalIsWideScreen = staticCompositionLocalOf { false }
val LocalMainPagerState = staticCompositionLocalOf<MainPagerState> { error("LocalMainPagerState not provided") }

@Composable
fun AppContent(
    active: Boolean,
    padding: PaddingValues,
) {
    if (!active) {
        UIConstants.PAGE_COUNT = 2
        UIConstants.APP_PAGE_INDEX = 2
        UIConstants.SETTINGS_PAGE_INDEX = 1
    }

    val appState = LocalAppState.current
    val pagerState = rememberPagerState(pageCount = { UIConstants.PAGE_COUNT })
    val mainPagerState = rememberMainPagerState(pagerState)
    LaunchedEffect(mainPagerState.pagerState.currentPage) {
        mainPagerState.syncPage()
    }

    val backStack = remember { mutableStateListOf<NavKey>().apply { add(Route.Main) } }
    val navigator = remember { Navigator(backStack) }

    val navigationItems = remember {
        if (active)
            listOf(
                NavigationItem(UIConstants.PAGE_TITLES[0], MiuixIcons.HorizontalSplit),
                NavigationItem(UIConstants.PAGE_TITLES[1], MiuixIcons.File),
                NavigationItem(UIConstants.PAGE_TITLES[2], MiuixIcons.Settings)
            )
        else
            listOf(
                NavigationItem(UIConstants.PAGE_TITLES[0], MiuixIcons.HorizontalSplit),
                NavigationItem(UIConstants.PAGE_TITLES[2], MiuixIcons.Settings)
            )
    }

    MainScreenBackHandler(mainPagerState, navigator)

    val isWideScreen = shouldShowSplitPane()

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalMainPagerState provides mainPagerState,
        LocalIsWideScreen provides isWideScreen
    ) {
        val entryProvider = remember(backStack) {
            entryProvider<NavKey> {
                entry<Route.Main> {
                    Home(
                        padding = padding,
                        navigationItems = navigationItems,
                        mainPagerState = mainPagerState
                    )
                }

                entry<Route.About> {
                    AboutPage(padding = padding)
                }

                entry<Route.FreezerSettings> {
                    FreezerSettingsPage(
                        active = active,
                        padding = padding,
                        scrollEndHaptic = appState.enableScrollEndHaptic
                    )
                }

                entry<Route.PUSHSettings> {
                    PUSHSettingsPage(
                        active = active,
                        padding = padding,
                        scrollEndHaptic = appState.enableScrollEndHaptic
                    )
                }

                entry<Route.MMSettings> {
                    MMSettingsPage(
                        active = active,
                        padding = padding,
                        scrollEndHaptic = appState.enableScrollEndHaptic
                    )
                }

                entry<Route.BatterySettings> {
                    BatterySettingsPage(
                        active = active,
                        padding = padding,
                        scrollEndHaptic = appState.enableScrollEndHaptic
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
            appState.enableCornerClip,
            appState.enableDim,
            appState.blockInputDuringTransition,
            appState.popDirectionFollowsSwipeEdge,
        ) {
            NavDisplayTransitionEffects(
                enableCornerClip = appState.enableCornerClip,
                dimAmount = if (appState.enableDim) 0.5f else 0f,
                blockInputDuringTransition = appState.blockInputDuringTransition,
                popDirectionFollowsSwipeEdge = appState.popDirectionFollowsSwipeEdge,
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
private fun Home(
    padding: PaddingValues,
    navigationItems: List<NavigationItem>,
    mainPagerState: MainPagerState
) {
    val isWideScreen = LocalIsWideScreen.current
    val layoutDirection = LocalLayoutDirection.current
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            if (isWideScreen) {
                SnackbarHost(state = snackbarHostState)
            }
        },
    ) {
        if (isWideScreen) {
            WideScreenContent(
                navigationItems = navigationItems,
                snackbarHostState = snackbarHostState,
                layoutDirection = layoutDirection,
                mainPagerState = mainPagerState,
            )
        } else {
            CompactScreenLayout(
                navigationItems = navigationItems,
                snackbarHostState = snackbarHostState,
                padding = padding,
                mainPagerState = mainPagerState
            )
        }
    }
}

@Composable
private fun WideScreenContent(
    navigationItems: List<NavigationItem>,
    snackbarHostState: SnackbarHostState,
    layoutDirection: LayoutDirection,
    mainPagerState: MainPagerState,
) {
    val page = mainPagerState.selectedPage
    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null && isRenderEffectSupported()
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface
    Row {
        BlurredBar(backdrop, blurActive) {
            NavigationRail(
                modifier = Modifier.background(barColor),
                color = barColor,
                mode = NavigationRailDisplayMode.IconWithSelectedLabel,
            ) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationRailItem(
                        selected = page == index,
                        onClick = { mainPagerState.animateToPage(index) },
                        icon = item.icon,
                        label = item.label,
                    )
                }
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            contentWindowInsets =
                WindowInsets.systemBars.union(
                    WindowInsets.displayCutout.exclude(
                        WindowInsets.displayCutout.only(WindowInsetsSides.Start),
                    ),
                )
        ) { padding ->
            Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
                AppPager(
                    snackbarHostState = snackbarHostState,
                    padding = PaddingValues(top = padding.calculateTopPadding()),
                    pagerState = mainPagerState,
                    modifier = Modifier
                        .imePadding()
                        .padding(end = padding.calculateEndPadding(layoutDirection)),
                )
            }
        }
    }
}

@Composable
private fun CompactScreenLayout(
    navigationItems: List<NavigationItem>,
    snackbarHostState: SnackbarHostState,
    padding: PaddingValues,
    mainPagerState: MainPagerState
) {
    val appState = LocalAppState.current
    val surfaceColor = colorScheme.surface
    val backdrop = rememberLayerBackdrop {
        drawRect(surfaceColor)
        drawContent()
    }
    val kyantBackdrop = com.kyant.backdrop.backdrops.rememberLayerBackdrop {
        drawRect(surfaceColor)
        drawContent()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                navigationItems = navigationItems,
                mainPagerState = mainPagerState,
                backdrop = backdrop,
                kyantBackdrop = kyantBackdrop,
                modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
            )
        },
        snackbarHost = {
            SnackbarHost(state = snackbarHostState)
        },
    ) { innerPadding ->
        Box(modifier = if (appState.navigationStyle == 2) Modifier.layerBackdrop(kyantBackdrop) else (if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)) {
            AppPager(
                snackbarHostState = snackbarHostState,
                padding = innerPadding,
                pagerState = mainPagerState,
                modifier = Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        start = padding.calculateStartPadding(LocalLayoutDirection.current),
                        end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    )
                    .imePadding(),
            )
        }
    }
}

@Composable
private fun NavigationBar(
    navigationItems: List<NavigationItem>,
    mainPagerState: MainPagerState,
    backdrop: LayerBackdrop?,
    kyantBackdrop: com.kyant.backdrop.backdrops.LayerBackdrop,
    modifier: Modifier = Modifier
) {
    val appState = LocalAppState.current
    val blurActive = backdrop != null && isRenderEffectSupported() && appState.blur
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface
    val page = mainPagerState.selectedPage
    AnimatedVisibility(
        visible = appState.navigationStyle == 0,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (blurActive) {
                        Modifier.textureBlur(
                            backdrop = backdrop,
                            colors = BlurColors(
                                blendColors = listOf(
                                    BlendColorEntry(color = colorScheme.surface.copy(0.8f)),
                                ),
                            )
                        )
                    } else {
                        Modifier
                    }
                )
                .background(barColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                )
                .then(modifier),
        ) {
            NavigationBar(
                color = barColor,
                mode = NavigationBarDisplayMode.IconAndText,
            ) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = page == index,
                        onClick = { mainPagerState.animateToPage(index) },
                        icon = item.icon,
                        label = item.label,
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = appState.navigationStyle == 1,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Box(
            modifier = modifier
        ) {
            FloatingNavigationBar(
                modifier = Modifier
                    .then(
                        if (blurActive) {
                            Modifier.textureBlur(
                                backdrop = backdrop,
                                colors = BlurColors(
                                    blendColors = listOf(
                                        BlendColorEntry(color = colorScheme.surface.copy(0.8f)),
                                    ),
                                )
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(barColor)
                    .then(modifier),
                mode = FloatingNavigationBarDisplayMode.IconAndText,
                horizontalAlignment = FloatingNavigationBarAlignment.Center
                    .toAlignment(),
            ) {
                navigationItems.forEachIndexed { index, item ->
                    FloatingNavigationBarItem(
                        selected = page == index,
                        onClick = { mainPagerState.animateToPage(index) },
                        icon = item.icon,
                        label = item.label,
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = appState.navigationStyle == 2,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!isRenderEffectSupported()) FloatingNavigationBar(
                mode = FloatingNavigationBarDisplayMode.IconAndText,
                horizontalAlignment = FloatingNavigationBarAlignment.Center
                    .toAlignment(),
            ) {
                navigationItems.forEachIndexed { index, item ->
                    FloatingNavigationBarItem(
                        selected = page == index,
                        onClick = { mainPagerState.animateToPage(index) },
                        icon = item.icon,
                        label = item.label,
                    )
                }
            }
            else FloatingBottomBar(
                modifier = modifier.align(Alignment.BottomCenter)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    )
                    .padding(bottom = 12.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                selectedIndex = { mainPagerState.selectedPage },
                onSelected = { mainPagerState.animateToPage(it) },
                backdrop = kyantBackdrop,
                tabsCount = navigationItems.size,
                isBlurEnabled = true,
            ) {
                navigationItems.forEachIndexed { index, item ->
                    FloatingBottomBarItem(
                        onClick = {
                            mainPagerState.animateToPage(index)
                        },
                        modifier = Modifier.defaultMinSize(minWidth = 76.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = colorScheme.onSurface
                        )
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            color = colorScheme.onSurface,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }
        }
    }
}

private fun FloatingNavigationBarAlignment.toAlignment(): Alignment.Horizontal = when (this) {
    FloatingNavigationBarAlignment.Center -> CenterHorizontally
    FloatingNavigationBarAlignment.Start -> Alignment.Start
    FloatingNavigationBarAlignment.End -> Alignment.End
}

@Composable
fun AppPager(
    snackbarHostState: SnackbarHostState,
    padding: PaddingValues,
    pagerState: MainPagerState,
    modifier: Modifier = Modifier,
) {
    val appState = LocalAppState.current
    HorizontalPager(
        state = pagerState.pagerState,
        modifier = modifier,
        userScrollEnabled = appState.enablePageUserScroll,
        verticalAlignment = Alignment.Top,
        pageContent = { page ->
            when (page) {
                UIConstants.MAIN_PAGE_INDEX -> InfoPage(
                    callback = { pagerState.animateToPage(it) },
                    padding = padding,
                    scrollEndHaptic = appState.enableScrollEndHaptic
                )

                UIConstants.APP_PAGE_INDEX -> AppPage(
                    viewModel = appListViewModel,
                    padding = padding,
                    scrollEndHaptic = appState.enableScrollEndHaptic
                )

                UIConstants.SETTINGS_PAGE_INDEX -> SettingsPage(
                    active = UIConstants.SETTINGS_PAGE_INDEX == 2,
                    padding = padding,
                    scrollEndHaptic = appState.enableScrollEndHaptic
                )
            }
        }
    )
}

@Composable
private fun MainScreenBackHandler(
    mainState: MainPagerState,
    navigator: Navigator,
) {
    val isPagerBackHandlerEnabled by remember {
        derivedStateOf {
            navigator.current() is Route.Main && navigator.backStackSize() == 1 && mainState.selectedPage != 0
        }
    }

    val navEventState = rememberNavigationEventState(NavigationEventInfo.None)

    NavigationBackHandler(
        state = navEventState,
        isBackEnabled = isPagerBackHandlerEnabled,
        onBackCompleted = {
            mainState.animateToPage(0)
        },
    )
}

@Stable
class MainPagerState(
    val pagerState: PagerState,
    private val coroutineScope: CoroutineScope,
) {
    var selectedPage by mutableIntStateOf(pagerState.currentPage)
        private set

    var isNavigating by mutableStateOf(false)
        private set

    private var navJob: Job? = null

    fun animateToPage(targetIndex: Int) {
        if (targetIndex == selectedPage) return

        navJob?.cancel()

        selectedPage = targetIndex
        isNavigating = true

        navJob = coroutineScope.launch {
            val myJob = coroutineContext.job
            try {
                pagerState.scroll(MutatePriority.UserInput) {
                    val distance = abs(targetIndex - pagerState.currentPage).coerceAtLeast(2)
                    val duration = 100 * distance + 100
                    val layoutInfo = pagerState.layoutInfo
                    val pageSize = layoutInfo.pageSize + layoutInfo.pageSpacing
                    val currentDistanceInPages = targetIndex - pagerState.currentPage - pagerState.currentPageOffsetFraction
                    val scrollPixels = currentDistanceInPages * pageSize

                    var previousValue = 0f
                    animate(
                        initialValue = 0f,
                        targetValue = scrollPixels,
                        animationSpec = tween(easing = EaseInOut, durationMillis = duration),
                    ) { currentValue, _ ->
                        previousValue += scrollBy(currentValue - previousValue)
                    }
                }

                if (pagerState.currentPage != targetIndex) {
                    pagerState.scrollToPage(targetIndex)
                }
            } finally {
                if (navJob == myJob) {
                    isNavigating = false
                    if (pagerState.currentPage != targetIndex) {
                        selectedPage = pagerState.currentPage
                    }
                }
            }
        }
    }

    fun syncPage() {
        if (!isNavigating && selectedPage != pagerState.currentPage) {
            selectedPage = pagerState.currentPage
        }
    }
}

@Composable
fun rememberMainPagerState(
    pagerState: PagerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MainPagerState = remember(pagerState, coroutineScope) {
    MainPagerState(pagerState, coroutineScope)
}