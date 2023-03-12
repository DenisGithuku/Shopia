package com.githukudenis.coroutinesindustrialbuild.data.repo

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem
import com.githukudenis.coroutinesindustrialbuild.data.model.Rating
import com.githukudenis.coroutinesindustrialbuild.domain.repo.ProductsRepo
import com.githukudenis.coroutinesindustrialbuild.domain.repo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductsDataSource: ProductsRepo {
    private val products = listOf(
        ProductsDTOItem(
            category = "jewelery",
            description = "To be worn by women",
            id = 1,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductsDTOItem(
            category = "jewelery",
            description = "To be worn by women",
            id = 1,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        ),
        ProductsDTOItem(
            category = "jewelery",
            description = "To be worn by women",
            id = 1,
            image = "https://sourceofimage.com",
            price = 45.0,
            rating = Rating(count = 3, rate = 5.6),
            title = "Louis Vutton"
        )
    )

    override suspend fun getProducts(): Flow<Resource<List<ProductsDTOItem>>> {
        return flow {
            emit(Resource.Success(products))
        }
    }

    override suspend fun getProductDetails(productId: Int): Flow<Resource<ProductsDTOItem>> {
        return flow {
            val product = products.find {
                it.id == productId
            } ?: return@flow
            emit(Resource.Success(product))
        }
    }
}