package com.githukudenis.feature_product.data.repo

import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.core_data.data.local.db.model.product.Rating
import com.githukudenis.core_data.data.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductsRepositoryImpl: ProductsRepository {

    private val products = listOf(
        ProductDBO(
            category = "jewelery",
            description = "To be worn by women",
            id = 1,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductDBO(
            category = "jewelery",
            description = "To be worn by women",
            id = 2,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductDBO(
            category = "women's clothing",
            description = "To be worn by women",
            id = 3,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductDBO(
            category = "women's clothing",
            description = "To be worn by women",
            id = 4,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductDBO(
            category = "men's clothing",
            description = "To be worn by men",
            id = 5,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        )
    )

    override suspend fun getCategories(): Flow<List<ProductCategory>> {
        return flow {
            emit(listOf(
                ProductCategory("men's clothing"),
                ProductCategory("jewelery"),
                ProductCategory("electronics"),
                ProductCategory("women's clothing")
            ))
        }
    }

    override suspend fun refreshCategories() {

    }

    override suspend fun refreshProducts() {

    }

    override suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>> {
        return flow {
            val productsInCategory = products.filter { product -> product.category == category }
            emit(productsInCategory)
        }
    }

    override suspend fun getProducts(): Flow<List<ProductDBO>> {
        return flow {
            emit(products)
        }
    }

    override suspend fun getProductDetails(productId: Int): Flow<ProductDBO> {
        return flow {
            val product = products.find {
                it.id == productId
            } ?: return@flow
            emit(product)
        }
    }
}