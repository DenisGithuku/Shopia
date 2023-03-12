package com.githukudenis.coroutinesindustrialbuild.data.remote

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTO
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val base_url = "https://fakestoreapi.com/"

interface ProductsApiService {

    @GET("products/categories")
    suspend fun getProductCategories(): Response<List<String>>

    @GET("products")
    suspend fun getAllProducts(): Response<ProductsDTO>

    @GET(
        "products/{productId}"
    )
    suspend fun getProductDetails(
        @Path("productId") productId: Int
    ): Response<ProductsDTOItem>

}