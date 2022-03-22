package com.example.crypto.ui.main.coinList

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto.R
import com.example.crypto.adapter.CoinsListAdapter
import com.example.crypto.adapter.OnItemClickCallback
import com.example.crypto.common.MainNavigationFragment
import com.example.crypto.databinding.FragmentCoinListBinding
import com.example.crypto.utils.doOnChange
import kotlinx.android.synthetic.main.fragment_coin_list.*
import java.time.Duration

class CoinListFragment: MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var binding: FragmentCoinListBinding
    private var coinListAdapter = CoinsListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Функция вызывается, когда фрагмент прорисовывается впервые. Переменная inflater это объект класса LayoutInflater.
        //Данный класс из содержимого Layout файла создаёт view-элемент. Далее идёт вызов метода inflate. При помощи
        // метода inflate view-элемент создаётся. В него передаётся идентификатор Layout файла из которого view будет
        // создаваться. Родительский ViewGroup элемент для создаваемого view, то есть это переменная container. Последний
        // параметр указывает на то, сделать ли передаваемый в ViewGroup элемент корневым для этого ViewGroup. В данном случае
        // мы передаём false, так как нам не нужно устанавливать данный фрагмент в качестве root элемента, нам нужны только
        // некоторые параметры от этого root-элемента.
        binding = FragmentCoinListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@CoinListFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        viewModel.loadCoinsFromApi()
    }

    override fun initializeViews() {
        coinsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinListAdapter
        }
    }

    override fun observeViewModel() {
        viewModel.isLoading.doOnChange(this) { loading ->
            coinsListLoading.visibility = if (viewModel.isListEmpty() && loading) View.VISIBLE else View.GONE

            if (loading) coinsListErrorView.visibility = View.GONE
        }

        viewModel.coinsListData.doOnChange(this) {
            coinListAdapter.updateList(it)

            coinsListErrorView.visibility = if (viewModel.isListEmpty()) View.VISIBLE else View.GONE
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
        TODO("Not yet implemented")
    }

    override fun onFavoriteCLick(symbol: String) {
        viewModel.updateFavoriteStatus(symbol)
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}