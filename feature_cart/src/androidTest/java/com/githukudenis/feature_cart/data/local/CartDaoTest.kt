package com.githukudenis.feature_cart.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.ShopiaDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class CartDaoTest {

    private lateinit var shopiaDatabase: ShopiaDatabase
    private lateinit var cartDao: CartDao

    @get:Rule
    val instantTaskExecutor: InstantTaskExecutorRule by lazy { InstantTaskExecutorRule() }

    @Before
    fun setUp() {
        shopiaDatabase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ShopiaDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        cartDao = shopiaDatabase.cartDao()
    }
}