package playground.main.ui.state

data class PlayerState(
    val playerNumber: Int,
    val maxHP: Int = 100,
    val maxMove: Int = 100,
    val maxATB: Int = 100,
    val currentHP: Int = maxHP,
    val currentMove: Int = 0,
    val currentATB: Int = 0,
    val currentTile: Int = 0,
    val primaryElementalType: ElementalType,
    val secondaryElementalType: ElementalType
)