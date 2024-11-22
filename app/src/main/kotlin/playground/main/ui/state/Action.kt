package playground.main.ui.state

sealed class Action {
    abstract val atbCost: Int
    abstract val waitTime: Int

    sealed class Move(
        override val atbCost: Int,
        override val waitTime: Int
    ): Action()
    sealed class Ability(): Action()

   class Cycle(
       override val atbCost: Int = 2,
       override val waitTime: Int = 1
   ): Ability()

    class Transmute(
        override val atbCost: Int = 2,
        override val waitTime: Int = 2
    ): Ability()
}