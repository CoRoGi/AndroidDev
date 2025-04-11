package playground.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import playground.main.ui.state.CharacterType
import playground.main.ui.state.TileUiState

@Composable
fun BattleTile(offset: Int = 0, index: Int, show: Boolean = true, tile: TileUiState) {
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
                        drawPath(roundedPolygonPath, color = tile.color.color)
                    }
                }.height(50.dp).width(50.dp)
        ) {
            Text("$index", modifier = Modifier.offset(x= 13.dp, y = 25.dp).background(Color.Red))
            when (tile.character) {
                CharacterType.NONE -> {
                    Icon(Icons.Filled.LocationOn, "player", modifier = Modifier.align(Alignment.Center).offset(y = (offset/2).dp))
                }
                CharacterType.PLAYER -> {
                    Icon(Icons.Filled.Face, "player", modifier = Modifier.align(Alignment.Center).offset(y = (offset/2).dp))
                }
                CharacterType.ENEMY -> {
                    Icon(Icons.Filled.Warning, "player", modifier = Modifier.align(Alignment.Center).offset(y = (offset/2).dp))
                }
            }
        }
    }
}