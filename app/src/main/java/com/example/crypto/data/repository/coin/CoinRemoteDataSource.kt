package com.example.crypto.data.repository.coin

import com.example.crypto.api.ApiInterface
import com.example.crypto.api.BaseRemoteDataSource
import com.example.crypto.api.Result
import com.example.crypto.api.model.HistoryPriceResponse
import javax.inject.Inject

class CoinRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {
    suspend fun historyPrice(symbol: String, targetCur: String, days: Int = 30): Result<HistoryPriceResponse> =
        getResult {
            service.historicalPrice(symbol, targetCur, days)
        }
}