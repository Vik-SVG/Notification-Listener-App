package com.vkpriesniakov.notificationlistenerapp.ui.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.databinding.MainFragmentBinding
import com.vkpriesniakov.notificationlistenerapp.utils.PostActivityContract
import com.vkpriesniakov.notificationlistenerapp.utils.isServiceEnabled
import com.vkpriesniakov.notificationlistenerapp.utils.showServiceDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationMainFragment : Fragment() {


    companion object {
        fun newInstance() = NotificationMainFragment()
        private const val TAG = "NotificationMain"
    }

    private val mViewModel:NotificationMainViewModel by viewModel()

    private val openPostActivityCustom =
        registerForActivityResult(PostActivityContract()) { updateUi() }

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

        mViewModel.allNotifications.observe(viewLifecycleOwner){
            it.let {
                Log.i(TAG, "Size: ${it.size}")

                it.forEach { notification ->
                    Log.i(TAG, "Id: ${notification.ntfId}; Date: ${notification.ntfDate}.")
                }

            }
        }
    }


    private fun setupStartButton() {
        bdn.btnStart.setOnClickListener {

            showServiceDialog(
               context =  requireContext(),
               openPostActivityCustom = openPostActivityCustom)
        }
    }

    private fun updateUi() {
        isServiceEnabled = isServiceEnabled(context as Context)

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