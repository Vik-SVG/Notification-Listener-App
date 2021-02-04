package com.vkpriesniakov.notificationlistenerapp.ui.main

import androidx.lifecycle.*
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.*
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes.Companion.getEnumFilterType
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.utils.FILTER_TYPES_KEY
import com.vkpriesniakov.notificationlistenerapp.utils.HOUR_SEC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NotificationMainViewModel (private val notificationRepo:NotificationMainRepository,
                                 private val state: SavedStateHandle
                                 ): ViewModel() {

    private val filterType: MutableStateFlow<Int> = MutableStateFlow(
        state.get<Int>(FILTER_TYPES_KEY)?:0 //  TODO:Filter getFromSPref
    )

    val allNotifications:LiveData<List<MyNotification>> = filterType.flatMapLatest { filter ->
        when(getEnumFilterType(filter)){
            ALL -> notificationRepo.allNotifications
            PER_HOUR -> notificationRepo.allNotificationsPerHour
            PER_DAY -> notificationRepo.allNotificationsPerDay
            PER_MONTH -> notificationRepo.allNotificationsPerMonth
        }
    }.asLiveData()

    fun insertNotification(notification:MyNotification) =
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.insertNotification(notification)
        }

    fun deleteNotification(notification:MyNotification) =
        viewModelScope.launch (Dispatchers.IO){
            notificationRepo.deleteNotification(notification)
        }

    fun deleteAll(){
        viewModelScope.launch (Dispatchers.IO){
         notificationRepo.deleteAllNotifications()
        }
    }

    init {
        viewModelScope.launch {
            filterType.collect { currentFilter ->
                state.set(FILTER_TYPES_KEY, currentFilter)
            }
        }
    }

    fun setFilter(type:FilterTypes){
        filterType.value = type.filter
      //  SharePref.data = filterType.value TODO
    }

    fun getFilterTypeVM(): FilterTypes{
        return getEnumFilterType(filterType.value)
    }


    //Factory viewModel implementation for implementing single detailNotification fragment
    //In current version is unnecessarily
    interface AssistedFactory{
        fun create():NotificationMainViewModel
    }

    companion object{
        fun provideFactory( assistedFactory:AssistedFactory):ViewModelProvider.Factory = object:ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create() as T
            }

        }
    }

}
