package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import playground.main.model.Graph
import playground.main.ui.model.TileModel
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    val graph: Graph<TileModel>
): ViewModel() {
    private val _tiles = MutableStateFlow<ArrayList<TileModel>>(arrayListOf())
    val tiles = _tiles.asStateFlow()

    private val _missingTiles = MutableStateFlow<ArrayList<Int>>(arrayListOf())
    val missingTiles = _missingTiles.asStateFlow()

    private val _shownTiles = MutableStateFlow<ArrayList<Int>>(arrayListOf())
    val shownTiles = _shownTiles.asStateFlow()

    fun createBattleGrid(maxWidth: Int, maxHeight: Int, missingTiles: ArrayList<Int>) {
        var count = 0
        for (row in 0 until maxHeight) {
            for (column in 0 until maxWidth) {
                val showing = !missingTiles.contains(count)
                val tile = TileModel(count, row, column, showing)
                graph.createVertex(tile)
                _tiles.value.add(tile)
                count++
                println("tile index: ${tile.index}")
            }
        }

        var left = 0
        while (left <= tiles.value.size - 2) {
            var right = left + 1
            while (right <= tiles.value.size - 1) {
                if (isNeighbor(tiles.value[left], tiles.value[right])) {
                    graph.addUndirectedEdge(graph.vertices[tiles.value[left].index], graph.vertices[tiles.value[right].index], 1.0)
                }
                right++
            }
            left++
        }

        for (vertex in graph.vertices) {
            println("${graph.edges(vertex)}")
        }

        graph.vertices.forEach { vertex ->
            if (vertex.data.show) {
                _shownTiles.value.add(vertex.index)
            } else {
                _missingTiles.value.add(vertex.index)
            }
        }
    }

    fun isNeighbor(vertex1: TileModel, vertex2: TileModel): Boolean {
        if (
            (vertex1.row == vertex2.row && Math.abs(vertex1.index - vertex2.index) <= 1) ||
            (vertex1.column == vertex2.column && Math.abs(vertex1.row - vertex2.row) <= 1) ||
            (Math.abs(vertex1.row - vertex2.row) <= 1 && Math.abs(vertex1.column - vertex2.column) <= 1)
            ) {
            return true
        }
        return false
    }
}