package playground.main.ui.state

data class CommittedAction(
    val user: PlayerState,
    val action: PlayerAction,
    val targetTile: Int,
    val castTile: Int,
)