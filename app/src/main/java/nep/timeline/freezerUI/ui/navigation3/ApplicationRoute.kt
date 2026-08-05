package nep.timeline.freezer.ui.navigation3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation keys for Navigation3.
 * Each destination is a NavKey (data object/data class) and can be saved/restored in the back stack.
 */
@Serializable
sealed interface ApplicationRoute : NavKey {
    @Serializable
    data object Main : ApplicationRoute

    @Serializable
    data object ScriptSettings : ApplicationRoute
}