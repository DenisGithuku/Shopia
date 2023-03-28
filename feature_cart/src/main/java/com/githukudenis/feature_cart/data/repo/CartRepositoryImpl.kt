package com.githukudenis.feature_cart.data.repo

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.feature_cart.data.remote.CartApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApiService: CartApiService,
    private val cartDao: CartDao,
    private val shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
) : CartRepository {
    override suspend fun getProductsInCart(userId: Int): Flow<List<Product>> = flow {
        try {
            if (cartDao.getAllProducts().isEmpty()) {
                refreshProducts(userId)
            }
            val products = cartDao.getAllProducts()
            emit(products)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)

    override suspend fun insertProductInCart(product: Product) {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                cartDao.insertProduct(product)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private suspend fun refreshProducts(userId: Int) {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                val response = cartApiService.getCart(userId)
                if (response.isSuccessful) {
                    val productsData = response.body()
                    productsData?.let { productsDTO ->
                        val products = mutableListOf<Product>()
                        productsDTO.map { productsDTOItems ->
                            products.addAll(productsDTOItems.products)
                        }
                        cartDao.insertProducts(products)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}