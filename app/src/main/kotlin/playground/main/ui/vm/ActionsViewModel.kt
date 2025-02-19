package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import playground.main.ui.state.PlayerAction
import javax.inject.Inject

@HiltViewModel
class ActionsViewModel @Inject constructor() : ViewModel() {
    private val _upcomingActions = MutableStateFlow<List<PlayerAction>>(listOf())
    val upcomingActions = _upcomingActions.asStateFlow()

    private val _committedActions = MutableStateFlow<Map<Int, List<Pair<PlayerAction, Int>>>>(
        mapOf(
            1 to listOf(),
            2 to listOf(),
            3 to listOf(),
            4 to listOf(),
            5 to listOf(),
            6 to listOf(),
            7 to listOf(),
            8 to listOf(),
            9 to listOf(),
//            10 to listOf(),
        )
    )
    val committedActions = _committedActions.asStateFlow()

    private val _currentTurn = MutableStateFlow<Int>(1)
    val currentTurn = _currentTurn.asStateFlow()

    fun addActions(actions: List<Pair<PlayerAction, Int>>) {
        viewModelScope.launch(Dispatchers.IO) {
            _upcomingActions.update {
                it.plus(actions.map { action -> action.first})
            }
            println("Before ${_committedActions.value}")
            actions.forEach { action ->
                val turnSlot = action.first.delay + _currentTurn.value
                _committedActions.update {
//                    it.plus(
//                        (turnSlot) to it[turnSlot]!!.plus(action)
//                    )
                    when (turnSlot <= 5) {
                        false -> {
//                            it[(action.delay + _currentTurn.value) % 5].
                            println("Adding action wrap around")
                            it.plus(
                                (turnSlot % 5) to it[turnSlot % 5]!!.plus(action)
                            )
                        }

                        true -> {
                            println("Adding action")
                            it.plus(
                                (turnSlot) to it[turnSlot]!!.plus(action)
                            )
                        }
                    }
                }
            }
            println("After ${_committedActions.value}")
        }
    }

    fun executeAction() {
        _upcomingActions.update {
            it.drop(1)
        }
    }

    fun nextTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMap = _committedActions.value.toMutableMap()
            println("newMap before: $newMap")
            _committedActions.value.keys.forEach { key ->
                println("key is $key")
//                if (key == 1) {
////                    newMap[key] = listOf()
//                    println("Key equals 1: ${newMap[key]}")
//                } else {
//                    when (key > 5) {
//                        true -> {
//                            val temp = newMap[key]
//                            newMap[key - 1] = temp!!
//                            newMap[key] = listOf()
//                        }
//                        false -> {
////                           println("map at first key: ${_committedActions.value[key]} and current Turn is ${_currentTurn.value}")
//                        }
//                    }
//                }
            }
            newMap[_currentTurn.value]!!.forEach { action ->
                println("Action executed in turn ${_currentTurn.value}: ${action.first.name}")
            }
            newMap[_currentTurn.value] = listOf()
            _committedActions.value = newMap.toMap()
            println("newMap after: $newMap")
            val newTurnValue = _currentTurn.value % 5 + 1
            println("current Turn is ${_currentTurn.value} and new turn is $newTurnValue")
            _currentTurn.value = newTurnValue
//            _currentTurn.update {
//                if (_currentTurn.value == 5) {
//                    it.minus(4)
//                } else {
//                    it.inc()
//                }
//            }
            println("Turn incremented to ${_currentTurn.value}")
        }
    }
}