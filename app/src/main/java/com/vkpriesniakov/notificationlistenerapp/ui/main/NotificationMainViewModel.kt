package com.vkpriesniakov.notificationlistenerapp.ui.main

import androidx.lifecycle.*
import com.vkpriesniakov.notificationlistenerapp.model.MyNotification
import com.vkpriesniakov.notificationlistenerapp.utils.FILTER_TYPES_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationMainViewModel (private val notificationRepo:NotificationMainRepository,
                                 private val state: SavedStateHandle
                                 ): ViewModel() {


    val allNotifications:LiveData<List<MyNotification>> =
        notificationRepo.allNotifications.asLiveData()


    fun insertNotification(notification:MyNotification) =
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepo.insertNotification(notification)
        }

    fun deleteNotification(notification:MyNotification) =
        viewModelScope.launch (Dispatchers.IO){
            notificationRepo.deleteNotification(notification)
        }

    /*
    private val filterType:MutableStateFlow<Int> = MutableStateFlow( //TODO:Filter settings
        state.get<Int>(FILTER_TYPES_KEY)?:0
    )

    init {
        viewModelScope.launch {
            filterType.collect {currentFilter ->
                state.set(FILTER_TYPES_KEY, currentFilter)
            }
        }
    }

    fun setFilter(num:Int){
        filterType.value = num
    }*/


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
