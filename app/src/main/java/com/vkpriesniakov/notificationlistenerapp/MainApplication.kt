package com.vkpriesniakov.notificationlistenerapp

import android.app.Application
import com.vkpriesniakov.notificationlistenerapp.di.myDatabaseModules
import com.vkpriesniakov.notificationlistenerapp.di.repositoryModule
import com.vkpriesniakov.notificationlistenerapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@MainApplication)
            // use modules
            modules(myDatabaseModules,
                repositoryModule,
                viewModelModule
            )

        }
    }
}