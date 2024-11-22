package playground.main.ui.state

import androidx.compose.ui.graphics.Color

data class TileUiState(
    val color: TileColor = TileColor.Blue(),
    val position: Pair<Int, Int> = Pair(0, 0)
)