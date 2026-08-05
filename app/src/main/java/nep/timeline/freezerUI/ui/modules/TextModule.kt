package nep.timeline.freezerUI.ui.modules

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer

class TextModule(val title: String, val onClick: Consumer<Int>) : IModule {
    @Composable
    override fun Draw() {
        Text(
            text = title,
            color = MiuixTheme.colorScheme.onSurface,
            modifier = Modifier.padding(17.dp).pointerInput(UInt) {
                detectTapGestures(
                    onDoubleTap = {
                        onClick.accept(1)
                    },
                    onLongPress = {
                        onClick.accept(3)
                    },
                    onPress = {
                        onClick.accept(2)
                    },
                    onTap = {
                        onClick.accept(0)
                    }
                )
            }
        )
    }
}