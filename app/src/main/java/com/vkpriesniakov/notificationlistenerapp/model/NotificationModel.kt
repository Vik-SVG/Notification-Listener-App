package com.vkpriesniakov.notificationlistenerapp.model

import android.graphics.drawable.Drawable
import java.util.*

data class NotificationModel (val appName:String,
                              val message:String,
                              val icon: Drawable,
                              val date: Date = Date()
)