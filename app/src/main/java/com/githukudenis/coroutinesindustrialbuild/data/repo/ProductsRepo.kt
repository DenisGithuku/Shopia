package com.githukudenis.coroutinesindustrialbuild.data.repo

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategories
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductsRepo {

    suspend fun getCategories(): Flow<Resource<ProductCategories>>
    suspend fun getProducts(): Flow<Resource<List<ProductsDTOItem>>>

    suspend fun getProductDetails(productId: Int): Flow<Resource<ProductsDTOItem>>
}
