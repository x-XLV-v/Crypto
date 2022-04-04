package com.example.crypto.ui.chart

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.crypto.R
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.databinding.ActivityChartBinding
import com.example.crypto.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.android.synthetic.main.item_coin_list.*
import kotlinx.android.synthetic.main.item_line_chart.*

@AndroidEntryPoint
class ChartActivity: AppCompatActivity() {
    private val viewModel: ChartViewModel by viewModels()
    private lateinit var binding: ActivityChartBinding

    private var symbol: String? = null
    private var symbolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart)
        binding.apply {
            lifecycleOwner = this@ChartActivity
            viewModel = this@ChartActivity.viewModel
        }

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL) == true) {
            symbol = intent?.getStringExtra(Constants.EXTRA_SYMBOL)
        }

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL_ID) == true) {
            symbol = intent?.getStringExtra(Constants.EXTRA_SYMBOL_ID)
        }

        supportActionBar?.title = symbol ?: ""

        observeViewModel()

        viewModel.historicalData(symbolId)
    }

    private fun observeViewModel() {
        symbol?.let {
            viewModel.coinBySymbol(it).doOnChange(this) { coin ->
                initializeViews(coin)
            }

            viewModel.historicalData.doOnChange(this) { data ->
                ChartHelper.drawHistoricalLineChart(lineChart, it, data)
            }

            viewModel.dataError.doOnChange(this) { error ->
                if (error) showToast("Не удалось получить данные об изменении цены")
            }
        }
    }

    private fun initializeViews(coinsListEntity: CoinsListEntity) {
        coinsItemSymbolTextView.text = coinsListEntity.symbol
        coinNameTextView.text = coinsListEntity.symbol
        coinPriceTextView.text = coinsListEntity.price.dollarString()
        PriceHelper.showChangePrice(coinChangeTextView, coinsListEntity.changePercent)
        ImageLoader.loadImage(coinImageView, coinsListEntity.image ?: "")
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}