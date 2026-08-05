package nep.timeline.freezerUI.ui.modules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer
import kotlin.math.roundToInt

class SliderModule(val title: String, val min: Float, val max: Float, val increment: Float, val default: Float, val onChange: Consumer<Float>) :
    IModule {
    @Composable
    override fun Draw() {
        val defaultValue = remember { mutableFloatStateOf(default) }
        Column {
            Text(
                text = "$title | ${String.format("%.2f", defaultValue.floatValue)}",
                color = MiuixTheme.colorScheme.onSurface,
                modifier = Modifier.padding(17.dp)
            )

            Slider(
                value = defaultValue.floatValue,
                onValueChange = { rawValue ->
                    val steppedValue = if (increment > 0) {
                        ((rawValue / increment).roundToInt() * increment).coerceIn(min, max)
                    } else {
                        rawValue
                    }

                    if (defaultValue.floatValue != steppedValue) {
                        defaultValue.floatValue = steppedValue
                        onChange.accept(steppedValue)
                    }
                },
                valueRange = min..max,
                steps = 0,
                modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 12.dp)
            )
        }
    }
}