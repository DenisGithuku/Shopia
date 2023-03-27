package com.githukudenis.feature_product.data.repository

import com.githukudenis.core_data.data.local.db.model.ProductsDao
import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.core_data.data.local.db.model.product.toProduct
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.feature_product.data.ProductsApiService
import com.githukudenis.feature_product.domain.repo.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsDao: ProductsDao,
    private val productsApiService: ProductsApiService,
    private val shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
) : ProductsRepository {
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
    }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)

    override suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>> = flow {
        try {
                val productsInCategory = productsDao.getProductsInCategory(category)
                emit(productsInCategory)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getProducts(): Flow<List<ProductDBO>> = flow {
        try {
            if (productsDao.getAllProducts().isEmpty()) {
                refreshProducts()
            }
                val products = productsDao.getAllProducts()
                emit(products)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)

    override suspend fun getProductDetails(productId: Int): Flow<ProductDBO> = flow {
        try {
            val product = productsDao.getProductById(productId)
            emit(product)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)

    override suspend fun refreshProducts() {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                val response = productsApiService.getAllProducts()
                if (response.isSuccessful) {
                    val products = response.body()
                    products?.let { productDTO ->
                        productsDao.insertAllProducts(productDTO.map { productsDTOItem -> productsDTOItem.toProduct() })
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun refreshCategories() {
        try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                val response = productsApiService.getProductCategories()
                if (response.isSuccessful) {
                    val categoriesDTO = response.body()
                    categoriesDTO?.let { categoryList ->
                        productsDao.insertAllCategories(categoryList.map { category ->
                            ProductCategory(
                                category
                            )
                        })
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}