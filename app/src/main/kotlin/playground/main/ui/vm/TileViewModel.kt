package playground.main.ui.vm

//@HiltViewModel
//class TileViewModel @Inject constructor(): ViewModel() {
//    private val _uiState = MutableStateFlow(TileUiState())
//    val uiState: StateFlow<TileUiState> = _uiState.asStateFlow()
//
//    fun updateTile(effect: PlayerActionEffect) {
//        _uiState.update { currentState ->
//            when (effect) {
//                is Next -> {
//                    currentState.copy(
//                        color = currentState.color.next()
//                    )
//                }
//                is Previous -> {
//                    currentState.copy(
//                        color = currentState.color.previous()
//                    )
//                }
//                is Promote -> {
//                    currentState.copy(
//                        color = currentState.color.promote()
//                    )
//                }
//                is Demote -> {
//                    // TODO: Replace with demote method
//                    currentState.copy(
//                        color = currentState.color.promote()
//                    )
//                }
//            }
//        }
//    }
//
//    fun setColor(color: TileColor) {
//        _uiState.update { currentState ->
//            currentState.copy(color = color)
//        }
//    }
//
//    fun handleAction(action: PlayerAction) {
//        when (action) {
//            is PlayerAction.AtbAction -> {
//                println("Handling action: ${action.name}")
//                    updateTile(effect = action.effect)
//                }
//            is PlayerAction.MoveAction -> {}
//        }
//    }
//
//    fun setPlayer() {
//        _uiState.update {
//            it.copy(character = CharacterType.PLAYER)
//        }
//    }
//}