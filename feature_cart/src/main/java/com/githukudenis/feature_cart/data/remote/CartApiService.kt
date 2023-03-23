package com.githukudenis.feature_cart.data.remote

import com.githukudenis.core_data.data.local.db.model.cart.ProductsInCartDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface CartApiService {

    @GET("carts/user/{userId}")
    suspend fun getCart(
        @Path("userId") userId: Int,
        @Query("startdate") startDate: String = "2019-12-10",

        @Query("enddate") enddate: String = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    ): Response<ProductsInCartDTO>

}