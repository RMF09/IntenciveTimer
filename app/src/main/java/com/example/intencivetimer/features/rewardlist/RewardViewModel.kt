package com.example.intencivetimer.features.rewardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.intencivetimer.data.RewardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val rewardDao: RewardDao
) : ViewModel() {

    /*private val dummyRewardsLiveData = MutableLiveData<List<Reward>>()
    val dummyReward: LiveData<List<Reward>> = dummyRewardsLiveData

    init {
        val dummyList = mutableListOf<Reward>()

        repeat(50) { index ->
            dummyList +=
                Reward(
                    iconKey = Icons.Outlined.Star,
                    title = "Star",
                    chanceInPercent = index
                )
        }
        dummyRewardsLiveData.value = dummyList
    }*/

    val rewards = rewardDao.getAllRewards().asLiveData()


}