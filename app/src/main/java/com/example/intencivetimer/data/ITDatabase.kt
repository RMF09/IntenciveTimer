package com.example.intencivetimer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.intencivetimer.core.IconKey
import com.example.intencivetimer.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Reward::class], version = 1)
abstract class ITDatabase : RoomDatabase() {

    abstract fun rewardDao(): RewardDao

    class Callback @Inject constructor(
        private val database: Provider<ITDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val rewardDao = database.get().rewardDao()

            applicationScope.launch {
                rewardDao.insert(
                    Reward(
                        iconKey = IconKey.CAKE,
                        name = "1 piece of cake",
                        chanceInPercent = 5
                    )
                )

                rewardDao.insert(
                    Reward(
                        iconKey = IconKey.BATH_TUB,
                        name = "Take a bath",
                        chanceInPercent = 7
                    )
                )

                rewardDao.insert(
                    Reward(
                        iconKey = IconKey.TV,
                        name = "Watch TV",
                        chanceInPercent = 10
                    )
                )

            }
        }
    }
}