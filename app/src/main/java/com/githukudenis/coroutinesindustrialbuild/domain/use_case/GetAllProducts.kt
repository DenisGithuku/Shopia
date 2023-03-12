package com.githukudenis.coroutinesindustrialbuild.domain.use_case

import com.githukudenis.coroutinesindustrialbuild.domain.repo.ProductsRepo
import com.githukudenis.coroutinesindustrialbuild.domain.repo.Resource
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetAllProducts @Inject constructor(
    private val productsRepo: ProductsRepo
) {
    operator fun invoke(): Flow<Resource<List<ProductDBO>>> = flow {
        try {
            emit(Resource.Loading())
            val products = productsRepo.getProducts()
            Resource.Success(products)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}