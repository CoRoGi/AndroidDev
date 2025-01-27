package playground.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import playground.main.ui.vm.TileViewModel

@Composable
fun BattleTile(viewModel: TileViewModel, offset: Int = 0, index: Int, show: Boolean = true) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (show) {
        Box(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension / 2,
                        centerX = size.width / 2,
                        centerY = size.height / 2 + offset
                    )
                    val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                    onDrawBehind {
                        drawPath(roundedPolygonPath, color = uiState.color.color)
                    }
                }.height(50.dp).width(50.dp).clickable { viewModel.updateTile() }
        ) {
            Text("$index", modifier = Modifier.offset(x= 13.dp, y = 25.dp).background(Color.Red))
        }
    }
}