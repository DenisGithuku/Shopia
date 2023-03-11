package com.githukudenis.coroutinesindustrialbuild.data.repo

import android.util.Log
import com.githukudenis.coroutinesindustrialbuild.data.api.CountriesApiService
import com.githukudenis.coroutinesindustrialbuild.data.model.Countries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class CountriesDatasource(
    private val countriesApiService: CountriesApiService
) : CountriesRepo {

    override suspend fun getCountries(): Flow<Resource<Countries>> = flow {
        try {
            emit(Resource.Loading())
            val countries = countriesApiService.getAllCountries()
            if (countries.isSuccessful) {
                val countries = countries.body()
                emit(Resource.Success(countries))
            }
        } catch (e: Exception) {
            emit(Resource.Error(cause = e.message.toString()))
            Log.e("countries_err", e.message.toString())
        }
    }
        .buffer(capacity = 10)
        .distinctUntilChanged()
        .retry(retries = 1)
        .flowOn(Dispatchers.IO)

}