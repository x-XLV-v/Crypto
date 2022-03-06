package com.example.crypto.data.repository.coinsList

import com.example.crypto.api.Result
import com.example.crypto.api.succesed
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.data.local.preferences.SharedPreferencesStorage
import com.example.crypto.utils.Constants
import java.util.*
import javax.inject.Inject

class CoinsListRepository @Inject constructor(
    private val coinsListRemoteDataSource: CoinsListRemoteDataSource,
    private val coinsListLocalDataSource: CoinsListLocalDataSource,
    private val sharedPreferencesStorage: SharedPreferencesStorage
) {
    val allCoinList = coinsListLocalDataSource.allCoinsList

    suspend fun coinsList(targetCur: String) {
        when (val result = coinsListRemoteDataSource.coinsList(targetCur)) {
            is Result.Success -> {
                if (result.succesed) {
                    val favSymbols = coinsListLocalDataSource.favouriteSymbols()

                    val customList = result.data.let {
                        it.filter { item -> item.symbol.isNullOrEmpty().not() }
                            .map { item ->
                                CoinsListEntity(
                                    item.symbol ?: "",
                                    item.id,
                                    item.name,
                                    item.price,
                                    item.changePercentage,
                                    item.image,
                                    favSymbols.contains(item.symbol)
                                )
                            }
                    }

                    coinsListLocalDataSource.insertCoinsIntoDatabase(customList)
                    sharedPreferencesStorage.timeLoadedAt = Date().time
                    Result.Success(true)
                }
                else {
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            else -> result as Result.Error
        }
    }

    suspend fun updateFavouriteStatus(symbol: String): com.example.crypto.api.Result<CoinsListEntity> {
        val result = coinsListLocalDataSource.updateFavouriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    fun loadData(): Boolean {
        val lastLoad = sharedPreferencesStorage.timeLoadedAt
        val currentTime = Date().time
        return currentTime - lastLoad > 15 * 1000
    }
}