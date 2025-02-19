package playground.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import playground.main.ui.state.PlayerAction
import playground.main.ui.state.PlayerState
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val _atbState = MutableStateFlow<Int>(0)
    val atbState = _atbState.asStateFlow()

    private val _moveState = MutableStateFlow<Int>(0)
    val moveState = _moveState.asStateFlow()

    private val _uncommittedActions = MutableStateFlow<List<Pair<PlayerAction, Int>>>(listOf())
    val uncommittedActions = _uncommittedActions.asStateFlow()

    private val _committedActions = MutableStateFlow<List<PlayerAction>>(listOf())
    val committedActions = _committedActions.asStateFlow()

    fun beginIncrement() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (_atbState.value < _playerState.value.maxATB) {
//                    println(_atbState.value)
                    delay(250)
                    _atbState.update { it.inc() }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (_moveState.value < _playerState.value.maxMove) {
//                    println(_moveState.value)
                    delay(250)
                    _moveState.update { it.inc() }
                }
            }
        }
    }

    fun subtractATB(amount: Int) {
        viewModelScope.launch {
            _atbState.update {
                it.minus(amount)
            }
        }
    }

    fun subtractMove(amount: Int) {
        viewModelScope.launch {
            _moveState.update {
                it.minus(amount)
            }
        }
    }

    fun addPendingAction(action: Pair<PlayerAction, Int>) {
        viewModelScope.launch {
            _uncommittedActions.update {
                it.plus(action)
            }
            println(uncommittedActions.value)
        }
    }

    fun commitActions() {
        viewModelScope.launch {
//            _committedActions.update {
//                it.plus(_uncommittedActions.value)
//            }
            _uncommittedActions.value.forEach { action ->
                when (action.first) {
                    is PlayerAction.MoveAction -> subtractMove(action.first.cost)
                    is PlayerAction.AtbAction -> subtractATB(action.first.cost)
                }
            }
            _uncommittedActions.update {
                it.drop(_uncommittedActions.value.size)
            }
        }
    }
}