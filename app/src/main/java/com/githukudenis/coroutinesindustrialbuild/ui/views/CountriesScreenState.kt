package com.githukudenis.coroutinesindustrialbuild.ui.views

import com.githukudenis.coroutinesindustrialbuild.data.model.Countries
import com.githukudenis.coroutinesindustrialbuild.data.model.CountriesItem

data class CountriesScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val countries: Countries? = null,
    val error: String? = null,
    val selectedCountry: CountriesItem? = null
)