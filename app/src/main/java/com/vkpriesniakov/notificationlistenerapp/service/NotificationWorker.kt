package com.vkpriesniakov.notificationlistenerapp.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.persistence.NotificationDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(ctx:Context, params:WorkerParameters) :Worker(ctx, params), KoinComponent{
    val TAG = "WorkerNotification"

    override fun doWork(): Result {

        return try {
            val mDao:NotificationDao by inject()
            val myNotification = MyNotification(
                ntfId = null,
                ntfTitle = inputData.getString("title"),
                ntfMessage = inputData.getString("text"),
                ntfPackage = inputData.getString("pack"),
                ntfDate = inputData.getLong("date", 0L))

            mDao.insertNotification(myNotification)
            Log.i(TAG, "got : ${inputData.getLong("date", 0L)}")
            Result.success()
        } catch (throwable: Throwable) {
            Log.e("workerLog", "Error applying blur", throwable)
            Result.failure()
        }

    }
}