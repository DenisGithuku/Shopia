package com.githukudenis.coroutinesindustrialbuild.data.api

import com.githukudenis.coroutinesindustrialbuild.data.model.Countries
import retrofit2.Response
import retrofit2.http.GET

const val base_url = "https://restcountries.com/v3.1/"

interface CountriesApiService {

    @GET("all")
    suspend fun getAllCountries(): Response<Countries>

}