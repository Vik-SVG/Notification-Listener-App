package com.vkpriesniakov.notificationlistenerapp.ui.main

import androidx.lifecycle.*
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.*
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.sharedpreferences.PreferencesRepository
import com.vkpriesniakov.notificationlistenerapp.sharedpreferences.UserPreferences
import com.vkpriesniakov.notificationlistenerapp.utils.DAY_MS
import com.vkpriesniakov.notificationlistenerapp.utils.HOUR_MS
import com.vkpriesniakov.notificationlistenerapp.utils.MONT_MS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

data class NotificationsUiModel(
    val allNotifications: List<MyNotification>,
    val filterOrder: String
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
        filterType: String
    ): List<MyNotification> {
        return when (filterType) {
            ALL.name -> notifications.sortedByDescending { it.ntfDate }
            PER_HOUR.name -> notifications.filter { it.ntfDate!! >= Date().time - HOUR_MS }
                .sortedByDescending { it.ntfDate }
            PER_DAY.name -> notifications.filter { it.ntfDate!! >= Date().time - DAY_MS }
                .sortedByDescending { it.ntfDate }
            PER_MONTH.name -> notifications.filter { it.ntfDate!! >= Date().time - MONT_MS }
                .sortedByDescending { it.ntfDate }
            else -> notifications.sortedByDescending { it.ntfDate }
        }
    }

    fun setFilter(type: FilterTypes) {
        viewModelScope.launch {
            userPreferencesRepo.updateChosenFilter(type)
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
