package com.example.tripit.dataclasses

sealed class PackingListItem {
    object LocationItem : PackingListItem()
    object CalendarItem : PackingListItem()
    object TravelType: PackingListItem()
    object TravelModal: PackingListItem()
    object TravelLuggage: PackingListItem()
    object TravelAccomodation: PackingListItem()
    object TravelSummary: PackingListItem()

}