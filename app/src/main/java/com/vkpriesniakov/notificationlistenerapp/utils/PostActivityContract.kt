package com.vkpriesniakov.notificationlistenerapp.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainFragment.Companion.mACTION_NOTIFICATION_LISTENER_SETTINGS

class PostActivityContract : ActivityResultContract<Int, String?>() {

    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(mACTION_NOTIFICATION_LISTENER_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return null
    }
}
