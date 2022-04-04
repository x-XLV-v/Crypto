package com.example.crypto.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.api.Result
import com.example.crypto.data.repository.coin.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: CoinRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dataError = MutableLiveData<Boolean>()
    val dataError: LiveData<Boolean> = _dataError

    private val _historicalData = MutableLiveData<List<DoubleArray>>()
    val historicalData: LiveData<List<DoubleArray>> = _historicalData

    fun coinBySymbol(symbol: String) = repository.coinBySymbol(symbol)

    fun historicalData(symbol: String?) {
        symbol?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                val result = repository.historyPrice(symbol)
                _isLoading.postValue(false)

                when (result) {
                    is Result.Success -> {
                        _historicalData.postValue(result.data.price)
                        _dataError.postValue(false)
                    }
                    is Result.Error -> {
                        _dataError.postValue(true)
                    }
                }
            }
        }
    }
}