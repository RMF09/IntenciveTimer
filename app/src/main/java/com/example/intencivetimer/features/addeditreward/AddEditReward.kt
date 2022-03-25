package com.example.intencivetimer.features.addeditreward

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.intencivetimer.R
import com.example.intencivetimer.core.IconKey
import com.example.intencivetimer.core.defaultRewardIcon
import com.example.intencivetimer.core.ui.composables.ITIconButton
import com.example.intencivetimer.core.ui.theme.IntenciveTimerTheme
import com.example.intencivetimer.core.utils.exhaustive
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collect

interface AddEditRewardActions {
    fun onRewardNameChanged(name: String)
    fun onRewardChanceChange(value: Int)
    fun onSaveClicked()
    fun onRewardIconButtonClicked()
    fun onRewardIconSelected(iconKey: IconKey)
    fun onRewardIconDialogDismissRequest()
}

@Composable
fun AddEditRewardScreen(navController: NavController) {
    val viewModel: AddEditRewardViewModel = hiltViewModel()
    val rewardName by viewModel.rewardName.observeAsState("")
    Log.e("TAG", "AddEditRewardScreen: $rewardName", )
    val rewardNameIsError by viewModel.rewardNameIsError.observeAsState(false)

    val rewardChanceInPercent by viewModel.rewardChance.observeAsState(0)
    val isEditMode = viewModel.isEditMode
    val rewardIconKeySelection by viewModel.rewardIconKeySelection.observeAsState(defaultRewardIcon)
    val showRewardIconSelectionDialog by
    viewModel.showRewardIconSelectionDialog.observeAsState(initial = false)


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddEditRewardViewModel.Event.RewardCreated -> navController.popBackStack()
                AddEditRewardViewModel.Event.RewardUpdated -> navController.popBackStack()
            }.exhaustive
        }
    }

    ScreenContent(
        isEditMode = isEditMode,
        rewardName = rewardName,
        rewardNameIsError = rewardNameIsError,
        rewardChanceInPercent = rewardChanceInPercent,
        rewardIconKeySelection = rewardIconKeySelection,
        showRewardIconSelectionDialog = showRewardIconSelectionDialog,
        onCloseClicked = { navController.popBackStack() },
        actions = viewModel
    )
}

@Composable
fun ScreenContent(
    isEditMode: Boolean,
    rewardName: String?,
    rewardChanceInPercent: Int,
    rewardIconKeySelection: IconKey,
    showRewardIconSelectionDialog: Boolean,
    onCloseClicked: () -> Unit,
    actions: AddEditRewardActions,
    rewardNameIsError: Boolean

) {
    Scaffold(
        topBar = {
            val appBarTitle = if (isEditMode) R.string.edit_reward else R.string.add_reward
            TopAppBar(
                title = {
                    Text(text = stringResource(id = appBarTitle))
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = actions::onSaveClicked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.add_new_reward),
                    tint = MaterialTheme.colors.background
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = rewardName ?: "",
                onValueChange = actions::onRewardNameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.reward_name)) },
                isError = rewardNameIsError
            )
            if (rewardNameIsError)
                Text(
                    text = stringResource(id = R.string.field_cant_be_blank),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error
                )

            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(id = R.string.chance) + ": $rewardChanceInPercent%")
            Slider(
                value = rewardChanceInPercent.toFloat() / 100,
                onValueChange = { value ->
                    actions.onRewardChanceChange((value * 100).toInt())
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ITIconButton(
                onClick = actions::onRewardIconButtonClicked,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = rewardIconKeySelection.rewardIcon,
                    contentDescription = stringResource(id = R.string.select_icon),
                    tint = if (isSystemInDarkTheme()) Color.LightGray else Color.Black
                )
            }

        }
    }
    if (showRewardIconSelectionDialog)
        RewardIconSelectionDialog(
            onDismissRequest = actions::onRewardIconDialogDismissRequest,
            onIconSelected = actions::onRewardIconSelected
        )
}

@Composable
private fun RewardIconSelectionDialog(
    onDismissRequest: () -> Unit,
    onIconSelected: (iconKey: IconKey) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(id = R.string.select_icon),
                color = MaterialTheme.colors.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                FlowRow {
                    IconKey.values().forEach { iconKey ->
                        IconButton(
                            onClick = {
                                onIconSelected(iconKey)
                                onDismissRequest()
                            }) {
                            Icon(
                                imageVector = iconKey.rewardIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(46.dp)
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        },
        buttons = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 6.dp,
                        bottom = 6.dp,
                        end = 6.dp
                    )
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )

}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Preview() {
    IntenciveTimerTheme {
        Surface(color = MaterialTheme.colors.background) {
            ScreenContent(
                isEditMode = false,
                rewardName = "Example Reward",
                rewardNameIsError = false,
                rewardChanceInPercent = 20,
                onCloseClicked = {},
                showRewardIconSelectionDialog = false,
                rewardIconKeySelection = defaultRewardIcon,
                actions = object : AddEditRewardActions {
                    override fun onRewardNameChanged(name: String) {}
                    override fun onRewardChanceChange(value: Int) {}
                    override fun onSaveClicked() {}
                    override fun onRewardIconButtonClicked() {}
                    override fun onRewardIconSelected(iconKey: IconKey) {}
                    override fun onRewardIconDialogDismissRequest() {}
                }
            )
        }
    }
}