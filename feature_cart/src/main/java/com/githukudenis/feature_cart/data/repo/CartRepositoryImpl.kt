package com.githukudenis.feature_cart.data.repo

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.feature_cart.data.remote.CartApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApiService: CartApiService, private val cartDao: CartDao
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
    }.flowOn(Dispatchers.IO)

    override suspend fun insertProductInCart(product: Product) {
        try {
            cartDao.insertProduct(product)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun refreshProducts(userId: Int) {
        try {
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
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}