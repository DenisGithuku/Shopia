package com.githukudenis.coroutinesindustrialbuild.data.local

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.data.remote.ProductsRemoteDatasource
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO
import com.githukudenis.coroutinesindustrialbuild.domain.repo.ProductsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ProductsLocalDataSource @Inject constructor(
    private val productsDao: ProductsDao,
    private val productsRemoteDatasource: ProductsRemoteDatasource
) : ProductsRepo {


    override suspend fun getCategories(): Flow<List<ProductCategory>> = flow {
        try {
            val productCategories = productsDao.getAllCategories()
            emit(productCategories)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getProducts(): Flow<List<ProductDBO>> = flow<List<ProductDBO>> {
        try {
            refreshProducts()
            val products = productsDao.getAllProducts()
            emit(products)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProductDetails(productId: Int): Flow<ProductDBO> = flow<ProductDBO> {
        try {
            val product = productsDao.getProductById(productId)
            emit(product)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun refreshProducts() {
        productsRemoteDatasource.refreshProducts()
    }
}