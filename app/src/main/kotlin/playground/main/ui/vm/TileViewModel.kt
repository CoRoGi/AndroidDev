package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import playground.main.ui.state.TileColor
import playground.main.ui.state.TileUiState
import javax.inject.Inject

@HiltViewModel
class TileViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(TileUiState())
    val uiState: StateFlow<TileUiState> = _uiState.asStateFlow()

    fun updateTile() {
        _uiState.update { currentState ->
            currentState.copy(
                color = currentState.color.next()
            )
        }
    }

    fun setColor(color: TileColor) {
        _uiState.update { currentState ->
            currentState.copy(
                color = color
            )
        }
    }
}