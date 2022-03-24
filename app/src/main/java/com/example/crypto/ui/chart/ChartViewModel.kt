package com.example.crypto.ui.chart

import androidx.lifecycle.ViewModel
import com.example.crypto.data.repository.coin.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: CoinRepository): ViewModel() {

}