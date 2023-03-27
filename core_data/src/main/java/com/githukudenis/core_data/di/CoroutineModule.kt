package com.githukudenis.core_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): ShopiaCoroutineDispatcher {
        return ShopiaCoroutineDispatcher(
            ioDispatcher = Dispatchers.IO,
            defaultDispatcher = Dispatchers.Default,
            mainDispatcher = Dispatchers.Main,
            unconfinedDispatcher = Dispatchers.Unconfined,
        )
    }
}

data class ShopiaCoroutineDispatcher(
    val ioDispatcher: CoroutineDispatcher,
    val defaultDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher,
    val unconfinedDispatcher: CoroutineDispatcher
)