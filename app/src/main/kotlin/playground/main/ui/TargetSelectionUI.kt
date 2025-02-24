package playground.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import playground.main.ui.state.PlayerAction
import playground.main.ui.vm.TileViewModel

@Composable
fun TargetSelectionUI(
    tiles: List<Int>,
    vms: List<TileViewModel>,
    playerVM: PlayerViewModel = hiltViewModel(),
    playerVmList: List<PlayerViewModel> = listOf(),
    action: PlayerAction,
    player: Int,
    navController: NavHostController
) {
    val currentPlayer by remember { mutableStateOf(1) }
    val currentVMIndex by remember { mutableStateOf(0) }

    val uncommittedActions = playerVmList[player - 1].uncommittedActions.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.padding(10.dp),
                rows = StaggeredGridCells.Adaptive(30.dp),
                horizontalItemSpacing = 16.dp,
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                items(tiles.size) { item ->
                    Button(
                        onClick = {
                            playerVmList[player - 1].addPendingAction(Pair(action, tiles[item]))
                            navController.popBackStack()
                        }
                    ) {
                        Text("Tile number ${tiles[item]}")
                    }
                }
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
//                playerVmList[player - 1].addPendingAction(action)
//                println("Player number $player, action added is ${action.name}")
                navController.popBackStack()
            }) {
            Text("Cancel")
        }
    }
}