package com.example.crypto.ui.main.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto.R
import com.example.crypto.adapter.CoinsListAdapter
import com.example.crypto.adapter.OnItemClickCallback
import com.example.crypto.common.MainNavigationFragment
import com.example.crypto.databinding.FragmentCoinListBinding
import com.example.crypto.databinding.FragmentFavouritesBinding
import com.example.crypto.ui.chart.ChartActivity
import com.example.crypto.ui.main.coinList.CoinListViewModel
import com.example.crypto.utils.Constants
import com.example.crypto.utils.doOnChange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_coin_list.*
import kotlinx.android.synthetic.main.fragment_favourites.*

@AndroidEntryPoint
class FavouritesFragment : MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentFavouritesBinding
    private var favoritesAdapter = CoinsListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavouritesFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun initializeViews() {
        coinsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    override fun observeViewModel() {
        viewModel.favoriteCoinsList.doOnChange(this) {
            favoritesAdapter.updateList(it)

            if (it.isEmpty()) {
                viewModel.isFavoriteEmpty(true)
            }
        }

        viewModel.toastError.doOnChange(this) {
            showToast(it)
        }

        viewModel.favoriteStock.doOnChange(this) {
            it?.let {
                showToast(
                    if (it.isFavourite)
                        "${it.symbol} добавлен в избранное"
                    else
                        "${it.symbol} удалён из избранного"
                )
            }
        }
    }

    override fun onItemClick(symbol: String, id: String) {
        requireActivity().run {
            startActivity(
                Intent(this, ChartActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_SYMBOL, symbol)
                        putExtra(Constants.EXTRA_SYMBOL_ID, id)
                    }
            )
        }
    }

    override fun onFavoriteCLick(symbol: String) {
        viewModel.updateFavoriteStatus(symbol)
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}