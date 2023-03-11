package com.githukudenis.coroutinesindustrialbuild.ui.views

sealed class CountriesScreenEvent {
    class OpenCountriesDetails(val flag: String): CountriesScreenEvent()
    object RefreshCountries: CountriesScreenEvent()
}