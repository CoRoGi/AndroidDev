package playground.main.ui

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import playground.main.ui.model.TileModel
import playground.main.ui.state.PlayerAction
import playground.main.ui.state.TileUiState
import playground.main.ui.vm.BattleViewModel
import kotlin.reflect.typeOf

@Serializable
object Player1

@Serializable
object Player2

@Serializable
object PlayerActions

@Serializable
data class Target(
    val action: PlayerAction,
    val player: Int
)

@Composable
fun PlayerTabs(
    tabs: List<String>,
    contentScreens: List<@Composable () -> Unit> = listOf(),
    playerActionContent: List<@Composable () -> Unit> = listOf(),
    navController: NavHostController,
    tilePairs: SnapshotStateList<Pair<TileModel, TileUiState>>,
    battleViewModel: BattleViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val ActionParameter = object : NavType<PlayerAction>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): PlayerAction? {
            return bundle.getString(key)?.let { parseValue(it) }
        }

        override fun parseValue(value: String): PlayerAction {
            return Json.decodeFromString(value)
        }

        override fun put(bundle: Bundle, key: String, value: PlayerAction) {
            bundle.putString(key, serializeAsValue(value))
        }

        override fun serializeAsValue(value: PlayerAction): String {
            return Json.encodeToString(value)
        }
    }

    Column {
        TabRow(
            selectedTabIndex = 0,
            containerColor = Color.Gray,
            contentColor = Color.White,
            indicator = { tabPositions ->
                // Indicator for the selected tab
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.DarkGray
                )
            }
        ) {
            // Iterate through each tab title and create a tab
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = 16.dp),
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index

                        when (selectedTabIndex) {
                            0 -> {
                                navController.navigate(Player1)
                            }

                            1 -> {
                                navController.navigate(Player2)
                            }
                        }
                    }
                ) {
                    Column {
                        // Text displayed on the tab
                        Text(text = tabTitle)
                        contentScreens.getOrNull(index)?.invoke()
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = Player1
        ) {
            navigation<Player1>(
                startDestination = PlayerActions
            ) {
                composable<PlayerActions> {
                    // Display the content screen corresponding to the selected tab
                    Box(modifier = Modifier.fillMaxHeight(0.9f)) {
                        playerActionContent.getOrNull(0)?.invoke()
                    }
                }
                composable<Target>(
                    typeMap = mapOf(typeOf<PlayerAction>() to ActionParameter)
                ) {
                    val args = it.toRoute<Target>()
                    Box(modifier = Modifier.fillMaxHeight(0.9f)) {
                        TargetSelectionUI(
                            tilePairs.filter { tilePair -> tilePair.first.show }
                                .map { shownTile -> shownTile.first },
                            action = args.action,
                            player = 1,
                            navController = navController,
                            battleViewModel
                        )
                    }
                }
            }

            navigation<Player2>(
                startDestination = PlayerActions
            ) {
                composable<PlayerActions> {
                    // Display the content screen corresponding to the selected tab
                    Box(modifier = Modifier.fillMaxHeight(0.9f)) {
                        playerActionContent.getOrNull(1)?.invoke()
                    }
                }
                composable<Target>(
                    typeMap = mapOf(typeOf<PlayerAction>() to ActionParameter)
                ) {
                    val args = it.toRoute<Target>()
                    Box(modifier = Modifier.fillMaxHeight(0.9f)) {
                        TargetSelectionUI(
                            tilePairs
                                .filter { tilePair ->
                                    tilePair.first.show
                                }.map { shownTile ->
                                    shownTile.first
                                },
                            action = args.action,
                            player = 2,
                            navController = navController,
                            battleViewModel = battleViewModel
                        )
                    }
                }
            }

        }
    }
}
