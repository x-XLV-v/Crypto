package com.example.crypto.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.crypto.R

object PriceHelper {
    //В зависимости от того упала цена или выросла меняем цвет текста
    //на красный/зелёный, меняем иконку на стрелку вниз/вверх
    fun showChangePrice(textView: TextView, change: Double?) {
        val changePrice = "%.2f %%".format(change).trimParanthesis()
        textView.text = changePrice
        val context = textView.context

        if (changePrice.contains("-")) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.red))
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(context, R.drawable.baseline_arrow_downward), null
            )
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.green))
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(context, R.drawable.baseline_arrow_upward), null
            )
        }
    }
}