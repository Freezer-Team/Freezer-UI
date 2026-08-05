package nep.timeline.freezerUI.ui.modules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import java.util.function.Consumer

class DropdownModule(val title: String, val summary: String? = null, val items: List<String>, val default: Int, val onChange: Consumer<Int>) :
    IModule {
    @Composable
    override fun Draw() {
        val defaultValue = remember { mutableIntStateOf(default) }
        OverlayDropdownPreference(
            title = title,
            summary = summary,
            items = items,
            selectedIndex = defaultValue.intValue,
            onSelectedIndexChange = {
                if (defaultValue.intValue != it) {
                    defaultValue.intValue = it
                    onChange.accept(it)
                }
            }
        )
    }
}