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
import playground.main.ui.vm.ActionsViewModel

@Composable
fun PlayerActionUI(
    playerNumber: Int,
    viewModel: PlayerViewModel = hiltViewModel(),
    playerActions: List<PlayerAction>,
    navController: NavController,
    actionViewModel: ActionsViewModel = hiltViewModel(),
) {
    val pendingActions by viewModel.uncommittedActions.collectAsStateWithLifecycle()
    val pendingAtbCost by remember {
        derivedStateOf {
            pendingActions.filter { it.first is PlayerAction.AtbAction }.map { it.first.cost }.fold(0) {
                    acc, value -> acc + value
            }
        }
    }
    val pendingMoveCost by remember {
        derivedStateOf {
            pendingActions.filter { it.first is PlayerAction.AtbAction }.map { it.first.cost }.fold(0) {
                    acc, value -> acc + value
            }
        }
    }

    val atbState by viewModel.atbState.collectAsStateWithLifecycle()
    val moveState by viewModel.moveState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
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
                            playerActions[item].cost + pendingAtbCost <= viewModel.playerState.value.maxATB && atbState > 0
                        }
                        is PlayerAction.MoveAction -> {
                            playerActions[item].cost + pendingMoveCost <= viewModel.playerState.value.maxMove && moveState > 0
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
                viewModel.commitActions()
                actionViewModel.addActions(pendingActions)
            }) {
            Text("Commit")
        }
    }
}