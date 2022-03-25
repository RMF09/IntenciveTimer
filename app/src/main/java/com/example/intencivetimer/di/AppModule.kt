package com.example.intencivetimer.di

import android.app.Application
import android.app.SharedElementCallback
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.intencivetimer.data.ITDatabase
import com.example.intencivetimer.data.RewardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: ITDatabase.Callback
    ): ITDatabase = Room.databaseBuilder(app,ITDatabase::class.java,"it_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideReward(db: ITDatabase) : RewardDao = db.rewardDao()

    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope