package nep.timeline.freezerUI.ui.modules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.preference.SwitchPreference
import java.util.function.Consumer

class SwitchModule(val title: String, val summary: String? = null, val default: Boolean, val onChange: Consumer<Boolean>) :
    IModule {
    @Composable
    override fun Draw() {
        val defaultValue = remember { mutableStateOf(default) }
        SwitchPreference(
            title = title,
            summary = summary,
            checked = defaultValue.value,
            onCheckedChange = {
                if (defaultValue.value != it) {
                    defaultValue.value = it
                    onChange.accept(it)
                }
            }
        )
    }
}