package com.example.crypto.ui.main.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.api.Result
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.data.repository.favourites.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: FavoriteRepository): ViewModel() {
    //Хранит сообщение об ошибке
    private val _toastError = MutableLiveData<String>()
    val toastLoading: LiveData<String> = _toastError

    //Список всех избранных криптовалют
    val favoriteCoinsList: LiveData<List<CoinsListEntity>> = repository.favoriteCoins

    //Переменная для хранения конкретной избранной криптовалюты
    private val _favoriteStock = MutableLiveData<CoinsListEntity?>()
    val favoriteStock: LiveData<CoinsListEntity?> = _favoriteStock

    //Переменная для проверки пустоты в избранном
    private val _favoritesEmpty = MutableLiveData<Boolean>()
    val favoritesEmpty: LiveData<Boolean> = _favoritesEmpty

    //Функция для обновления статуса избранной криптовалюты
    fun updateFavoriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFavoriteStatus(symbol)
            when (result) {
                is Result.Success -> _favoriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun isFavoriteEmpty(empty: Boolean) {
        _favoritesEmpty.postValue(empty)
    }
}