package playground.main.ui.state

data class BattleState(
    val timerState: TimerState,
    val playerState: PlayerState,
    val currentTurn: Int
)
