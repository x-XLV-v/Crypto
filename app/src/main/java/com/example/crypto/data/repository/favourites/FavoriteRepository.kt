package com.example.crypto.data.repository.favourites

import androidx.lifecycle.LiveData
import com.example.crypto.api.Result
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.utils.Constants
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoritesDataSource: FavoritesDataSource) {
    //Полный список избранного
    val favoriteCoins: LiveData<List<CoinsListEntity>> = favoritesDataSource.favoriteCoins

    //Функция получения сокращённых имён всех избранных криптовалют
    suspend fun favoriteSymbols(): List<String> = favoritesDataSource.favoriteSymbol()

    //Функция для обновления статуса избранного
    suspend fun updateFavoriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = favoritesDataSource.updateFavoriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }
}