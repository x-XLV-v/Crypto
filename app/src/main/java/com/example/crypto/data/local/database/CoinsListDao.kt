package com.example.crypto.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoinsListDao {
    //Запрос для получения всего содержимого таблицы coins_list
    @Query("SELECT * FROM coins_list")
    fun coinsList(): LiveData<List<CoinsListEntity>>

    //Запрос для получения криптовалюты по значению поля symbol
    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    suspend fun coinFromSymbol(symbol: String): CoinsListEntity?

    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    fun coinLiveDataFromSymbol(symbol: String): List<CoinsListEntity>

    //Запрос для получения только избранных криптовалют
    @Query("SELECT * FROM coins_list WHERE isFavourite = 1")
    fun favoriteCoins(): LiveData<List<CoinsListEntity>>

    //Запрос для получения первичных ключей у избранных криптовалют
    @Query("SELECT symbol FROM coins_list WHERE isFavourite = 1")
    suspend fun favouriteSymbols(): List<String>

    //Добвления данных в таблицу
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CoinsListEntity>)

    //Обновление данных в таблице
    @Update
    suspend fun updateCoinsListEntry(data: CoinsListEntity): Int

    //Удаление данных из таблицы
    @Query("DELETE FROM coins_list")
    suspend fun deleteAll()
}