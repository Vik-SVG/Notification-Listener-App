package com.vkpriesniakov.notificationlistenerapp.utils

import android.content.Context
import android.view.*
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.*

class CustomPopup(inflater: LayoutInflater,
                  private val filterType: FilterTypes,
                  private val filterListener:OnFilterClick) {

    private var popupView = inflater.inflate(R.layout.popup_window_menu, null)

    private var popup = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private var rbAll: RadioButton = popupView.findViewById(R.id.radio_filter_all)
    private var rbHour: RadioButton = popupView.findViewById(R.id.radio_filter_Hour)
    private var rbDay: RadioButton = popupView.findViewById(R.id.radio_filter_day)
    private var rbMonth: RadioButton = popupView.findViewById(R.id.radio_filter_month)

    private var listOfButtons:List<Pair<RadioButton, FilterTypes>> = listOf(
        Pair(rbAll,ALL),
        Pair(rbHour,PER_HOUR),
        Pair(rbDay,PER_DAY),
        Pair(rbMonth,PER_MONTH)
    )

       fun setPopupListeners() {

           when(filterType){
               ALL -> rbAll.isChecked = true
               PER_HOUR -> rbHour.isChecked = true
               PER_DAY -> rbDay.isChecked = true
               PER_MONTH -> rbMonth.isChecked = true
           }

           listOfButtons.forEach { list ->
               list.first.setOnClickListener {
                   filterListener.onFilterClick(list.second)
                   popup.dismiss()
               }
           }

            popupView.rootView.setOnClickListener {
                popup.dismiss()
            }
        }

   fun showPopup() {
        popup.apply {
            animationStyle = R.style.popup_window_animation_custom
            isOutsideTouchable = true
            showAtLocation(popupView.rootView as View, Gravity.TOP, Gravity.END, 0)
            dimBehind()
        }
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val wm = contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = (container.layoutParams as WindowManager.LayoutParams)
            .also {
            it.flags = it.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            it.dimAmount = 0.4f
        }
        wm.updateViewLayout(container, p)
    }

    interface OnFilterClick{
       fun onFilterClick(filterType:FilterTypes)
    }

}


