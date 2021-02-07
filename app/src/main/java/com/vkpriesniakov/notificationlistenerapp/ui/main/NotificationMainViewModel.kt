package com.vkpriesniakov.notificationlistenerapp.ui.main

import androidx.lifecycle.*
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.*
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.Companion.getEnumFilterType
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.sharedpreferences.PreferencesRepository
import com.vkpriesniakov.notificationlistenerapp.sharedpreferences.UserPreferences
import com.vkpriesniakov.notificationlistenerapp.utils.DAY_MS
import com.vkpriesniakov.notificationlistenerapp.utils.FILTER_TYPES_KEY
import com.vkpriesniakov.notificationlistenerapp.utils.HOUR_MS
import com.vkpriesniakov.notificationlistenerapp.utils.MONT_MS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*

data class NotificationsUiModel(
    val allNotifications: List<MyNotification>,
    val filterOrder: Int
)

class NotificationMainViewModel(
    private val notificationRepo: NotificationMainRepository,
    private val userPreferencesRepo: PreferencesRepository
) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepo.userPreferencesFlow

    val allNotificationsModelFlow = combine(
        notificationRepo.allNotifications,
        userPreferencesFlow
    ) { allNotifications: List<MyNotification>, userPref: UserPreferences ->
        return@combine NotificationsUiModel(
            allNotifications = filterAllNotifications(
                allNotifications,
                userPref.filterType
            ),
            filterOrder = userPref.filterType
        )
    }.asLiveData()


    private fun filterAllNotifications(
        notifications: List<MyNotification>,
        filterType: Int
    ): List<MyNotification> {
        val filteredNotif = when(filterType){
            0 ->notifications
            1 ->notifications.filter { it.ntfDate!! >= System.currentTimeMillis() - HOUR_MS}
            2 ->notifications.filter { it.ntfDate!! >= System.currentTimeMillis() - DAY_MS }
            3 ->notifications.filter {  it.ntfDate!! >= System.currentTimeMillis() - MONT_MS }
            else -> notifications
        }
        return filteredNotif
    }

    fun setFilter(type: FilterTypes) {
        viewModelScope.launch {
            userPreferencesRepo.updateChosenFilter(type.filter)
        }
    }

    var currentPopupFilter = 0

    init {
        viewModelScope.launch {
            userPreferencesFlow.collect { currentPopupFilter = it.filterType }
        }
    }


    fun insertNotification(notification: MyNotification) =
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.insertNotification(notification)
        }

    fun deleteNotification(notification: MyNotification) =
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.deleteNotification(notification)
        }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.deleteAllNotifications()
        }
    }

    //Factory viewModel implementation for implementing single detailNotification fragment
    //In current version is unnecessarily
    interface AssistedFactory {
        fun create(): NotificationMainViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: AssistedFactory): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return assistedFactory.create() as T
                }

            }
    }

}
