package nep.timeline.freezerUI.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nep.timeline.freezerUI.utils.StringUtils
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Folder
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun HourlyCard(cardModifier: Modifier = Modifier, cardColors: CardColors = CardDefaults.defaultColors(), modifier: Modifier = Modifier, stateData: Map<String, String>?) {
    Card(cardModifier, colors = cardColors) {
        Column(
            modifier = modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = MiuixIcons.Folder,
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurfaceContainer
                )
                Text(
                    text = "小时级别信息",
                    color = MiuixTheme.colorScheme.onSurfaceContainer, style = TextStyle(
                        fontWeight = FontWeight.W700,
                        color = MiuixTheme.colorScheme.onSurface
                    ),
                )
            }

            val sampleData = remember(stateData) {
                stateData?.map { hourlyData ->
                    TempLineItem(
                        count = StringUtils.StringToInteger(StringUtils.getSubString(hourlyData.value, "Unfrozen: ", "")),
                        date = "${hourlyData.key}:00",
                    )
                } ?: emptyList()
            }
            DailyTempLineView(data = sampleData)
        }
    }
}