package playground.main.ui.state

data class TileUiState(
    var color: TileColor = TileColor.Blue(),
    var character: CharacterType = CharacterType.NONE
)