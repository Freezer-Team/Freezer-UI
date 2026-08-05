package nep.timeline.freezer.binders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UIComponentData(
    val uuid: String,
    val type: ComponentType,
    val title: String,
    val summary: String? = null,
    val boolValue: Boolean = false,
    val floatMinValue: Float = 0f,
    val floatMaxValue: Float = 0f,
    val floatIncrementValue: Float = 0.01f,
    val floatDefaultValue: Float = 0f,
    val intValue: Int = 0,
    val items: List<String>? = null
) : Parcelable

enum class ComponentType {
    TEXT,
    SWITCH,
    DROPDOWN,
    SLIDER
}