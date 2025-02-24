package playground.main.ui.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerAction {
    val name: String
    val cost: Int
    val delay: Int

    @Serializable
    @SerialName("MoveAction")
    sealed class MoveAction : PlayerAction
    @Serializable
    @SerialName("AtbAction")
    sealed class AtbAction : PlayerAction {
        abstract val effect: PlayerActionEffect
    }

    @Serializable
    @SerialName("move")
    data class Move(
        override val name: String = "Move",
        override val cost: Int = 10,
        override val delay: Int = 1,
    ) : MoveAction()

    @Serializable
    @SerialName("slash")
    data class Slash(
        override val name: String = "Slash",
        override val cost: Int = 45,
        override val delay: Int = 2,
        override val effect: PlayerActionEffect = Next
    ) : AtbAction()

    @Serializable
    @SerialName("wait")
    data class Wait(
        override val name: String = "Wait",
        override val cost: Int = 50,
        override val delay: Int = 1,
        override val effect: PlayerActionEffect = Previous
    ) : AtbAction()

    @Serializable
    @SerialName("magic")
    data class Magic(
        override val name: String = "Magic",
        override val cost: Int = 60,
        override val delay: Int = 3,
        override val effect: PlayerActionEffect = Promote
    ) : AtbAction()
}