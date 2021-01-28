package com.vkpriesniakov.notificationlistenerapp.ui.main


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.databinding.MainFragmentBinding
import com.vkpriesniakov.notificationlistenerapp.utils.PostActivityContract
import com.vkpriesniakov.notificationlistenerapp.utils.UtilsProvider

class NotificationMainFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationMainFragment()
        const val mACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private const val TAG = "NotificationMain"
    }

    private val openPostActivityCustom =
        registerForActivityResult(PostActivityContract()) { updateUi() }

    private lateinit var viewModelNotification: NotificationMainViewModel

    private var _binding: MainFragmentBinding? = null
    private var isServiceEnabled: Boolean? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bdn get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = bdn.root
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar?.elevation = 0f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUi()
        setupStartButton()
    }

    private fun setupStartButton() {
        bdn.btnStart.setOnClickListener {
            UtilsProvider.buildNotificationServiceAlertDialog(
                requireContext(),
                openPostActivityCustom,
                if (UtilsProvider.isNotificationServiceEnabled(requireContext())) R.string.alert_dialog_explanation_on
                else R.string.alert_dialog_explanation_off
            )
        }
    }

    private fun updateUi() {
        isServiceEnabled = UtilsProvider.isNotificationServiceEnabled(context as Context)

        if (isServiceEnabled as Boolean) {
            bdn.btnStart.text = getString(R.string.stop_btn_string)
        } else {
            bdn.btnStart.text = getString(R.string.start_btn_string)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

}