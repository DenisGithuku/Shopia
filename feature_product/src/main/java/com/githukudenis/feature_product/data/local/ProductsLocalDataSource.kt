package com.githukudenis.feature_product.data.local

import com.githukudenis.core_data.data.local.db.model.ProductsDao
import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.feature_product.data.remote.ProductsRemoteDatasource
import com.githukudenis.feature_product.domain.repo.ProductsRepo
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
            if (productsDao.getAllCategories().isEmpty()) {
                refreshCategories()
            }
            val productCategories = productsDao.getAllCategories()
            emit(productCategories)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>> = flow {
        try {
            val productsInCategory = productsDao.getProductsInCategory(category)
            emit(productsInCategory)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getProducts(): Flow<List<ProductDBO>> = flow<List<ProductDBO>> {
        try {
            if (productsDao.getAllProducts().isEmpty()) {
                refreshProducts()
            }
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

    override suspend fun refreshProducts() {
        productsRemoteDatasource.refreshProducts()
    }

    override suspend fun refreshCategories() {
        productsRemoteDatasource.getProductCategories()
    }
}