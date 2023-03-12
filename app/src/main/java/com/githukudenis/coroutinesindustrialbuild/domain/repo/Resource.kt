package com.githukudenis.coroutinesindustrialbuild.domain.repo

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>: Resource<T>()
    class Success<T>(val results: T?): Resource<T>(data = results)
    class Error<T>(val cause: String?): Resource<T>(message = cause)
}
