package com.example.crypto.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto.R
import kotlinx.android.synthetic.main.item_coin_list.view.*
import com.example.crypto.data.local.database.CoinsListEntity
import com.example.crypto.utils.ImageLoader
import com.example.crypto.utils.PriceHelper
import com.example.crypto.utils.dollarString
import com.example.crypto.utils.emptyIfNull

//Данный интерфейс используется для обработки клика по элементу из RecyclerView
interface OnItemClickCallback {
    //Клик по элементу из списка
    fun onItemClick(symbol: String, id: String)

    //Клик по иконке добавления/удаления из избранного
    fun onFavoriteCLick(symbol: String)
}

class CoinsListAdapter(private val onItemClickCallback: OnItemClickCallback):
    RecyclerView.Adapter<CoinsListAdapter.CoinsListViewHolder>() {
    private val coinsList: MutableList<CoinsListEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        return CoinsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        holder.bind(coinsList[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = coinsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<CoinsListEntity>) {
        this.coinsList.clear()             //Очищаем список от старых элементов
        this.coinsList.addAll(list)        //Заполняем список свежими данными
        notifyDataSetChanged()             //Обновляем RecyclerView
    }

    class CoinsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: CoinsListEntity, onItemClickCallback: OnItemClickCallback) {
            itemView.coinsItemSymbolTextView.text = model.symbol
            //Проверка на null и устанавливаем пустую строку, если true
            itemView.coinsItemNameTextView.text = model.name.emptyIfNull()
            //Добавление символа доллара к цене
            itemView.coinsItemPriceTextView.text = model.price.dollarString()

            //Проверка падение цены. Если цена упала, то устанавлваем красный цвет и стрелку вниз.
            //В противном случае устанавливаем зелёный текст и стрелку вверх
            PriceHelper.showChangePrice(itemView.coinsItemChangeTextView, model.changePercent)

            //Проверяем, что если элемент находится в списке избранного, то устанавливаем
            //иконку item_favorite, иначе item_favorite_border
            itemView.favoriteImageView.setImageResource(
                if (model.isFavourite) R.drawable.item_favorite
                else R.drawable.item_favorite_border
            )

            //Обработка нажатия на кнопку добавления/удаления избранного
            itemView.favoriteImageView.setOnClickListener {
                onItemClickCallback.onFavoriteCLick(model.symbol)
            }

            //Загружаем изображение криптовалюты
            ImageLoader.loadImage(itemView.coinsItemImageView, model.image ?: "")

            //Обрботка клика по элементу списка
            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(
                    model.symbol,
                    model.id ?: model.symbol
                )
            }
        }
    }
}