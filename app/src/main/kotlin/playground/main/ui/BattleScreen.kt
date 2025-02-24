package playground.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import playground.main.ui.state.PlayerAction
import playground.main.ui.vm.ActionsViewModel
import playground.main.ui.vm.GridViewModel
import playground.main.ui.vm.TileViewModel

@Composable
fun BattleScreen(
    playerViewModel: PlayerViewModel = hiltViewModel(),
    gridViewModel: GridViewModel = hiltViewModel(),
    actionVM: ActionsViewModel = hiltViewModel(),
    maxWidth: Int,
    maxHeight: Int
) {
    val tiles by gridViewModel.tiles.collectAsStateWithLifecycle()
    val shownTiles by gridViewModel.shownTiles.collectAsStateWithLifecycle()
    val missingTiles by gridViewModel.missingTiles.collectAsStateWithLifecycle()

    val currentTurn by actionVM.currentTurn.collectAsStateWithLifecycle()
    val committedActions by actionVM.committedActions.collectAsStateWithLifecycle()

    gridViewModel.createBattleGrid(maxWidth, maxHeight, arrayListOf(0, 1, 3, 4, 20, 24))
//    gridViewModel.createBattleGrid(maxWidth, maxHeight, arrayListOf())
//    gridViewModel.createBattleGrid(maxWidth, maxHeight, arrayListOf(6, 8))

    val battleTileVMs = mutableListOf<TileViewModel>()

    tiles.forEach {
        battleTileVMs.add(hiltViewModel<TileViewModel>(key = it.index.toString()))
    }

//    val actionVM = hiltViewModel<ActionsViewModel>()

    val navController = rememberNavController()

    Column(Modifier.fillMaxSize()) {
        TileGrid(
            width = maxWidth,
            height = maxHeight,
            missingTiles = missingTiles,
            shownTiles = shownTiles,
            tiles = tiles,
            vms = battleTileVMs
        )

        AutoScrollingLazyRow(list = listOf(1, 2, 3, 4, 5), actionViewModel = actionVM, tileVMs = battleTileVMs) { item ->
            LazyListItem(item, committedActions)
        }

        val players = listOf("Player 1", "Player 2")

        val vms = listOf(
            hiltViewModel<PlayerViewModel>(key = "0"),
            hiltViewModel<PlayerViewModel>(key = "1")
        )

        val playerUIs = listOf<@Composable () -> Unit>(
            { PlayerResourceUI(vms[0]) },
            { PlayerResourceUI(vms[1]) }
        )

        val player1Actions = listOf(PlayerAction.Move(), PlayerAction.Wait(), PlayerAction.Magic())
        val player2Actions = listOf(PlayerAction.Move(), PlayerAction.Slash(), PlayerAction.Wait(), PlayerAction.Magic())

        val playerTabContents =
            listOf<@Composable () -> Unit>(
                { PlayerActionUI(1, vms[0], player1Actions, navController, actionViewModel = actionVM) },
                { PlayerActionUI(2, vms[1], player2Actions, navController, actionViewModel = actionVM) })

//        val targetSelectionContent =
//            listOf<@Composable () -> Unit>({ TargetSelectionUI(shownTiles, battleTileVMs, vms[0]) },
//                { TargetSelectionUI(shownTiles, battleTileVMs, vms[1]) })

        PlayerTabs(
            players,
            playerUIs,
            playerTabContents,
            shownTiles,
            battleTileVMs,
            navController,
            vms,
//            targetSelectionContent
        )
//        PlayerResourceUI()
//        ProgressIndicator()
    }
}

@Composable
fun LazyListItem(turn: Int, actions: Map<Int, List<Pair<PlayerAction, Int>>>) {
    val turnActions = actions[turn]
    val actionText = StringBuilder()
    turnActions!!.forEach { action ->
        actionText.append("${action.first.name} ")
    }
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 6.dp)
            .width(100.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Turn ${turn}",
                fontSize = 24.sp
            )
            Text(
                text = actionText.toString(),
                fontSize = 12.sp
            )
        }
    }
}

