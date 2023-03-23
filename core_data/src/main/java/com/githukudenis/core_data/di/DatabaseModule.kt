package com.githukudenis.core_data.di

import android.content.Context
import androidx.room.Room
import com.githukudenis.core_data.data.local.db.ShopiaDatabase
import com.githukudenis.core_data.data.local.db.model.ProductsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProductsDatabase(@ApplicationContext context: Context): ShopiaDatabase {
        return Room.databaseBuilder(
            context, ShopiaDatabase::class.java, "products_db"
        ).fallbackToDestructiveMigration().build()

    }

    @Provides
    @Singleton
    fun provideProductsDao(productsDatabase: ShopiaDatabase): ProductsDao {
        return productsDatabase.productsDao()
    }
}