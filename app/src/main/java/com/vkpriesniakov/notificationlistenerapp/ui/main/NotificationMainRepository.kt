package com.vkpriesniakov.notificationlistenerapp.ui.main


import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import com.vkpriesniakov.notificationlistenerapp.utils.DAY_SEC
import com.vkpriesniakov.notificationlistenerapp.utils.HOUR_SEC
import kotlinx.coroutines.flow.Flow

interface NotificationMainInterface{

    suspend fun getAllNotificationsList(): Flow<List<MyNotification>>

    suspend fun getNotificationById(notificationId:String):Flow<MyNotification>

    suspend fun insertNotification(myNotification: MyNotification)

    suspend fun deleteNotification(myNotification: MyNotification)

    suspend fun deleteAllNotifications()

}


class NotificationMainRepository(private val database: NotificationDao) :NotificationMainInterface{

    val allNotifications = database.getAllNotificationsList()

    val allNotificationsPerHour = database.getNotificationsByFilter(HOUR_SEC)

    val allNotificationsPerDay = database.getNotificationsByFilter(DAY_SEC)

    val allNotificationsPerMonth = database.getNotificationsByFilter(DAY_SEC)

    override suspend fun getAllNotificationsList(): Flow<List<MyNotification>> = database.getAllNotificationsList()

    override suspend fun getNotificationById(notificationId: String): Flow<MyNotification> = database.getNotificationById(notificationId)

    override suspend fun insertNotification(myNotification: MyNotification) = database.insertNotification(myNotification)

    override suspend fun deleteNotification(myNotification: MyNotification) = database.deleteNotification(myNotification)

    override suspend fun deleteAllNotifications() { database.deleteAllNotifications()}

}