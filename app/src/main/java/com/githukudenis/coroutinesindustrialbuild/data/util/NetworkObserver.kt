package com.githukudenis.coroutinesindustrialbuild.data.util

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    fun observe(): Flow<ConnectionStatus>
    enum class ConnectionStatus {
        AVAILABLE, UNAVAILABLE, LOSING, LOST
    }
}