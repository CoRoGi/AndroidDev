package playground.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BattleTileGrid() {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize().systemBarsPadding().padding(vertical = 30.dp).align(
                    Alignment.Center),
                columns = StaggeredGridCells.FixedSize(50.dp)
            ) {
                items(12) { index ->
                    if (index % 2 == 0) {
                        BattleTile(offset = 50, index)
                    } else {
                        BattleTile(index = index)
                    }
                }
            }
        }
    }
}