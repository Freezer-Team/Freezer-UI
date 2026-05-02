package nep.timeline.freezerUI.ui.custom

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun CustomBasicComponent(
    modifier: Modifier = Modifier,
    insideMargin: PaddingValues = BasicComponentDefaults.InsideMargin,
    title: String? = null,
    titleColor: Color = MiuixTheme.colorScheme.onSurface,
    subtitle: String? = null,
    subtitleColor: Color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
    summary: String? = null,
    summaryColor: Color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
    leftAction: @Composable (() -> Unit?)? = null,
    rightActions: @Composable RowScope.() -> Unit = {},
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Row(
        modifier = if (onClick != null) {
            modifier
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = interactionSource
                ) {
                    onClick.invoke()
                }
        } else {
            modifier
        }
            .heightIn(min = 56.dp)
            .fillMaxWidth()
            .padding(insideMargin),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        leftAction?.let {
            it()
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            title?.let {
                Text(
                    text = it,
                    style = MiuixTheme.textStyles.title4,
                    color = titleColor
                )
            }
            subtitle?.let {
                Text(
                    text = it,
                    style = MiuixTheme.textStyles.subtitle,
                    color = subtitleColor
                )
            }
            summary?.let {
                Text(
                    text = it,
                    style = MiuixTheme.textStyles.footnote1,
                    color = summaryColor
                )
            }
        }
        Box(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = rightActions
            )
        }
    }
}