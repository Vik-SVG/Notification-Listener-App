package com.vkpriesniakov.notificationlistenerapp.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import java.lang.reflect.InvocationTargetException
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "UtilsProvider"

fun showServiceDialog(
    context: Context,
    openPostActivityCustom: ActivityResultLauncher<Int>
) {

    val message = if (isServiceEnabled(context)) R.string.alert_dialog_explanation_on
    else R.string.alert_dialog_explanation_off

    val alertDialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        .setTitle(R.string.notification_listener_service)
        .setMessage(message)
        .setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                openPostActivityCustom.launch(1)
            })
        .setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                // If you choose to not enable the notification listener
                // the app. will not work as expected
            }).create().show()
}

interface OnDeletionClick {
    fun onDeleteClick()
}

fun showDeleteDialog(
    context: Context,
    listener: OnDeletionClick
) {
    val message = "Do you want to delete all Notifications?"

    val alertDialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        .setTitle("Delete All Notifications")
        .setMessage(message)
        .setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                listener.onDeleteClick()
            })
        .setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            }).create().show()
}


fun isServiceEnabled(context: Context): Boolean {
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

fun convertTime(time: Long?): String? {
    val date = time?.let { Date(it) }
    val format: Format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

fun convertDate(time: Long?): String? {
    val date = time?.let { Date(it) }
    val format: Format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return format.format(date)
}

fun getNotificationIcon(context: Context, packageName: String): Drawable {
    //TODO: put icon into cache directory

    return context.packageManager.getApplicationIcon(packageName)
}

fun getIconFromCache() {

}

fun setAnimatedBackground(context: Context, view: View) {

    val colorFrom: Int = context.resources.getColor(R.color.white)
    val colorTo: Int = context.resources.getColor(R.color.darker_grey)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250 // milliseconds

    colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()

}