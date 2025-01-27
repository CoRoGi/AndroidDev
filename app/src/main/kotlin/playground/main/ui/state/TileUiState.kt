package playground.main.ui.state

data class TileUiState(
    val color: TileColor = TileColor.Blue(),
    val character: CharacterType = CharacterType.NONE
)