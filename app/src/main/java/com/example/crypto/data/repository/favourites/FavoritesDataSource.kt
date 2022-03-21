package com.example.crypto.data.repository.favourites

import androidx.lifecycle.LiveData
import com.example.crypto.data.local.database.CoinsDatabase
import com.example.crypto.data.local.database.CoinsListEntity
import javax.inject.Inject

class FavoritesDataSource @Inject constructor(private val database: CoinsDatabase) {
    //Получаем все избранные криптовалюты
    val favoriteCoins: LiveData<List<CoinsListEntity>> = database.coinsListDao().favoriteCoins()

    //Получаем список всех сокращённых имён избранных криптовалют
    suspend fun favoriteSymbol(): List<String> = database.coinsListDao().favouriteSymbols()

    //Обновление статуса избранного (добавлен/не добавлен)
    suspend fun updateFavoriteStatus(symbols: String): CoinsListEntity? {
        val coin = database.coinsListDao().coinFromSymbol(symbols)
        coin?.let {
            val coinsListEntity = CoinsListEntity(
                it.symbol,
                it.id,
                it.name,
                it.price,
                it.changePercent,
                it.image,
                it.isFavourite.not()
            )

            if (database.coinsListDao().updateCoinsListEntry(coinsListEntity) > 0) {
                return coinsListEntity
            }
        }
        return null
    }
}