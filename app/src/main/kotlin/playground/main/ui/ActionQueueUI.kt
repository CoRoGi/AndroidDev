package playground.main.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import playground.main.ui.vm.ActionsViewModel
import playground.main.ui.vm.TileViewModel
import java.util.UUID

private const val SCROLL_DX = 24f
private const val REQUIRED_CARD_COUNT = 10

private class AutoScrollItem<T>(
    val id: String = UUID.randomUUID().toString(),
    val data: T
)

@Composable
fun <T : Any> AutoScrollingLazyRow(
    list: List<T>,
    modifier: Modifier = Modifier,
    actionViewModel: ActionsViewModel = hiltViewModel(),
    tileVMs: List<TileViewModel> = hiltViewModel(),
    itemContent: @Composable (item: T) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var items by remember { mutableStateOf(list.mapAutoScrollItem()) }

    val currentActions by actionViewModel.committedActions.collectAsStateWithLifecycle()

    val currentTurn by actionViewModel.currentTurn.collectAsStateWithLifecycle()

    var count by remember { mutableStateOf(0) }

    LazyRow(
        state = lazyListState,
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(
            items, key = { _, item -> item.id }
        ) { index, item ->
            itemContent(item.data)
            println("item: ${item.data}")

            val listState = rememberLazyListState()

            val isScrolledOff by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                }
            }

            LaunchedEffect(isScrolledOff) {
//                println("Scrolled Off: ${currentTurn.value}")
//                if (currentTurn.value <= 5) {
//                    actionViewModel.nextTurn()
//                }
            }

            LaunchedEffect(listState) {
                snapshotFlow {
                    listState.firstVisibleItemIndex
                }.collect {
                    println("firstVisibleItemIndexChanged: ${currentTurn} ${listState.firstVisibleItemScrollOffset}")
                    if (currentTurn <= 5) {
                        if (count < 5) {
                            count++
                        } else {
                            currentActions[currentTurn]!!.forEach { action ->
                                tileVMs[action.second].handleAction(action.first)
                                delay(1000)
                            }
                            delay(3500)
                            actionViewModel.nextTurn()
                        }
                    }
                }

//                currentActions.value[1]!!.forEach { action ->
//                    println(action)
//                }
            }

            if (index == items.lastIndex) {
                val currentList = items
                val firstVisibleItemIndex = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                val secondPart = currentList.subList(0, firstVisibleItemIndex.value)
                val firstPart =
                    currentList.subList(firstVisibleItemIndex.value, currentList.size)

                LaunchedEffect(key1 = Unit) {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(
                            0,
                            maxOf(0, lazyListState.firstVisibleItemScrollOffset - SCROLL_DX.toInt())
                        )
                    }
//                    println("Scrolled and current Turn is ${currentTurn.value}")
//                    if (currentTurn.value <= 5) {
//                        actionViewModel.nextTurn()
//                    }
//                    println("next Turn")
//                    actionViewModel.nextTurn()
                }

                println("updated items list on turn ${currentTurn}")
//                items = (firstPart + secondPart)
                items += items
            }

        }
    }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            while (true) {
                lazyListState.autoScroll()
            }
        }
    }
}

private fun <T : Any> List<T>.mapAutoScrollItem(): List<AutoScrollItem<T>> {
    val newList = this.map { AutoScrollItem(data = it) }.toMutableList()
    var index = 0
    if (this.size < REQUIRED_CARD_COUNT) {
        while (newList.size != REQUIRED_CARD_COUNT) {
            println("new list: ${newList.size}")
            if (index > this.size - 1) {
                index = 0
            }

            newList.add(AutoScrollItem(data = this[index]))
            index++
            println("new list after : ${newList.size} index $index")
        }
    }
    return newList
}

suspend fun ScrollableState.autoScroll(
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 500, easing = LinearEasing)
) {
    var previousValue = 0f
    scroll(MutatePriority.PreventUserInput) {
        animate(0f, SCROLL_DX, animationSpec = animationSpec) { currentValue, _ ->
            previousValue += scrollBy(currentValue - previousValue)
        }
    }
}
