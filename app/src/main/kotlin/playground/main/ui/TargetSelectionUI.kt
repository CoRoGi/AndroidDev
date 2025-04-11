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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import playground.main.ui.model.TileModel
import playground.main.ui.state.PlayerAction
import playground.main.ui.vm.BattleViewModel

@Composable
fun TargetSelectionUI(
    tiles: List<TileModel>,
    action: PlayerAction,
    player: Int,
    navController: NavHostController,
    battleViewModel: BattleViewModel = hiltViewModel()
) {
    val screen = when (player) {
        1 -> Player1
        else -> Player2
    }

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            val currentTile = tiles.find { it.index == battleViewModel.playerStates.value[player -1].currentTile }!!
            if (action is PlayerAction.AtbAction && action.range == 0) {
                Button(
                    onClick = {
                        battleViewModel.addPendingAction(player, Triple(action, currentTile.index, currentTile.index))
                        navController.navigate(screen)
                    }
                ) {
                    Text("Tile number ${currentTile.index}")
                }
            } else
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.padding(10.dp),
                rows = StaggeredGridCells.Adaptive(30.dp),
                horizontalItemSpacing = 16.dp,
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                items(tiles.size) { item ->
                    when (action) {
                        is PlayerAction.AtbAction -> {
                            if (battleViewModel.isWithinRange(tiles.find { it.index == battleViewModel.playerStates.value[player -1].currentTile }!!, tiles[item], action.range) &&
                                tiles[item].index != battleViewModel.playerStates.value[player - 1].currentTile
                                ) {
                                Button(
                                    onClick = {
                                        battleViewModel.addPendingAction(player, Triple(action, tiles[item].index, currentTile.index))
                                        navController.navigate(screen)
                                    }
                                ) {
                                    Text("Tile number ${tiles[item].index}")
                                }
                            }
                        }
                        else -> {
                            Button(
                                onClick = {
                                    battleViewModel.addPendingAction(player, Triple(action, tiles[item].index, currentTile.index))
                                    navController.navigate(screen)
                                }
                            ) {
                                Text("Tile number ${tiles[item].index}")
                            }
                        }
                        }
                    }
                }
            }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navController.navigate(screen)
            }) {
            Text("Cancel")
        }
    }
}