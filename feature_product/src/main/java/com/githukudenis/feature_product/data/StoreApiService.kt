package com.githukudenis.feature_product.data

import com.githukudenis.core_data.data.local.db.model.product.ProductsDTO
import retrofit2.Response
import retrofit2.http.GET

const val base_url = "https://fakestoreapi.com/"

interface ProductsApiService {

    @GET("products/categories")
    suspend fun getProductCategories(): Response<List<String>>

    @GET("products")
    suspend fun getAllProducts(): Response<ProductsDTO>

}