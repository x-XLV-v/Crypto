package com.example.crypto.ui.main.coinList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.api.Result
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.data.repository.coinsList.CoinsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(private val repository: CoinsListRepository): ViewModel() {
    //Хранит состояние текущей загрузки (идёт загрузка или нет)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //Хранит сообщение об ошибке
    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    //Хранит полный список криптовалют
    val coinsListData = repository.allCoinList

    //Переменная для хранения конкретной избранной криптовалюты
    private val _favoriteStock = MutableLiveData<CoinsListEntity?>()
    val favoriteStock: LiveData<CoinsListEntity?> = _favoriteStock

    //Проверяем пуст ли наш список
    fun isListEmpty(): Boolean {
        return coinsListData.value?.isEmpty() ?: true
    }

    //Мы запрашиваем данные из сети
    fun loadCoinsFromApi(targetCur: String = "usd") {
        if (repository.loadData()) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                repository.coinsList(targetCur)
                _isLoading.postValue(false)
            }
        }
    }

    //Изменение статуса избранного (добавление/удаление)
    fun updateFavoriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFavouriteStatus(symbol)
            when (result) {
                is Result.Success -> _favoriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }
}