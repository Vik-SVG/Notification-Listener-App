package com.vkpriesniakov.notificationlistenerapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.databinding.NotificationCardBinding
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.utils.*
import java.lang.reflect.InvocationTargetException

class NotificationRVAdapter(val mContext: Context) :
    RecyclerView.Adapter<NotificationRVAdapter.NotificationViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(mContext)
    var allNotifications = mutableListOf<MyNotification>()

    fun removeAt(position: Int) {
        allNotifications.removeAt(position)
        notifyItemRemoved(position)
        Log.i("RemovedAtAdapter", "Removed $position")
    }

    internal fun setNotifications(notifications: List<MyNotification>) {
        this.allNotifications = notifications.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val bdn = NotificationCardBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(bdn)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = allNotifications.size

    inner class NotificationViewHolder(private val bdnView: NotificationCardBinding) :
        RecyclerView.ViewHolder(bdnView.root) {
        fun bind(position: Int) {

            val packageName = allNotifications[position].ntfPackage

            val iconApp = packageName?.let {

                //TODO: getIcon from cached directory
                //if null, getNotification
                try {
                    getNotificationIcon(mContext, it)
                } catch (e: InvocationTargetException) {
                    e.cause?.printStackTrace()
                    mContext.getDrawable(R.drawable.ic_none_notification)
                } catch (e: Exception) {
                    e.printStackTrace()
                    mContext.getDrawable(R.drawable.ic_none_notification)
                }

            }

            Glide.with(mContext).load((iconApp)).placeholder(R.color.darker_grey)
                .into(bdnView.imgAppNotification)

            if (iconApp != null) {
                bdnView.imgAppNotification.setImageDrawable(iconApp)
            } else {
                bdnView.imgAppNotification.setImageResource(R.drawable.ic_none_notification)
            }


            bdnView.txtTitleAppName.text = allNotifications[position].ntfTitle

            bdnView.txtNotificationText.text = allNotifications[position].ntfMessage

            bdnView.txtNtfDate.text = convertDate(allNotifications[position].ntfDate)

            bdnView.txtNtfTime.text = convertTime(allNotifications[position].ntfDate)

        }
    }
}