package com.vkpriesniakov.notificationlistenerapp.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.vkpriesniakov.notificationlistenerapp.persistence.AppDatabase
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainRepository
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin


val myDatabaseModules = module {

    fun provideAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
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

        single { SavedStateHandle() }
        viewModel { NotificationMainViewModel(get (), get()) }

    }