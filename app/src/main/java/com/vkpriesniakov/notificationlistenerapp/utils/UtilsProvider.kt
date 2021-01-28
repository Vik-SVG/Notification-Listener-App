package com.vkpriesniakov.notificationlistenerapp.utils

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.vkpriesniakov.notificationlistenerapp.R

class UtilsProvider {

    companion object {

        private const val TAG = "UtilsProvider"
        fun buildNotificationServiceAlertDialog(
            context: Context,
            openPostActivityCustom: ActivityResultLauncher<Int>,
            message: Int
        ) {
            val alertDialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(R.string.notification_listener_service)
                .setMessage(message)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // context.startActivity(Intent(mACTION_NOTIFICATION_LISTENER_SETTINGS))
                        openPostActivityCustom.launch(1)
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }).create().show()
        }


        fun isNotificationServiceEnabled(context: Context): Boolean {
            val pkgName: String = context.packageName
            val flat = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            Log.i(TAG, flat + "\n" + pkgName)
            if (!TextUtils.isEmpty(flat)) {
                val names = flat.split(":".toRegex()).toTypedArray()
                for (i in names.indices) {
                    val cn = ComponentName.unflattenFromString(names[i])
                    if (cn != null) {
                        if (TextUtils.equals(pkgName, cn.packageName)) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}
