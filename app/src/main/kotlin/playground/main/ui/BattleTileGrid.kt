package playground.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import playground.main.ui.model.TileModel
import playground.main.ui.vm.GridViewModel
import playground.main.ui.vm.TileViewModel

@Composable
fun TileGrid(viewModel: GridViewModel = hiltViewModel(), width: Int, height: Int, missingTiles: ArrayList<Int> = arrayListOf(), shownTiles: ArrayList<Int> = arrayListOf(), tiles: ArrayList<TileModel> = arrayListOf(), vms: List<TileViewModel>) {
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
                        BattleTile(vms[count], index = count, offset = 50, show = tiles[count].show)
                    } else {
                        BattleTile(vms[count], index = count, offset = 0, show = tiles[count].show)
                    }
//                    if (missingTiles.contains(count)) {
//                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), index = count, show = false)
//                    } else if (item % 2 == 0) {
//                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), offset = 50, index = count)
//                    } else {
//                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), index = count)
//                    }
                    count++
                }
            }

        }

    }
}