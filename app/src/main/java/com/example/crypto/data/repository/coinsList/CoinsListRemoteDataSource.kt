package com.example.crypto.data.repository.coinsList

import com.example.crypto.api.ApiInterface
import com.example.crypto.api.BaseRemoteDataSource
import com.example.crypto.api.Result
import com.example.crypto.api.model.Coin
import javax.inject.Inject

class CoinsListRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {
    suspend fun coinsList(targetCur: String): Result<List<Coin>> =
        getResult {
            service.coinList(targetCur)
        }
}