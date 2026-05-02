package nep.timeline.freezerUI.ui.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import nep.timeline.freezerUI.GlobalVars

@Composable
fun App(
    active: Boolean,
    padding: PaddingValues = PaddingValues(0.dp)
) {
    var appState by if (GlobalVars.globalSettings != null) remember { mutableStateOf(AppState(navigationStyle = GlobalVars.globalSettings.navigationStyle, blur = GlobalVars.globalSettings.blurUI)) } else remember { mutableStateOf(AppState()) }
    val updateAppState: ((AppState) -> AppState) -> Unit = remember {
        { transform -> appState = transform(appState) }
    }

    AppTheme(
        smoothRounding = false,
    ) {
        CompositionLocalProvider(
            LocalAppState provides appState,
            LocalUpdateAppState provides updateAppState,
        ) {
            AppContent(active, padding)
        }
    }
}