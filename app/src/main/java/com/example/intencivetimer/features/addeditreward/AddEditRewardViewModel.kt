package com.example.intencivetimer.features.addeditreward

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intencivetimer.core.IconKey
import com.example.intencivetimer.core.defaultRewardIcon
import com.example.intencivetimer.data.Reward
import com.example.intencivetimer.data.RewardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRewardViewModel @Inject constructor(
    private val rewardDao: RewardDao,
    savedStateHandle: SavedStateHandle
) : ViewModel(), AddEditRewardActions {

    private val rewardID = savedStateHandle.get<Long>(ARG_REWARD_ID)
    private var reward: Reward? = null

    val isEditMode = rewardID != NO_REWARD_ID


    private val rewardNameLiveData = savedStateHandle.getLiveData<String>("rewardNameLiveData")
    val rewardName: LiveData<String> get() = rewardNameLiveData

    private val rewardNameIsErrorLiveData = savedStateHandle.getLiveData("rewardNameIsError", false)
    val rewardNameIsError: LiveData<Boolean> get() = rewardNameIsErrorLiveData

    private val rewardChanceLiveData = savedStateHandle.getLiveData<Int>("rewardChanceLiveData")
    val rewardChance: LiveData<Int> get() = rewardChanceLiveData

    private val rewardIconSelectionLiveData = savedStateHandle.getLiveData<IconKey>(
        "rewardIconSelectionLiveData"
    )
    val rewardIconKeySelection: LiveData<IconKey> get() = rewardIconSelectionLiveData

    private val showRewardIconSelectionDialogLiveData =
        savedStateHandle.getLiveData("dialog", false)
    val showRewardIconSelectionDialog: LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()


    init {
        if (rewardID != null && rewardID != NO_REWARD_ID) {
            viewModelScope.launch {
                reward = rewardDao.getRewardById(rewardId = rewardID)
                Log.e("Reward", "$reward: $rewardID")
                populateEmptyInputValuesWithRewardData()
            }
        } else {
            populateInputValuesWithDefaultValues()

        }
    }

    private fun populateEmptyInputValuesWithRewardData() {
        val reward = reward
        if (reward != null) {
            Log.e(
                "TAG",
                "populateEmptyInputValuesWithRewardData: ${rewardNameLiveData.value} r: $reward",
            )
            if (rewardNameLiveData.value == null) {
                rewardNameLiveData.value = reward.name
            }
            if (rewardChanceLiveData.value == null) {
                rewardChanceLiveData.value = reward.chanceInPercent
            }
            if (rewardIconSelectionLiveData.value == null) {
                rewardIconSelectionLiveData.value = reward.iconKey
            }

        }
    }

    private fun populateInputValuesWithDefaultValues() {
        rewardNameLiveData.value = ""
        rewardChanceLiveData.value = 10
        rewardIconSelectionLiveData.value = defaultRewardIcon

    }

    override fun onRewardNameChanged(name: String) {
        rewardNameLiveData.value = name
    }

    override fun onRewardChanceChange(value: Int) {
        rewardChanceLiveData.value = value
    }

    override fun onSaveClicked() {
        val rewardNameInput = rewardName.value
        val rewardChanceInput = rewardChance.value
        val rewardIconKeySelection = rewardIconKeySelection.value

        rewardNameIsErrorLiveData.value = false

        Log.e("TAG", "onSaveClicked:$rewardNameInput, $rewardChanceInput, $rewardID")

        viewModelScope.launch {
            if (!rewardNameInput.isNullOrBlank() && rewardChanceInput != null && rewardIconKeySelection != null) {
                if (rewardID != NO_REWARD_ID) {
                    reward?.let {
                        updateReward(
                            it.copy(
                                name = rewardNameInput,
                                chanceInPercent = rewardChanceInput,
                                iconKey = rewardIconKeySelection
                            )
                        )
                    }
                } else
                    createReward(Reward(rewardIconKeySelection, rewardNameInput, rewardChanceInput))
            } else {
                if (rewardNameInput.isNullOrBlank()) {
                    rewardNameIsErrorLiveData.value = true
                }
            }
        }
    }

    override fun onRewardIconButtonClicked() {
        showRewardIconSelectionDialogLiveData.value = true
    }

    override fun onRewardIconSelected(iconKey: IconKey) {
        rewardIconSelectionLiveData.value = iconKey
    }

    override fun onRewardIconDialogDismissRequest() {
        showRewardIconSelectionDialogLiveData.value = false
    }

    private suspend fun updateReward(reward: Reward) {
        rewardDao.update(reward)
        eventChannel.send(Event.RewardUpdated)
        // TODO: 22/03/2022 Show confirmation message

    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insert(reward = reward)
        eventChannel.send(Event.RewardCreated)
        // TODO: 22/03/2022 Show confirmation message
    }

    sealed class Event {
        object RewardCreated : Event()
        object RewardUpdated : Event()
    }
}

const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L