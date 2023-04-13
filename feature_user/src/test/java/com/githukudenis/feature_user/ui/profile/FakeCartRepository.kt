package com.githukudenis.feature_user.ui.profile

import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.db.model.cart.ProductsInCartDTOItem
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_cart.ui.views.cart.ProductInCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartRepository : CartRepository {
    private val products = mutableListOf(
        ProductsInCartDTOItem(
            __v = 1, date = "today", id = 1, userId = 12, products = mutableListOf(
                Product(
                    productId = 1, quantity = 13
                ),
                Product(
                    productId = 3, quantity = 10
                ),
                Product(
                    productId = 4, quantity = 52
                ),
                Product(
                    productId = 2, quantity = 18
                ),
                Product(
                    productId = 6, quantity = 27
                ),
                Product(
                    productId = 12, quantity = 34
                ),
                Product(
                    productId = 15, quantity = 45
                ),
            )
        ),
        ProductsInCartDTOItem(
            __v = 1, date = "today", id = 2, userId = 7, products = mutableListOf(
                Product(
                    productId = 1, quantity = 13
                ), Product(
                    productId = 3, quantity = 10
                ), Product(
                    productId = 4, quantity = 5
                ), Product(
                    productId = 2, quantity = 8
                ), Product(
                    productId = 5, quantity = 7
                )
            )
        ),
        ProductsInCartDTOItem(
            __v = 1, date = "yesterday", id = 3, userId = 18, products = mutableListOf(
                Product(
                    productId = 1, quantity = 13
                ), Product(
                    productId = 3, quantity = 10
                ), Product(
                    productId = 4, quantity = 5
                ), Product(
                    productId = 2, quantity = 8
                ), Product(
                    productId = 5, quantity = 7
                )
            )
        ),
    )


    override suspend fun getProductsInCart(userId: Int): Flow<List<ProductInCart>> {
        return flow {
            val userProducts = products.find { productItem ->
                productItem.userId == userId
            }
            userProducts?.products
        }
    }

    override suspend fun insertProductInCart(product: Product) {
        products.find {
            it.userId == 12
        }?.products?.toMutableList()?.add(product)
    }

    override suspend fun clearCart() {
        products.clear()
    }

    override suspend fun removeFromCart(productId: Int) {
        products.find {
            it.userId == 12
        }?.products?.dropWhile {
            it.productId == productId
        }
    }
}