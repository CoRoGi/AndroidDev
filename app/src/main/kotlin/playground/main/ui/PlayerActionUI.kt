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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import playground.main.ui.state.PlayerAction
import playground.main.ui.vm.BattleViewModel

@Composable
fun PlayerActionUI(
    playerNumber: Int,
    playerActions: List<PlayerAction>,
    navController: NavController,
    battleViewModel: BattleViewModel = hiltViewModel()
) {
    val battlePendingActions by battleViewModel.uncommittedActions.collectAsStateWithLifecycle()
    val battlePendingAtbCost by remember {
        derivedStateOf {
            battlePendingActions[playerNumber - 1].filter { it.first is PlayerAction.AtbAction }.map { it.first.cost }.fold(0) {
                acc, value, -> acc + value
            }
        }
    }

    val battlePendingMoveCost by remember {
        derivedStateOf {
            battlePendingActions[playerNumber - 1].filter { it.first is PlayerAction.MoveAction }
                .map { it.first.cost }.fold(0) { acc, value, ->
                acc + value
            }
        }
    }

    val atbStates by battleViewModel.atbStates.collectAsStateWithLifecycle()
    val moveStates by battleViewModel.moveStates.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        println("Player Action UI: ${battlePendingActions}")
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.padding(10.dp),
                rows = StaggeredGridCells.Adaptive(30.dp),
                horizontalItemSpacing = 16.dp,
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                items(playerActions.size) { item ->
                    val enabled = when (playerActions[item]) {
                        is PlayerAction.AtbAction -> {
                            playerActions[item].cost + battlePendingAtbCost <= battleViewModel.playerStates.value[playerNumber - 1].maxATB && atbStates[playerNumber - 1] > 0
                        }
                        is PlayerAction.MoveAction -> {
                            playerActions[item].cost + battlePendingMoveCost <= battleViewModel.playerStates.value[playerNumber - 1].maxMove && moveStates[playerNumber - 1] > 0
                        }
                    }
                    Button(enabled = enabled, onClick = { navController.navigate(Target(playerActions[item], playerNumber )) }) {
                        Text("${playerActions[item].name}")
                    }
                }
            }
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                battleViewModel.addActions(playerNumber, battlePendingActions[playerNumber - 1])
                battleViewModel.commitActions(playerNumber)
            }) {
            Text("Commit")
        }
    }
}