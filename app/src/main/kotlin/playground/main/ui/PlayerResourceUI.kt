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
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PlayerResourceUI(playerViewModel: PlayerViewModel) {
    val playerState by playerViewModel.playerState.collectAsStateWithLifecycle()
    val atbState by playerViewModel.atbState.collectAsStateWithLifecycle()
    val moveState by playerViewModel.moveState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        playerViewModel.beginIncrement()
    }

    Box(
        Modifier.fillMaxSize().background(Color.LightGray)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            LinearProgressIndicator(
                progress = {
                    atbState / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
            LinearProgressIndicator(
                progress = {
                    moveState / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
            LinearProgressIndicator(
                progress = {
                    playerState.currentHP / 100f
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp).fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red
            )
        }
    }
}
