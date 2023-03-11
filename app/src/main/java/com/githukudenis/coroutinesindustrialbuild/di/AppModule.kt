package com.githukudenis.coroutinesindustrialbuild.di

import android.content.Context
import com.githukudenis.coroutinesindustrialbuild.data.api.CountriesApiService
import com.githukudenis.coroutinesindustrialbuild.data.api.base_url
import com.githukudenis.coroutinesindustrialbuild.data.repo.CountriesDatasource
import com.githukudenis.coroutinesindustrialbuild.data.repo.CountriesRepo
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkStateObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLogginInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
//            .callTimeout(timeout = 10000L, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCountriesApi(okHttpClient: OkHttpClient): CountriesApiService {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CountriesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCountriesRepository(
        countriesApiService: CountriesApiService
    ): CountriesRepo {
        return CountriesDatasource(countriesApiService = countriesApiService)
    }

    @Provides
    @Singleton
    fun provideNetworkStateObserver(
        @ApplicationContext
        context: Context
    ): NetworkObserver {
        return NetworkStateObserver(context = context)
    }
}