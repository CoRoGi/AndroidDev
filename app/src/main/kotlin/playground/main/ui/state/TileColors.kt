package playground.main.ui.state

import androidx.compose.ui.graphics.Color


sealed class TileColor{
    abstract val color: Color

    sealed class Primary(): TileColor()
    sealed class Secondary(): TileColor()
    sealed class Special(): TileColor()

    class Red(
        override val color: Color = Color.Red
    ): Primary()

    class Blue(
        override val color: Color = Color.Blue
    ): Primary()

    class Yellow(
        override val color: Color = Color.Yellow
    ): Primary()

    class Purple(
        override val color: Color = Color.Magenta
    ): Secondary()

    class Orange(
        override val color: Color = Color(0xFFFFA500)
    ): Secondary()

    class Green(
        override val color: Color = Color.Green
    ): Secondary()

    class White(
        override val color: Color = Color.LightGray
    ): Special()

    class Black(
        override val color: Color = Color.Black
    ): Special()

    fun next(): TileColor {
        return when (this) {
            is TileColor.Red -> TileColor.Blue()
            is TileColor.Blue -> TileColor.Yellow()
            is TileColor.Yellow -> TileColor.Red()
            is TileColor.Orange -> TileColor.Purple()
            is TileColor.Purple -> TileColor.Green()
            is TileColor.Green -> TileColor.Orange()
            is TileColor.White -> TileColor.Blue()
            is TileColor.Black -> TileColor.Blue()
        }
    }

    fun switch(): TileColor {
        return when (this) {
            is TileColor.Red -> TileColor.Orange()
            is TileColor.Blue -> TileColor.Purple()
            is TileColor.Yellow -> TileColor.Green()
            is TileColor.Orange -> TileColor.Yellow()
            is TileColor.Purple -> TileColor.Red()
            is TileColor.Green -> TileColor.Blue()
            is TileColor.White -> TileColor.Black()
            is TileColor.Black -> TileColor.White()
        }
    }
}





