package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import playground.main.model.Graph
import playground.main.ui.model.TileModel
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    val graph: Graph<TileModel>
): ViewModel() {
    val tiles = arrayListOf<TileModel>()

    fun createBattleGrid(maxWidth: Int, maxHeight: Int) {
        var count = 0
        for (row in 0 until maxHeight) {
            for (column in 0 until maxWidth) {
                val tile = TileModel(count, row, column)
                graph.createVertex(tile)
                tiles.add(tile)
                count++
                println("tile index: ${tile.index}")
            }
        }

        var left = 0
        while (left <= tiles.size - 2) {
            var right = left + 1
            while (right <= tiles.size - 1) {
                if (isNeighbor(tiles[left], tiles[right])) {
                    graph.addUndirectedEdge(graph.vertices[tiles[left].index], graph.vertices[tiles[right].index], 1.0)
                }
                right++
            }
            left++
        }
        for (vertex in graph.vertices) {
            println("${graph.edges(vertex)}")
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