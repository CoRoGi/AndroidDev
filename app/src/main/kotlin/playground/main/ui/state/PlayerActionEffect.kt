package playground.main.ui.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlayerActionEffect

@Serializable
@SerialName("next")
data object Next: PlayerActionEffect()

@Serializable
@SerialName("promote")
data object Promote: PlayerActionEffect()

@Serializable
@SerialName("previous")
data object Previous: PlayerActionEffect()

@Serializable
@SerialName("demote")
data object Demote: PlayerActionEffect()
