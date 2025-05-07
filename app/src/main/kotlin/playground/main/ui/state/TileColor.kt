package playground.main.ui.state

import androidx.compose.ui.graphics.Color

sealed class TileColor{
    abstract val color: Color
    abstract val elementalTypes: Set<ElementalType>

    sealed class Primary(): TileColor()
    sealed class Secondary(): TileColor()
    sealed class Special(): TileColor()

    class SL(
        override val color: Color = Color.Red,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.SOLID, ElementalType.LIQUID)
    ): Primary()

    class SG(
        override val color: Color = Color.Cyan,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.SOLID, ElementalType.GAS)
    ): Primary()

    class SP(
        override val color: Color = Color.Yellow,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.SOLID, ElementalType.PLASMA)
    ): Primary()

    class LG(
        override val color: Color = Color.Magenta,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.LIQUID, ElementalType.GAS)
    ): Secondary()

    class LP(
        override val color: Color = Color(0xFFFFA500),
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.LIQUID, ElementalType.PLASMA)
    ): Secondary()

    class GP(
        override val color: Color = Color.Green,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.GAS, ElementalType.PLASMA)
    ): Secondary()

    class White(
        override val color: Color = Color.LightGray,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.GAS, ElementalType.PLASMA)
    ): Special()

    class Black(
        override val color: Color = Color.Black,
        override val elementalTypes: Set<ElementalType> = setOf(ElementalType.GAS, ElementalType.PLASMA)
    ): Special()

    fun next(): TileColor {
        return when (this) {
            is TileColor.SL -> TileColor.SG()
            is TileColor.SG -> TileColor.SP()
            is TileColor.SP -> TileColor.SL()
            is TileColor.LP -> TileColor.LG()
            is TileColor.LG -> TileColor.GP()
            is TileColor.GP -> TileColor.LP()
            is TileColor.White -> TileColor.SG()
            is TileColor.Black -> TileColor.SG()
        }
    }

    fun previous(): TileColor {
        return when (this) {
            is TileColor.SL -> TileColor.SP()
            is TileColor.SG -> TileColor.SL()
            is TileColor.SP -> TileColor.SG()
            is TileColor.LP -> TileColor.GP()
            is TileColor.LG -> TileColor.LP()
            is TileColor.GP -> TileColor.LG()
            is TileColor.White -> TileColor.SL()
            is TileColor.Black -> TileColor.SP()
        }
    }

    fun promote(): TileColor {
        return when (this) {
            is TileColor.SL -> TileColor.LP()
            is TileColor.SG -> TileColor.LG()
            is TileColor.SP -> TileColor.GP()
            is TileColor.LP -> TileColor.SP()
            is TileColor.LG -> TileColor.SL()
            is TileColor.GP -> TileColor.SG()
            is TileColor.White -> TileColor.Black()
            is TileColor.Black -> TileColor.White()
        }
    }

    fun switch(tileColor: TileColor): TileColor {
        return when (tileColor) {
            is SL -> SL()
            is SG -> SG()
            is SP -> SP()
            is LG -> LG()
            is LP -> LP()
            is GP -> GP()
            else -> SL()
        }
    }

    companion object {
        fun matchType(elementalTypes: Set<ElementalType>): TileColor {
            if (elementalTypes.containsAll(SL().elementalTypes)) return SL()
            if (elementalTypes.containsAll(SG().elementalTypes)) return SG()
            if (elementalTypes.containsAll(SP().elementalTypes)) return SP()
            if (elementalTypes.containsAll(LG().elementalTypes)) return LG()
            if (elementalTypes.containsAll(LP().elementalTypes)) return LP()
            if (elementalTypes.containsAll(GP().elementalTypes)) return GP()
            return SL()
        }
    }

}
