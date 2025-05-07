package playground.main.ui.state

data class TileUiState(
    var color: TileColor = TileColor.SG(),
    var elementalType: ElementalType = ElementalType.SOLID,
    var character: CharacterType = CharacterType.NONE
)