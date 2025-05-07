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
import playground.main.ui.state.CommittedAction
import playground.main.ui.state.ElementalType
import playground.main.ui.state.PlayerAction
import playground.main.ui.vm.BattleViewModel

@Composable
fun BattleScreen(
    battleViewModel: BattleViewModel = hiltViewModel(),
    maxWidth: Int,
    maxHeight: Int
) {
    val navController = rememberNavController()
    val battleCommittedActions by battleViewModel.actionsQueue.collectAsStateWithLifecycle()
    val tilePairs by battleViewModel.tilePairs.collectAsStateWithLifecycle()

    val players = listOf("Player 1", "Player 2")

    val playerUIs = listOf<@Composable () -> Unit>(
        { PlayerResourceUI(1, battleViewModel) },
        { PlayerResourceUI(2, battleViewModel) }
    )

//    battleViewModel.setPlayer(15, CharacterType.PLAYER, 1)
//    battleViewModel.setPlayer(23, CharacterType.PLAYER, 2)
//    battleViewModel.setTile(1, 15)
//    battleViewModel.setTile(2, 23)
//        vms[0].setTile(12)
//        vms[1].setTile(16)

//        battleTileVMs[12].setPlayer()
//        battleTileVMs[16].setPlayer()

    val player1Actions = listOf(
        PlayerAction.Move(),
        PlayerAction.Wait(elementalType = ElementalType.GAS),
        PlayerAction.Magic(elementalType = ElementalType.PLASMA)
    )
    val player2Actions =
        listOf(
            PlayerAction.Move(),
            PlayerAction.Slash(elementalType = ElementalType.SOLID),
            PlayerAction.Wait(elementalType = ElementalType.LIQUID),
            PlayerAction.Magic(elementalType = ElementalType.GAS)
        )

    val playerTabContents =
        listOf<@Composable () -> Unit>(
            {
                PlayerActionUI(
                    1,
                    player1Actions,
                    navController,
                    battleViewModel = battleViewModel
                )
            },
            {
                PlayerActionUI(
                    2,
                    player2Actions,
                    navController,
                    battleViewModel = battleViewModel
                )
            },
        )

    Column(Modifier.fillMaxSize()) {
        TileGrid(
            width = maxWidth,
            height = maxHeight,
            tilePairs = tilePairs,
        )

        AutoScrollingLazyRow(
            list = listOf(1, 2, 3, 4, 5),
            battleViewModel = battleViewModel
        ) { item ->
            LazyListItem(item, battleCommittedActions)
        }

        PlayerTabs(
            players,
            playerUIs,
            playerTabContents,
            navController,
            tilePairs,
            battleViewModel
        )
    }
}

@Composable
fun LazyListItem(turn: Int, actions: Map<Int, List<CommittedAction>>) {
    val turnActions = actions[turn]
    val actionText = StringBuilder()
    turnActions!!.forEach { action ->
        actionText.append("${action.action.name} ")
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

