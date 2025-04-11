package playground.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import playground.main.ui.vm.BattleViewModel

@Composable
fun PlayerResourceUI(player: Int, battleViewModel: BattleViewModel = hiltViewModel()) {
    val battlePlayerStates by battleViewModel.playerStates.collectAsStateWithLifecycle()
    val battleAtbStates by battleViewModel.atbStates.collectAsStateWithLifecycle()
    val battleMoveStates by battleViewModel.moveStates.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
//        playerViewModel.beginIncrement()
        battleViewModel.beginIncrement()
    }

    Box(
        Modifier.fillMaxSize().background(Color.LightGray)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            LinearProgressIndicator(
                progress = {
//                    atbState / 100f
                    battleAtbStates[player - 1] / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
            LinearProgressIndicator(
                progress = {
//                    moveState / 100f
                    battleMoveStates[player - 1] / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
            LinearProgressIndicator(
                progress = {
//                    playerState.currentHP / 100f
                    battlePlayerStates[player - 1].currentHP / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
        }
    }
}
