package com.vkpriesniakov.notificationlistenerapp.persistence

import androidx.room.*
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.utils.DATABASE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM $DATABASE_NAME ORDER BY ntfDate DESC")
    fun getAllNotificationsList(): Flow<List<MyNotification>>

    @Query("SELECT * FROM $DATABASE_NAME WHERE ntfId = :notificationId")
    fun getNotificationById(notificationId:String):Flow<MyNotification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(vararg myNotification: MyNotification)

    @Delete
    fun deleteNotification(myNotification: MyNotification)

    @Query("DELETE FROM $DATABASE_NAME")
    fun deleteAllNotifications()

    @Query("SELECT * FROM $DATABASE_NAME WHERE ntfDate/1000 >= (strftime('%s', 'now') - :timeFilter) ORDER BY ntfDate DESC")
    fun getNotificationsByFilter(timeFilter:Long): Flow<List<MyNotification>>

}