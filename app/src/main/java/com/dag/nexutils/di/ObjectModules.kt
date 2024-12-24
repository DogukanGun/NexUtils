package com.dag.nexutils.di

import android.content.Context
import com.dag.nexutils.base.AlertDialogManager
import com.dag.nexutils.base.UITextHelper
import com.dag.nexutils.base.navigation.DefaultNavigator
import com.dag.nexutils.base.navigation.Destination
import com.dag.nexutils.domain.DataPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ObjectModules {

    @Provides
    @Singleton
    fun provideDataPreferencesStore(
        @ApplicationContext context: Context
    ): DataPreferencesStore {
        return DataPreferencesStore(context)
    }

    @Provides
    @Singleton
    fun provideDefaultNavigator(): DefaultNavigator {
        return DefaultNavigator(startDestination = Destination.HomeScreen)
    }

    @Provides
    @Singleton
    fun provideUITextHelper(@ApplicationContext context: Context): UITextHelper {
        return UITextHelper(context)
    }

    @Provides
    @Singleton
    fun provideAlertDialogManager(): AlertDialogManager {
        return AlertDialogManager()
    }
}