package playground.main.ui.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class EffectPattern {
    abstract val range: Int
}

@Serializable
@SerialName("hline")
data class HLine(override val range: Int): EffectPattern()

@Serializable
@SerialName("vline")
data class VLine(override val range: Int): EffectPattern()

@Serializable
@SerialName("circle")
data class Circle(override val range: Int): EffectPattern()

@Serializable
@SerialName("single")
data class Single(override val range: Int = 1): EffectPattern()
