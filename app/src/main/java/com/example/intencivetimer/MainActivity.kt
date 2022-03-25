package com.example.intencivetimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.intencivetimer.features.addeditreward.AddEditRewardScreen
import com.example.intencivetimer.features.rewardlist.RewardListScreen
import com.example.intencivetimer.features.timer.TimerScreen
import com.example.intencivetimer.core.ui.theme.IntenciveTimerTheme
import com.example.intencivetimer.features.addeditreward.ARG_REWARD_ID
import com.example.intencivetimer.features.addeditreward.NO_REWARD_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntenciveTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ScreenContent()
                }
            }
        }
    }
}

@Composable
fun ScreenContent() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavDestinations.forEach { bottomNavDestinations ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = bottomNavDestinations.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = stringResource(id = bottomNavDestinations.label))
                        },
                        selected =
                        currentDestination?.hierarchy?.any { it.route == bottomNavDestinations.route } == true,
                        onClick = {
                            navController.navigate(bottomNavDestinations.route) {
                                popUpTo(id = navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavDestinations[0].route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavDestinations.Timer.route) {
                TimerScreen(navController)
            }
            composable(BottomNavDestinations.RewardList.route) {
                RewardListScreen(navController)
            }
            composable(
                FullscreenDestinations.AddEditReward.route + "?$ARG_REWARD_ID={$ARG_REWARD_ID}",
                arguments = listOf(navArgument(ARG_REWARD_ID) {
                    type = NavType.LongType
                    defaultValue = NO_REWARD_ID
                })
            ) {
                AddEditRewardScreen(
                    navController
                )
            }
        }
    }

}

val bottomNavDestinations = listOf(
    BottomNavDestinations.Timer,
    BottomNavDestinations.RewardList,

    )

sealed class BottomNavDestinations(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object Timer : BottomNavDestinations(route = "timer", Icons.Outlined.Schedule, R.string.timer)
    object RewardList :
        BottomNavDestinations(route = "reward", Icons.Outlined.List, R.string.reward_list)
}

sealed class FullscreenDestinations(
    val route: String
) {
    object AddEditReward : FullscreenDestinations(route = "add_edit_reward")
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IntenciveTimerTheme {
        ScreenContent()
    }
}