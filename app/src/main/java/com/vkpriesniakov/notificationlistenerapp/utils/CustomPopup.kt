package com.vkpriesniakov.notificationlistenerapp.utils

import android.content.Context
import android.view.*
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import com.vkpriesniakov.notificationlistenerapp.R

class CustomPopup(
    inflater: LayoutInflater,
    private val view: View) {

    private var popupView = inflater.inflate(R.layout.popup_window_menu, null)

    private var rbAll: RadioButton = popupView.findViewById(R.id.radio_filter_all)
    private var rbHour: RadioButton = popupView.findViewById(R.id.radio_filter_Hour)
    private var rbDay: RadioButton = popupView.findViewById(R.id.radio_filter_day)
    private var rbMonth: RadioButton = popupView.findViewById(R.id.radio_filter_month)


    private var popup = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

      infix fun setPopupListeners(context: Context) {
            popupView.rootView.setOnClickListener {
                popup.dismiss()
            }

          //TODO filter check

            rbAll.setOnClickListener {
                Toast.makeText(context, "Clicked all", Toast.LENGTH_SHORT).show()
            }

            rbDay.setOnClickListener {
                Toast.makeText(context, "Clicked day", Toast.LENGTH_SHORT).show()

            }

            rbHour.setOnClickListener {
                Toast.makeText(context, "Clicked hour", Toast.LENGTH_SHORT).show()

            }

            rbMonth.setOnClickListener {
                Toast.makeText(context, "Clicked month", Toast.LENGTH_SHORT).show()

            }


        }

   fun showPopup() {
        popup.animationStyle = R.style.popup_window_animation_custom
        popup.isOutsideTouchable = true
        popup.showAtLocation(view, Gravity.TOP, Gravity.END, 0)
        popup.dimBehind()
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val wm = contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.updateViewLayout(container, p)
    }

}


