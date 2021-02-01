package com.vkpriesniakov.notificationlistenerapp.ui.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.databinding.MainFragmentBinding
import com.vkpriesniakov.notificationlistenerapp.utils.*
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

    private lateinit var mPopup:CustomPopup

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bdn get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = bdn.root

       mPopup = CustomPopup(inflater, bdn.rvMain).also {
           it setPopupListeners context as Context
       }

        setHasOptionsMenu(true)

        ((activity as AppCompatActivity)).setSupportActionBar(bdn.includeAppbar.myToolbar)

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
                context = requireContext(),
                openPostActivityCustom = openPostActivityCustom
            )
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       return when(item.itemId){
           R.id.settings_menu -> {
               mPopup.showPopup()
               return true
           }
            else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

}