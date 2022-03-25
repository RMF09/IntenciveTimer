package com.example.intencivetimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {

    @Query("SELECT * FROM reward")
    fun getAllRewards() : Flow<List<Reward>>

    @Query("SELECT * FROM reward WHERE id=:rewardId")
    suspend fun getRewardById(rewardId: Long): Reward

    @Insert(onConflict = REPLACE)
    suspend fun insert(reward: Reward)

    @Update
    suspend fun update(reward: Reward)
}