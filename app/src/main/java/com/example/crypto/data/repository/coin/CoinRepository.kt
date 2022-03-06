package com.example.crypto.data.repository.coin

import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val coinLocalDataSource: CoinLocalDataSource,
    private val coinRemoteDataSource: CoinRemoteDataSource
) {
    fun coinBySymbol(symbol: String) = coinLocalDataSource.coinBySymbol(symbol)

    suspend fun historyPrice(symbol: String, targetCur: String = "usd") = coinRemoteDataSource.historyPrice(symbol, targetCur)
}