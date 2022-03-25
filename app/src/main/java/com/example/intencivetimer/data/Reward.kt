package com.example.intencivetimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.intencivetimer.core.IconKey

@Entity(tableName = "reward")
data class Reward(
    val iconKey: IconKey,
    val name: String,
    val chanceInPercent: Int,
    val isUnlocked: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
