package com.vkpriesniakov.notificationlistenerapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MainListenerService : NotificationListenerService() {

    private val TAG = "MyNotificationListener"

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        //   to send intent
        Log.i(TAG, "Received ID " + sbn!!.id)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i(TAG, "Removed ID " + sbn!!.id)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val stringExtra = p1?.getStringExtra("NotificationListenerExample") //TODO: make receiver
    }

}