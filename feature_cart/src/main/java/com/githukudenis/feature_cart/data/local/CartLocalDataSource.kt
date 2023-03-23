package com.githukudenis.feature_cart.data.local

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.feature_cart.data.remote.CartApiService
import com.githukudenis.feature_cart.data.repo.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val cartDao: CartDao,
    private val cartApiService: CartApiService,
) : CartRepository {

    override suspend fun getProductsInCart(userId: Int): Flow<List<Product>> = flow {
        try {
            if (cartDao.getAllProducts().isEmpty()) {
                refreshProducts(userId)
            }
            val products = cartDao.getAllProducts()
            emit(products)
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertProductInCart(product: Product) {
        try {
            withContext(Dispatchers.IO) {
                cartDao.insertProduct(product)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun refreshProducts(userId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val productsResponse = cartApiService.getCart(userId)
                if (productsResponse.isSuccessful) {
                    val products = productsResponse.body()
                    products?.let { productsInCartDTO ->
                        productsInCartDTO.map { productsInCartDTOItem ->
                            cartDao.insertProducts(productsInCartDTOItem.products)
                        }
                    }
                }

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}