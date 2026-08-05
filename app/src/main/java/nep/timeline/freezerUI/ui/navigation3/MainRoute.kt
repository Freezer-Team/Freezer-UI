package nep.timeline.freezerUI.ui.navigation3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation keys for Navigation3.
 * Each destination is a NavKey (data object/data class) and can be saved/restored in the back stack.
 */
@Serializable
sealed interface MainRoute : NavKey {
    @Serializable
    data object Main : MainRoute

    @Serializable
    data object About : MainRoute

    @Serializable
    data object Log : MainRoute

    @Serializable
    data object FreezerSettings : MainRoute

    @Serializable
    data object MMSettings : MainRoute

    @Serializable
    data object PUSHSettings : MainRoute

    @Serializable
    data object BatterySettings : MainRoute

    @Serializable
    data object ScriptSettings : MainRoute
}