package com.githukudenis.feature_cart.data.repo

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.feature_cart.data.remote.CartApiService
import com.githukudenis.feature_cart.ui.views.cart.ProductInCart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApiService: CartApiService,
    private val cartDao: CartDao,
    private val productsRepository: ProductsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
) : CartRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProductsInCart(userId: Int): Flow<List<ProductInCart>> {
        return try {
            if (cartDao.getAllProducts().isEmpty()) {
                refreshProducts(userId)
            }
            val productsInCart = cartDao.getAllProducts()
            val allProductsFlow = productsRepository.getProducts()
            val products = allProductsFlow.mapLatest { allProducts ->
                allProducts.filter { productDBO ->
                    productsInCart.any { productInCart -> productInCart.productId == productDBO.id }
                }.map {
                    val quantity =
                        productsInCart.first { productInCart -> productInCart.productId == it.id }?.quantity

                        ProductInCart(
                            quantity = quantity,
                            productDBO = it
                        )
                }
            }
            products
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun insertProductInCart(product: Product) {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                cartDao.insertProduct(product)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun clearCart() {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                /*
                 clear cart for current user
                 */
                cartDao.deleteCart()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun removeFromCart(productId: Int) {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                val product = cartDao.getAllProducts().find {
                    it.productId == productId
                }
                product?.let { cartDao.deleteProduct(it) }
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