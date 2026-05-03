package nep.timeline.freezerUI.ui.navigation3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation keys for Navigation3.
 * Each destination is a NavKey (data object/data class) and can be saved/restored in the back stack.
 */
sealed interface Route : NavKey {
    @Serializable
    data object Main : Route

    @Serializable
    data object About : Route

    @Serializable
    data object FreezerSettings : Route

    @Serializable
    data object MMSettings : Route

    @Serializable
    data object PUSHSettings : Route

    @Serializable
    data object BatterySettings : Route
}