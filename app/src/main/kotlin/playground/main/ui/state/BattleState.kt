package playground.main.ui.state

data class BattleState(
    val turnNumber: Int = 0,
    val committedActions: List<CommittedAction> = listOf(),
    val players: List<PlayerState> = listOf()
)