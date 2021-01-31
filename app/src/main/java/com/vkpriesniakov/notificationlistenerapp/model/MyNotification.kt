package com.vkpriesniakov.notificationlistenerapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vkpriesniakov.notificationlistenerapp.utils.DATABASE_NAME

@Entity (tableName = DATABASE_NAME)
data class MyNotification (@PrimaryKey (autoGenerate = true)
                           val ntfId:Int? = 0,
                            val ntfTitle:String?,
                            val ntfMessage:String?,
                            val ntfPackage: String?,
                            val ntfDate: Long?)