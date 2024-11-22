package playground.main.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import playground.main.ui.state.CountUiState
import javax.inject.Inject

@HiltViewModel
class CountViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(CountUiState())
    val uiState: StateFlow<CountUiState> = _uiState.asStateFlow()

    fun incrementCount() {
        _uiState.update { currentState->
            currentState.copy(
                 currentState.currentCount + 1
            )
        }
    }
}