package playground.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import playground.main.ui.model.TileModel
import playground.main.ui.state.TileUiState

@Composable
fun TileGrid(
    width: Int,
    height: Int,
    tilePairs: SnapshotStateList<Pair<TileModel, TileUiState>>,
    ) {
    // TODO: Add neighbor param to battle tile and get from viewModel's graph
    Column(
        modifier = Modifier.systemBarsPadding(),
        verticalArrangement = Arrangement.Top
    ) {
        var count = 0
        for (row in 0 until height) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for (item in 0 until width) {
                    if (item % 2 == 0) {
                        BattleTile(
                            index = count,
                            offset = 50,
                            show = tilePairs[count].first.show,
                            tile = tilePairs[count].second,
                        )
                    } else {
                        BattleTile(
                            index = count,
                            offset = 0,
                            show = tilePairs[count].first.show,
                            tile = tilePairs[count].second
                        )
                    }
                    count++
                }
            }

        }

    }
}