package com.githukudenis.coroutinesindustrialbuild.data.repo

import com.githukudenis.coroutinesindustrialbuild.data.model.Countries
import kotlinx.coroutines.flow.Flow

interface CountriesRepo {
    suspend fun getCountries(): Flow<Resource<Countries>>
}
