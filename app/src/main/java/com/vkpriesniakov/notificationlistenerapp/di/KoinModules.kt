package com.vkpriesniakov.notificationlistenerapp.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.vkpriesniakov.notificationlistenerapp.persistence.AppDatabase
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import com.vkpriesniakov.notificationlistenerapp.sharedpreferences.PreferencesRepository
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainRepository
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val myDatabaseModules = module {

    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    fun provideNotificationsDao(appDatabase: AppDatabase): NotificationDao {
        return appDatabase.notificationDao()
    }

    single { provideAppDatabase(androidApplication()) }
    single { provideNotificationsDao(get()) }

}

    val repositoryModule = module{

        fun provideNotificationRepository(dao: NotificationDao):NotificationMainRepository{
            return NotificationMainRepository(dao)
        }
        single { provideNotificationRepository(get()) }

    }

    val viewModelModule = module{

        single { PreferencesRepository(androidContext()) }
        viewModel { NotificationMainViewModel(get (), get()) }

    }