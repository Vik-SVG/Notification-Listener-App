package com.vkpriesniakov.notificationlistenerapp.ui.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkpriesniakov.notificationlistenerapp.R
import com.vkpriesniakov.notificationlistenerapp.adapters.NotificationRVAdapter
import com.vkpriesniakov.notificationlistenerapp.databinding.MainFragmentBinding
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import com.vkpriesniakov.notificationlistenerapp.swipe_to_delete.SwipeToDeleteCallback
import com.vkpriesniakov.notificationlistenerapp.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationMainFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationMainFragment()
        private const val TAG = "NotificationMain"
    }

    //viewBinding
    private val mViewModel: NotificationMainViewModel by viewModel()
    private var _binding: MainFragmentBinding? = null

    // The property below is only valid between onCreateView and onDestroyView.
    private val bdn get() = _binding!!

    private lateinit var mAdapter: NotificationRVAdapter

    //onActivityResult
    private val openPostActivityCustom =
        registerForActivityResult(PostActivityContract()) { updateUi() }

    private var isServiceEnabled: Boolean? = null
    private lateinit var mPopup: CustomPopup


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = bdn.root

        setupRecyclerView()
        setHasOptionsMenu(true)

        ((activity as AppCompatActivity)).setSupportActionBar(bdn.includeAppbar.myToolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        setupStartButton()
    }

    private fun setupRecyclerView() {
        mAdapter = NotificationRVAdapter(activity as AppCompatActivity)

        bdn.rvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(context as Context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                mViewModel.deleteNotification(
                    mAdapter.allNotifications[viewHolder.adapterPosition]
                )
                mAdapter.removeAt(viewHolder.adapterPosition)
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(bdn.rvMain)

        subscribeUI(mAdapter)
    }

    private fun subscribeUI(adapter: NotificationRVAdapter) {

        mViewModel.allNotificationsModelFlow.observe(viewLifecycleOwner) { notif ->
            adapter.setNotifications(notif.allNotifications)

            setupPopupMenu(
                activity?.layoutInflater as LayoutInflater,
                FilterTypes.valueOf(notif.filterOrder)
            )

            if (notif.allNotifications.isNotEmpty()) {
                bdn.imgEmptyNotifications.visibility = View.GONE
                bdn.txtNoNotification.visibility = View.GONE
            } else {
                bdn.imgEmptyNotifications.visibility = View.VISIBLE
                bdn.txtNoNotification.visibility = View.VISIBLE
            }
        }
    }

    private fun setupPopupMenu(inflater: LayoutInflater, filterType: FilterTypes) {
        mPopup =
            CustomPopup(
                inflater,
                filterType,
                object : CustomPopup.OnFilterClick {
                    override fun onFilterClick(filterType: FilterTypes) {
                        mViewModel.setFilter(filterType)
                    }
                }
            ).also {
                it.setPopupListeners()
                Log.i(TAG, "Set new popup ")
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
        return when (item.itemId) {
            R.id.settings_menu -> {
                mPopup.showPopup()
                // Log.i(TAG, "Current filter is: ")
                return true
            }
            R.id.delete_all -> {
                showDeleteDialog(context as Context, object : OnDeletionClick {
                    override fun onDeleteClick() {
                        mViewModel.deleteAll()
                        Log.i(TAG, "Delete All")
                    }
                })
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }
}