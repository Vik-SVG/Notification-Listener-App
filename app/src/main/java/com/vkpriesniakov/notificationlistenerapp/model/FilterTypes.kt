package com.vkpriesniakov.notificationlistenerapp.model

enum  class FilterTypes(val filter:Int) {

    ALL(0),
    PER_HOUR(1),
    PER_DAY(2),
    PER_MONTH(3);


companion object {
    fun getEnumFilterType(filter: Int): FilterTypes {
        return when (filter) {
            0 -> ALL
            1 -> PER_HOUR
            2 -> PER_DAY
            3 -> PER_MONTH
            else -> ALL
        }
    }
}
}
