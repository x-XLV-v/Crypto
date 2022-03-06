package com.example.crypto.data.repository.coin

import com.example.crypto.data.local.database.CoinsDatabase
import javax.inject.Inject

class CoinLocalDataSource @Inject constructor(private val database: CoinsDatabase) {
    fun coinBySymbol(symbol: String) = database.coinsListDao().coinLiveDataFromSymbol(symbol)
}