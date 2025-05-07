package playground.main.ui.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import playground.main.model.Graph
import playground.main.ui.model.TileModel
import playground.main.ui.state.CharacterType
import playground.main.ui.state.Circle
import playground.main.ui.state.CommittedAction
import playground.main.ui.state.ElementalType
import playground.main.ui.state.HLine
import playground.main.ui.state.PlayerAction
import playground.main.ui.state.PlayerState
import playground.main.ui.state.Single
import playground.main.ui.state.TileColor
import playground.main.ui.state.TileUiState
import playground.main.ui.state.VLine
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val graph: Graph<TileModel>
): ViewModel() {
    // Grid VM
    private val _tiles = MutableStateFlow<MutableList<TileModel>>(mutableStateListOf())
    val tiles = _tiles.asStateFlow()

    private val _tilePairs = MutableStateFlow<SnapshotStateList<Pair<TileModel, TileUiState>>>(mutableStateListOf())
    val tilePairs = _tilePairs.asStateFlow()

    // Actions VM
    private val _actionsQueue = MutableStateFlow<Map<Int, MutableList<CommittedAction>>>(
        mutableStateMapOf(
            1 to mutableStateListOf(),
            2 to mutableStateListOf(),
            3 to mutableStateListOf(),
            4 to mutableStateListOf(),
            5 to mutableStateListOf(),
            6 to mutableStateListOf(),
            7 to mutableStateListOf(),
            8 to mutableStateListOf(),
            9 to mutableStateListOf(),
        )
    )

    val actionsQueue = _actionsQueue.asStateFlow()

    private val _currentTurn = MutableStateFlow(1)
    val currentTurn = _currentTurn.asStateFlow()

    // Player VM
    private val _playerStates = MutableStateFlow(
        mutableStateListOf(
            PlayerState(
                playerNumber = 1,
                currentTile = 15,
                primaryElementalType = ElementalType.PLASMA,
                secondaryElementalType = ElementalType.GAS,
            ),
            PlayerState(
                playerNumber = 2,
                currentTile = 23,
                primaryElementalType = ElementalType.SOLID,
                secondaryElementalType = ElementalType.LIQUID
            )
        )
    )
    val playerStates= _playerStates.asStateFlow()

    private val _atbStates = MutableStateFlow<MutableList<Int>>(mutableStateListOf(0, 0))
    val atbStates = _atbStates.asStateFlow()

    private val _moveStates = MutableStateFlow<MutableList<Int>>(mutableStateListOf(0, 0))
    val moveStates = _moveStates.asStateFlow()

    private val _uncommittedActions = MutableStateFlow<MutableList<MutableList<Triple<PlayerAction, Int, Int>>>>(mutableStateListOf(mutableStateListOf(), mutableStateListOf()))
    val uncommittedActions = _uncommittedActions.asStateFlow()

    // Grid VM
    fun createBattleGrid(maxWidth: Int, maxHeight: Int, missingTiles: ArrayList<Int>, startingPositions: ArrayList<Int>) {
        var count = 0
        for (row in 0 until maxHeight) {
            for (column in 0 until maxWidth) {
                val showing = !missingTiles.contains(count)
                val tile = TileModel(count, row, column, showing)
                graph.createVertex(tile)
                _tiles.value.add(tile)
                if (startingPositions.contains(tile.index)) {
                    _tilePairs.value.add(Pair(tile, TileUiState(character = CharacterType.PLAYER)))
                } else {
                    _tilePairs.value.add(Pair(tile, TileUiState()))
                }
                count++
            }
        }

        var left = 0
        while (left <= _tiles.value.size - 2) {
            var right = left + 1
            while (right <= _tiles.value.size - 1) {
                val range = 1
                val hypotenuse = sqrt((maxWidth * maxWidth + maxHeight * maxHeight).toDouble()).roundToInt()
                for (radius in range..hypotenuse) {
                    if (isWithinRange(_tiles.value[left], _tiles.value[right], radius)) {
                        graph.addUndirectedEdge(graph.vertices[_tiles.value[left].index], graph.vertices[_tiles.value[right].index], radius.toDouble())
                    }
                }
                right++
            }
            left++
        }

        for (vertex in graph.vertices) {
            println("Test ${graph.edges(vertex)}")
        }
    }

    private fun isNeighbor(vertex1: TileModel, vertex2: TileModel): Boolean {
        return (vertex1.row == vertex2.row && abs(vertex1.index - vertex2.index) <= 1) ||
                (vertex1.column == vertex2.column && abs(vertex1.row - vertex2.row) <= 1) ||
                (abs(vertex1.row - vertex2.row) <= 1 && abs(vertex1.column - vertex2.column) <= 1)
    }

    fun isWithinRange(vertex1: TileModel, vertex2: TileModel, range: Int): Boolean {
        val minIndex = minOf(vertex1.index, vertex2.index)
        val maxIndex = maxOf(vertex1.index, vertex2.index)
        val minTile = if (minIndex == vertex1.index) vertex1 else vertex2
        val maxTile = if (maxIndex == vertex1.index) vertex1 else vertex2

        if (minTile.index % 2 == 0 && minTile.row % 2 == 0) {
            if (
                (minTile.row == maxTile.row && abs(minTile.index - maxTile.index) <= range) ||
                (minTile.column == maxTile.column && abs(minTile.row - maxTile.row) <= range) ||
                (abs(minTile.row - maxTile.row) <= range && abs(minTile.column - maxTile.column) < range) ||
                (abs(minTile.row - maxTile.row) < range && abs(minTile.column - maxTile.column) <= range) ||
                isNeighbor(minTile, maxTile)
            ) {
                return true
            }
        } else if (minTile.index % 2 == 0) {
            if (
                (minTile.row == maxTile.row && abs(minTile.index - maxTile.index) <= range) ||
                (minTile.column == maxTile.column && abs(minTile.row - maxTile.row) <= range) ||
                (abs(minTile.row - maxTile.row) < range && abs(minTile.column - maxTile.column) <= range)
            ) {
                return true
            }
        } else if (minTile.row % 2 == 0) {
            if (
                (minTile.row == maxTile.row && abs(minTile.index - maxTile.index) <= range) ||
                (minTile.column == maxTile.column && abs(minTile.row - maxTile.row) <= range) ||
                (abs(minTile.row - maxTile.row) < range && abs(minTile.column - maxTile.column) <= range)
            ) {
                return true
            }
        } else {
            if (
                (minTile.row == maxTile.row && abs(minTile.index - maxTile.index) <= range) ||
                (minTile.column == maxTile.column && abs(minTile.row - maxTile.row) <= range) ||
                (abs(minTile.row - maxTile.row) <= range && abs(minTile.column - maxTile.column) < range) ||
                (abs(minTile.row - maxTile.row) < range && abs(minTile.column - maxTile.column) <= range) ||
                isNeighbor(minTile, maxTile)
            ) {
                return true
            }
        }
        return false
    }

    // Actions VM
    fun addActions(playerNumber: Int, actions: List<Triple<PlayerAction, Int, Int>>) {
        viewModelScope.launch(Dispatchers.IO) {
            actions.forEach { action ->
                val turnSlot = action.first.delay + _currentTurn.value
                _actionsQueue.update {
                    when (turnSlot <= 5) {
                        false -> {
                            it.plus(
                                (turnSlot % 5) to it[turnSlot % 5]!!.plus((CommittedAction(_playerStates.value[playerNumber - 1], action.first, action.second, action.third))).toMutableList())
                        }

                        true -> {
                            println("Adding action")
                            it.plus(
                                (turnSlot) to it[turnSlot]!!.plus(CommittedAction(_playerStates.value[playerNumber - 1], action.first, action.second, action. third)).toMutableList())
                        }
                    }
                }
            }
        }
    }

    fun nextTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMap = _actionsQueue.value.toMutableMap()
            newMap[_currentTurn.value] = mutableListOf()
            _actionsQueue.value = newMap.toMap()
            val newTurnValue = _currentTurn.value % 5 + 1
            _currentTurn.value = newTurnValue
        }
    }

    // Player VM
    fun beginIncrement() {
        _playerStates.value.forEachIndexed { index, player ->
            viewModelScope.launch(Dispatchers.IO) {
                while (true) {
                    if (_atbStates.value[index] < player.maxATB) {
                        delay(250)
                        _atbStates.update {
                            it.toMutableList().apply {
                                this[index]++
                            }
                        }
                    }
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                while (true) {
                    if (_moveStates.value[index] < player.maxMove) {
                        delay(250)
                        _moveStates.update {
                            it.toMutableList().apply {
                                this[index]++
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subtractATB(player: Int, amount: Int) {
        viewModelScope.launch {
            val newState = _atbStates.value.toMutableList()
            newState[player - 1] = newState[player - 1] - amount
            _atbStates.update {
                it.apply {
                    this[player - 1] -= amount
                }
            }
        }
    }

    private fun subtractMove(player: Int, amount: Int) {
        viewModelScope.launch {
            val newState = _moveStates.value.toMutableList()
            newState[player - 1] = newState[player - 1] - amount
            _moveStates.update {
                it.apply {
                    this[player - 1] -= amount
                }
            }
        }
    }

    fun addPendingAction(player: Int, action: Triple<PlayerAction, Int, Int>) {
        viewModelScope.launch {
            _uncommittedActions.update {
                it.apply {
                    this[player - 1].add(action)
                }
            }
        }
    }

    fun commitActions(player: Int) {
        viewModelScope.launch {
            println("commit Uncommitted Actions Before: ${_uncommittedActions.value}")
            _uncommittedActions.value[player - 1].forEach { action ->
                when (action.first) {
                    is PlayerAction.MoveAction -> {
                        println("Subtracting")
                        val currentPosition = _playerStates.value[player - 1].currentTile
                        val targetPosition = action.second
                        val weight = graph.weights[currentPosition][targetPosition]?.toInt() ?: 1
                        subtractMove(player, action.first.cost * weight)
                    }
                    is PlayerAction.AtbAction -> {
                        println("Subtracting")
                        subtractATB(player, action.first.cost)
                    }
                }
            }
            _uncommittedActions.update {
                it.apply {
                    this[player - 1] = emptyList<Triple<PlayerAction, Int, Int>>().toMutableList()
                }
            }
        }
    }

    private fun setTile(player: Int, tileNumber: Int) {
        viewModelScope.launch {
            _playerStates.update {
                it.apply { this[player - 1] = this[player - 1].copy(currentTile = tileNumber) }
            }
        }
    }

    private fun updateTilePair(index: Int, tileColor: TileColor) {
        _tilePairs.update {
//            when (tileColor) {
//                is Next -> {
//                    it.apply { this[index] = this[index].copy(second = this[index].second.copy(color = this[index].second.color.next()))  }
//                }
//                is Previous -> {
//                    it.apply { this[index] = this[index].copy(second = this[index].second.copy(color = this[index].second.color.previous()))  }
//                }
//                is Promote -> {
//                    it.apply { this[index] = this[index].copy(second = this[index].second.copy(color = this[index].second.color.promote()))  }
//                }
//                is Demote -> {
//                    it.apply { this[index] = this[index].copy(second = this[index].second.copy(color = this[index].second.color.promote()))  }
//                }
//            }
//            }
            it.apply { this[index] = this[index].copy(second = this[index].second.copy(color = this[index].second.color.switch(tileColor)))  }
        }
    }

    fun handleAction(index: Int, action: CommittedAction) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            when (action.action) {
                is PlayerAction.AtbAction -> {
                    val targetTile = _tiles.value[action.targetTile]
                    val aoeTiles = mutableSetOf<TileModel>(targetTile)
                    val castTile = _tiles.value[action.castTile]
                    when (action.action.areaOfEffect) {
                        is Circle -> {
                            aoeTiles.remove(targetTile)
                            _tiles.value.forEach { tile ->
                                if (isWithinRange(_tiles.value[action.user.currentTile], tile, 1)) {
                                    aoeTiles.add(tile)
                                }
                            }
                        }
                        is HLine -> {
                            _tiles.value.forEach { tile ->
                                if (isWithinRange(tile, _tiles.value[index], action.action.areaOfEffect.range)
                                    && isWithinRange(tile, castTile, action.action.areaOfEffect.range)
                                    && tile.index != action.castTile) {
                                    aoeTiles.add(tile)
                                }
                            }
                        }
                        is Single -> {}
                        is VLine -> {
                            if (targetTile.column == castTile.column) {
                                if (targetTile.index > castTile.index) {
                                    val extraTile = _tiles.value.find {
                                        it.column == targetTile.column &&
                                                it.index > targetTile.index &&
                                                isWithinRange(it, targetTile, 1)
                                    }
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                } else {
                                    val extraTile = _tiles.value.find { it.column == targetTile.column && it.index < targetTile.index && isWithinRange(it, targetTile, 1)}
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                }
                            } else if (targetTile.row == castTile.row) {
                                if (targetTile.index > castTile.index) {
                                    val extraTile = _tiles.value.find {
                                        it.row != targetTile.row &&
                                                it.column > targetTile.column &&
                                                it.index < targetTile.index &&
                                                isWithinRange(it, targetTile, 1)
                                    }
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                } else {
                                    val extraTile = _tiles.value.find {
                                        it.row != targetTile.row &&
                                                it.column != targetTile.column &&
                                                it.index < targetTile.index &&
                                        isWithinRange(it, targetTile, 1)
                                    }
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                }
                            } else {
                                if (targetTile.column > castTile.column) {
                                    val extraTile = _tiles.value.find {
                                        it.row == targetTile.row &&
                                                it.index > targetTile.index &&
                                                isWithinRange(it, targetTile, 1)
                                    }
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                } else {
                                    val extraTile = _tiles.value.find {
                                        it.row == targetTile.row &&
                                                it.index < targetTile.index &&
                                                isWithinRange(it, targetTile, 1)
                                    }
                                    if (extraTile != null) {
                                        aoeTiles.add(extraTile)
                                    }
                                }
                            }
                        }
                    }
                    aoeTiles.forEach { tile ->
//                        updateTilePair(index = tile.index, effect = action.action.effect)
                    }
                    if (action.action.elementalType == action.user.primaryElementalType) {
                        aoeTiles.forEach { tile ->
                            val types = _tilePairs.value[tile.index].second.color.elementalTypes
                            if (types.containsAll(listOf(action.user.primaryElementalType, action.user.secondaryElementalType))) {

                            } else if (types.contains(action.user.primaryElementalType)) {
                                val changeTo = TileColor.matchType(setOf(action.user.primaryElementalType, action.user.secondaryElementalType))
                                updateTilePair(tile.index, changeTo)
                            } else if (types.contains(action.user.secondaryElementalType)) {
                                val changeTo = TileColor.matchType(setOf(action.user.primaryElementalType, action.user.secondaryElementalType))
                                updateTilePair(tile.index, changeTo)
                            } else {

                            }
                        }
                    }
                }
                is PlayerAction.MoveAction -> {
                    setPlayer(index = action.user.currentTile, CharacterType.NONE)
                    setPlayer(index = index, CharacterType.PLAYER)
                    setTile(action.user.playerNumber, action.targetTile)
                }
            }
        }
    }

    private fun setPlayer(index: Int, characterType: CharacterType) {
        _tilePairs.update {
            it.apply {
                 this[index] = this[index].copy(second = this[index].second.copy(character = characterType))
            }
        }
    }
}