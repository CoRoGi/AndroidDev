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

@Serializable
@SerialName("toSL")
data object toSL: PlayerActionEffect()

@Serializable
@SerialName("toSG")
data object toSG: PlayerActionEffect()

@Serializable
@SerialName("toSP")
data object toSP: PlayerActionEffect()

@Serializable
@SerialName("toLG")
data object toLG: PlayerActionEffect()

@Serializable
@SerialName("toLP")
data object toLP: PlayerActionEffect()

@Serializable
@SerialName("toGP")
data object toGP: PlayerActionEffect()
