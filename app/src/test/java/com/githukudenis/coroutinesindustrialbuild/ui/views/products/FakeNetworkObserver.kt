package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNetworkObserver: NetworkObserver {
    override fun observe(): Flow<NetworkObserver.ConnectionStatus> {
        return flowOf(NetworkObserver.ConnectionStatus.AVAILABLE)
    }
}