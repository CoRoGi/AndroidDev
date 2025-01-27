package playground.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import playground.main.ui.vm.GridViewModel
import playground.main.ui.vm.TileViewModel

//@Composable
//fun BattleTileGrid() {
//    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
//        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
//            LazyVerticalStaggeredGrid(
//                modifier = Modifier.fillMaxSize().systemBarsPadding().padding(vertical = 30.dp).align(
//                    Alignment.Center),
//                columns = StaggeredGridCells.FixedSize(50.dp)
//            ) {
//                items(12) { index ->
//                    if (index % 2 == 0) {
//                        BattleTile(offset = 50, index)
//                    } else {
//                        BattleTile(index = index)
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun TileGrid(viewModel: GridViewModel = hiltViewModel(), width: Int, height: Int, missingTiles: ArrayList<Int> = arrayListOf()) {
    viewModel.createBattleGrid(width, height)
    // TODO: Add neighbor param to battle tile and get from viewModel's graph

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        verticalArrangement = Arrangement.Top
    ) {
        var count = 0
        for (row in 0 until height) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for (item in 0 until width) {
                    if (missingTiles.contains(count)) {
                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), index = count, show = false)
                    } else if (item % 2 == 0) {
                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), offset = 50, index = count)
                    } else {
                        BattleTile(hiltViewModel<TileViewModel>(key = count.toString()), index = count)
                    }
                    count++
                }
            }

        }

    }
}