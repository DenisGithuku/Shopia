package com.githukudenis.feature_product.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkStateObserver(
    private val context: Context
): NetworkObserver {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<NetworkObserver.ConnectionStatus> {
        return callbackFlow {
            val listener = ConnectivityManager.OnNetworkActiveListener {
                launch {
                    send(NetworkObserver.ConnectionStatus.AVAILABLE)
                }
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(NetworkObserver.ConnectionStatus.AVAILABLE)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(NetworkObserver.ConnectionStatus.LOSING)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(NetworkObserver.ConnectionStatus.LOST)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(NetworkObserver.ConnectionStatus.AVAILABLE)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            connectivityManager.addDefaultNetworkActiveListener(listener)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
                connectivityManager.removeDefaultNetworkActiveListener(listener)
            }
        }.distinctUntilChanged()
    }
}