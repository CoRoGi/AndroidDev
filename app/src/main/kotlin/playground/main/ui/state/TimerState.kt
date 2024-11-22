package playground.main.ui.state

data class TimerState(
    val isRunning: Boolean = true,
    val turnsPassed: Int = 0
)
