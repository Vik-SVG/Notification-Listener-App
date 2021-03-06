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

    private var mFirstIncomingNotification: MyNotification? = null
    private var mSecondIncomingNotification: MyNotification? = null

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        Log.i(TAG, "Got ID ${sbn.id}")

        val text = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        val title = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString()

        if (text.equals("null") && title.equals("null") ||
            text.equals(null) && title.equals(null)
        ) {
            Log.i(TAG, "Got null")
            return
        } else {
            checkForEquals(sbn)
        }

    }

    private fun checkForEquals(sbn: StatusBarNotification) {

        if (mFirstIncomingNotification == null) {

            mFirstIncomingNotification = MyNotification(
                ntfId = null,
                ntfTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
                    .toString(),
                ntfMessage = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)
                    .toString(),
                ntfPackage = sbn.packageName,
                ntfDate = sbn.postTime
            )
            buildWork(sbn)
        } else {
            mSecondIncomingNotification = MyNotification(
                ntfId = null,
                ntfTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
                    .toString(),
                ntfMessage = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)
                    .toString(),
                ntfPackage = sbn.packageName,
                ntfDate = sbn.postTime
            )

            if (mFirstIncomingNotification!!.ntfTitle.equals(mSecondIncomingNotification!!.ntfTitle) &&
                mFirstIncomingNotification!!.ntfMessage.equals(mSecondIncomingNotification!!.ntfMessage) &&
                mFirstIncomingNotification!!.ntfPackage.equals(mSecondIncomingNotification!!.ntfPackage)
            ) {

                Log.i(TAG, "Equals messages")
                return
            } else {
                buildWork(sbn)
            }
            mFirstIncomingNotification = mSecondIncomingNotification
        }
    }


    private fun buildWork(sbn: StatusBarNotification) {

        val title = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
        var text = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)

        if (text is SpannableString) {
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
