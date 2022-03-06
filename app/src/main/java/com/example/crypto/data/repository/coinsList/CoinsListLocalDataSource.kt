package com.example.crypto.data.repository.coinsList

import androidx.lifecycle.LiveData
import com.example.crypto.data.local.database.CoinsDatabase
import com.example.crypto.data.local.database.CoinsListEntity
import javax.inject.Inject

class CoinsListLocalDataSource @Inject constructor(private val database: CoinsDatabase) {
    val allCoinsList: LiveData<List<CoinsListEntity>> = database.coinsListDao().coinsList()

    suspend fun insertCoinsIntoDatabase(coinToInsert: List<CoinsListEntity>) {
        if (coinToInsert.isNotEmpty()) {
            database.coinsListDao().insert(coinToInsert)
        }
    }

    suspend fun favouriteSymbols(): List<String> = database.coinsListDao().favouriteSymbols()

    suspend fun updateFavouriteStatus(symbol: String): CoinsListEntity? {
        val coin = database.coinsListDao().coinFromSymbol(symbol)
        coin?.let {
            val coinsListEntity = CoinsListEntity(
                it.symbol,
                it.id,
                it.name,
                it.price,
                it.changePercent,
                it.image,
                it.isFavourite.not(),
            )

            if (database.coinsListDao().updateCoinsListEntry(coinsListEntity) > 0) {
                return coinsListEntity
            }
        }

        return null
    }
}