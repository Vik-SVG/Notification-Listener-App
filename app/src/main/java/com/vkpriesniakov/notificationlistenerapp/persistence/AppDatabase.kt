package com.vkpriesniakov.notificationlistenerapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.utils.DATABASE_NAME

@Database(entities = [MyNotification::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun notificationDao():NotificationDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance:AppDatabase? = null

        fun getInstance(context: Context
        ): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .build()
        }

    }

}