package com.vkpriesniakov.notificationlistenerapp.ui.main


import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import kotlinx.coroutines.flow.Flow

interface NotificationMainInterface{
    suspend fun getAllNotificationsList(): Flow<List<MyNotification>>

    suspend fun getNotificationById(notificationId:String):Flow<MyNotification>

    suspend fun insertNotification(myNotification: MyNotification)

    suspend fun deleteNotification(myNotification: MyNotification)
}


class NotificationMainRepository(private val database: NotificationDao) :NotificationMainInterface{

    val allNotifications = database.getAllNotificationsList()

    override suspend fun getAllNotificationsList(): Flow<List<MyNotification>> = database.getAllNotificationsList()

    override suspend fun getNotificationById(notificationId: String): Flow<MyNotification> = database.getNotificationById(notificationId)

    override suspend fun insertNotification(myNotification: MyNotification) = database.insertNotification(myNotification)

    override suspend fun deleteNotification(myNotification: MyNotification) = database.deleteNotification(myNotification)
}