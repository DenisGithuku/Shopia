package com.githukudenis.feature_product.di

import android.content.Context
import androidx.room.Room
import com.githukudenis.feature_product.data.local.ProductsDao
import com.githukudenis.feature_product.data.local.ProductsDatabase
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
    fun provideProductsDatabase(@ApplicationContext context: Context): ProductsDatabase {
        return Room.databaseBuilder(
            context, ProductsDatabase::class.java, "products_db"
        ).fallbackToDestructiveMigration().build()

    }

    @Provides
    @Singleton
    fun provideProductsDao(productsDatabase: ProductsDatabase): ProductsDao {
        return productsDatabase.productsDao()
    }
}