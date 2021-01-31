package com.vkpriesniakov.notificationlistenerapp.service

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.persistence.AppDatabase
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import org.koin.android.ext.android.inject


class MainListenerService : NotificationListenerService() {

    private val TAG = "MyNotificationListener"


    override fun onNotificationPosted(sbn: StatusBarNotification?) {

        val mDao:NotificationDao by inject()

        //   to send intent
        Log.i(TAG, "Received ID " + sbn!!.id)

        //TODO: enable Notification transition to Room

        val myNotification = MyNotification( 0,
            sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString(),
            sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString(),
            sbn.packageName,
            sbn.postTime)

      //  val appContext:Context = application
       // val db = AppDatabase.getInstance(appContext)
        //val ntfDao = db.notificationDao()

        mDao.insertNotification(myNotification)

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i(TAG, "Removed ID " + sbn!!.id)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}