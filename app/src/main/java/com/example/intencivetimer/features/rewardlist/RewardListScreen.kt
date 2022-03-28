package com.example.intencivetimer.features.rewardlist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.intencivetimer.FullscreenDestinations
import com.example.intencivetimer.R
import com.example.intencivetimer.core.IconKey
import com.example.intencivetimer.core.ui.theme.IntenciveTimerTheme
import com.example.intencivetimer.core.ui.theme.listBottomPadding
import com.example.intencivetimer.data.Reward
import com.example.intencivetimer.features.addeditreward.ADD_EDIT_REWARD_RESULT
import com.example.intencivetimer.features.addeditreward.ARG_REWARD_ID
import com.example.intencivetimer.features.addeditreward.RESULT_REWARD_ADDED
import com.example.intencivetimer.features.addeditreward.RESULT_REWARD_UPDATED
import kotlinx.coroutines.launch

@Composable
fun RewardListScreen(
    navController: NavController,
    viewModel: RewardViewModel = hiltViewModel()
) {
    //val dummyList by viewModel.dummyReward.observeAsState(listOf())
    val rewards by viewModel.rewards.observeAsState(listOf())

    val addEditRewardResult =
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            ADD_EDIT_REWARD_RESULT
        )?.observeAsState()

    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = addEditRewardResult) {
        addEditRewardResult?.value?.let {
            when (it) {
                RESULT_REWARD_ADDED -> {
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_added))
                }
                RESULT_REWARD_UPDATED ->
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_updated))
            }
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>(
                ADD_EDIT_REWARD_RESULT
            )
        }
    }


    ScreenContent(
        rewards = rewards,
        onAddNewRewardClicked = { navController.navigate(FullscreenDestinations.AddEditReward.route) },
        onRewardItemClicked = { id ->
            navController.navigate(FullscreenDestinations.AddEditReward.route + "?$ARG_REWARD_ID=$id")
        },
        scaffoldState = scaffoldState
    )
}

@Composable
private fun ScreenContent(
    rewards: List<Reward>,
    onAddNewRewardClicked: () -> Unit,
    onRewardItemClicked: (Long) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.reward_list))
            })
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewRewardClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_new_reward),
                    tint = MaterialTheme.colors.background
                )
            }
        }
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = listBottomPadding
                ),
                state = listState
            ) {
                items(rewards) { reward ->
                    RewardItem(
                        reward = reward,
                        onRewardItemClicked = { id ->
                            onRewardItemClicked(id)
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = listState.firstVisibleItemIndex > 5,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)

            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    backgroundColor = Color.Gray,
                    modifier = Modifier.padding(end = 16.dp, bottom = 80.dp)

                ) {
                    Icon(
                        tint = MaterialTheme.colors.background,
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = stringResource(id = R.string.scroll_to_top)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardItem(
    reward: Reward,
    onRewardItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onRewardItemClicked(reward.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = reward.iconKey.rewardIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
            )
            Column() {
                Text(
                    text = reward.name,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = stringResource(id = R.string.chance) + " : ${reward.chanceInPercent}%",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(name = "Light Mode Item", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode Item", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ItemPreview() {
    IntenciveTimerTheme {
        Surface(color = MaterialTheme.colors.background) {
            RewardItem(
                Reward(
                    iconKey = IconKey.STAR,
                    name = "Star",
                    chanceInPercent = 40
                ),
                onRewardItemClicked = {}
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    IntenciveTimerTheme {
        Surface(color = MaterialTheme.colors.background) {
            ScreenContent(
                rewards = listOf(
                    Reward(
                        iconKey = IconKey.CAKE,
                        name = "Star",
                        chanceInPercent = 1
                    ),
                    Reward(
                        iconKey = IconKey.BATH_TUB,
                        name = "Star",
                        chanceInPercent = 2
                    )
                ),
                onAddNewRewardClicked = {},
                onRewardItemClicked = {}
            )
        }
    }
}

