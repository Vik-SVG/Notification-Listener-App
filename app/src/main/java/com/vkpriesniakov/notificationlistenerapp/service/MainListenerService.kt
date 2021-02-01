package com.vkpriesniakov.notificationlistenerapp.service

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.lang.ref.WeakReference


class MainListenerService : NotificationListenerService() {

    private val TAG = "MyNotificationListener"


    override fun onNotificationPosted(sbn: StatusBarNotification) {

        Log.i(TAG, "Got ID ${sbn.id}")

        //TODO: memory leaks spotted. Require refactoring

        startWorker(sbn)

    }

    private fun startWorker(sbn: StatusBarNotification) {

        val title = WeakReference(sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        val text = WeakReference(sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString())
        val pckName = WeakReference(sbn.packageName)
        val date = WeakReference(sbn.postTime)

        val builder = Data.Builder().also {
            it.putString("title", title.get())
            it.putString("text", text.get())
            it.putString("pack", pckName.get())
            it.putLong("date", date.get()!!)
        }

        val workManager = WorkManager.getInstance(this)
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(builder.build())
            .build()
        workManager.enqueue(workRequest)

        title.clear()
        text.clear()
        pckName.clear()
        date.clear()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i(TAG, "Removed ID " + sbn!!.id)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}
