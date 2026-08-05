@file:OptIn(ExperimentalScrollBarApi::class)
package nep.timeline.freezerUI.ui.page

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nep.timeline.freezerUI.ui.modules.IModule
import nep.timeline.freezerUI.ui.modules.ModuleRegister
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezerUI.ui.app.LocalIsWideScreen
import nep.timeline.freezerUI.ui.app.LocalNavigator
import nep.timeline.freezerUI.ui.custom.BackNavigationIcon
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
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme

@Composable
fun ScriptSettingsPage(
    packageName: String? = null,
    userId: String? = null,
    active: Boolean,
    padding: PaddingValues,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val navigator = LocalNavigator.current

    val backdrop = if (packageName != null) rememberBlurBackdrop(GlobalVars.globalSettings.blurUI, true) else rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                AdaptiveTopAppBar(
                    title = "Script \u2192 " + stringResource(R.string.settings),
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
        ScriptSettingsContent(
            packageName = packageName,
            userId = userId,
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
private fun ScriptSettingsContent(
    packageName: String?,
    userId: String?,
    active: Boolean,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
    scrollEndHaptic: Boolean
) {
    val isWideScreen = LocalIsWideScreen.current
    val lazyListState = rememberLazyListState()
    val imageUri = BackgroundManager.currentUri
    val cardBackdrop = if (packageName != null) rememberBlurBackdrop(GlobalVars.globalSettings.blurUI, true) else rememberBlurBackdrop()
    val cardBlurActive = cardBackdrop != null && imageUri != null

    val painter = BackgroundManager.readImage()

    val cardColors = if (cardBlurActive) CardColors(
        color = colorScheme.surfaceContainer.copy(BackgroundManager.cardAlpha),
        contentColor = colorScheme.onSurfaceContainer.copy(BackgroundManager.cardAlpha)
    ) else CardColors(
        color = colorScheme.surfaceContainer,
        contentColor = colorScheme.onSurfaceContainer
    )

    val modulesState = remember {
        mutableStateOf<Map<String, Map<String, LinkedHashSet<IModule>>>>(emptyMap())
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val fetchedMap = ModuleRegister.fetchModules(packageName ?: "Global").toMutableMap()

            if (packageName != null) {
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
            }

            withContext(Dispatchers.Main) {
                modulesState.value = fetchedMap
            }
        }
    }

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
                modulesState.value.forEach { (scriptName, pageMap) ->
                    val validPages = pageMap.filter { it.value.isNotEmpty() }

                    if (validPages.isNotEmpty()) {
                        item(key = "script_$scriptName") {
                            SmallTitle(
                                text = scriptName,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        validPages.forEach { (pageName, modules) ->
                            item(key = "page_${scriptName}_$pageName") {
                                SmallTitle(
                                    text = pageName,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                                )

                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .padding(bottom = 12.dp)
                                        .then(
                                            if (cardBlurActive) {
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
                                            } else Modifier
                                        ),
                                    colors = cardColors
                                ) {
                                    modules.forEach { module ->
                                        key(module) {
                                            module.Draw()
                                        }
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
