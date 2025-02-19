package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import playground.main.ui.state.Demote
import playground.main.ui.state.Next
import playground.main.ui.state.PlayerAction
import playground.main.ui.state.PlayerActionEffect
import playground.main.ui.state.Previous
import playground.main.ui.state.Promote
import playground.main.ui.state.TileColor
import playground.main.ui.state.TileUiState
import javax.inject.Inject

@HiltViewModel
class TileViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(TileUiState())
    val uiState: StateFlow<TileUiState> = _uiState.asStateFlow()

    fun updateTile(effect: PlayerActionEffect) {
        _uiState.update { currentState ->
            when (effect) {
                is Next -> {
                    currentState.copy(
                        color = currentState.color.next()
                    )
                }
                is Previous -> {
                    currentState.copy(
                        color = currentState.color.previous()
                    )
                }
                is Promote -> {
                    currentState.copy(
                        color = currentState.color.promote()
                    )
                }
                is Demote -> {
                    // TODO: Replace with demote method
                    currentState.copy(
                        color = currentState.color.promote()
                    )
                }
            }
        }
    }

    fun setColor(color: TileColor) {
        _uiState.update { currentState ->
            currentState.copy(color = color)
        }
    }

    fun handleAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.AtbAction -> {
                println("Handling action: ${action.name}")
                    updateTile(effect = action.effect)
                }
            is PlayerAction.MoveAction -> {}
        }
    }
}