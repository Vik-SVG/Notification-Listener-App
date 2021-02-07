package com.vkpriesniakov.notificationlistenerapp.service

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification


class MainListenerService : NotificationListenerService() {

    private val TAG = "MyNotificationListener"

    private var mn1:MyNotification? = null
    private var mn2:MyNotification? = null

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        Log.i(TAG, "Got ID ${sbn.id}")

        val text = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        val title = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString()

       if ( text.equals("null") && title.equals("null") ||
           text.equals(null) && title.equals(null)
       ){
           Log.i(TAG, "Got null")
           return
       } else{
           checkForEquals(sbn)
       }

    }

    private fun checkForEquals(sbn: StatusBarNotification) {

        if (mn1 == null){

            mn1 = MyNotification( ntfId = null,
                ntfTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString(),
                ntfMessage =  sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString(),
                ntfPackage = sbn.packageName,
                ntfDate = sbn.postTime)
            buildWork(sbn)
        } else{
            mn2 = MyNotification( ntfId = null,
                ntfTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString(),
                ntfMessage =  sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString(),
                ntfPackage = sbn.packageName,
                ntfDate = sbn.postTime)

            if (mn1!!.ntfTitle.equals(mn2!!.ntfTitle) &&
                mn1!!.ntfMessage.equals(mn2!!.ntfMessage) &&
                mn1!!.ntfPackage.equals(mn2!!.ntfPackage)){

                Log.i(TAG, "Equals messages")
                    return
            } else  {
                buildWork(sbn)
            }
            mn1 = mn2
        }
    }


    private fun buildWork(sbn: StatusBarNotification){

        val title = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
        var text = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)

        if (text is SpannableString){
            text = text.toString()
        }

        val builder = Data.Builder().apply {
            putString("title", title as String?)
            putString("text", text as String?)
            putString("pack", sbn.packageName)
            putLong("date", sbn.postTime)
        }

        val workManager = WorkManager.getInstance(this)
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(builder.build())
            .build()
        workManager.enqueue(workRequest)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i(TAG, "Removed ID " + sbn!!.id)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}
