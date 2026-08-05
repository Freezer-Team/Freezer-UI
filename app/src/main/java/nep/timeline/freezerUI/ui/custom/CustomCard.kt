package nep.timeline.freezerUI.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = CardDefaults.CornerRadius,
    insideMargin: PaddingValues = CardDefaults.InsideMargin,
    colors: CardColors = CardDefaults.defaultColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = colors.color, shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius, bottomEnd = 0.dp, bottomStart = 0.dp))
            .clip(RoundedCornerShape(cornerRadius, cornerRadius, 0.dp, 0.dp)) // For touch feedback, because there is a problem when using Smooth RoundedCornerShape.
            .semantics(mergeDescendants = false) {
                isTraversalGroup = true
            }
            .pointerInput(Unit) {},
        propagateMinConstraints = true
    ) {
        Column(
            modifier = Modifier.padding(insideMargin),
            content = content
        )
    }
}

@Composable
fun CustomCard2(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = CardDefaults.CornerRadius,
    insideMargin: PaddingValues = CardDefaults.InsideMargin,
    colors: CardColors = CardDefaults.defaultColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = colors.color, shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = cornerRadius, bottomStart = cornerRadius))
            .clip(RoundedCornerShape(0.dp, 0.dp, cornerRadius, cornerRadius)) // For touch feedback, because there is a problem when using Smooth RoundedCornerShape.
            .semantics(mergeDescendants = false) {
                isTraversalGroup = true
            }
            .pointerInput(Unit) {},
        propagateMinConstraints = true
    ) {
        Column(
            modifier = Modifier.padding(insideMargin),
            content = content
        )
    }
}