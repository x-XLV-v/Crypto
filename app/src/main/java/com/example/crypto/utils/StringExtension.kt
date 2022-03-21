package com.example.crypto.utils

import java.text.DecimalFormat

//Проверяет строку на null, и при необходимости возвращает пустую строку
fun String?.emptyIfNull(): String {
    return this ?: ""
}

//Убираем скобки из строки
fun String?.trimParanthesis(): String {
    return this?.replace(Regex("[()]"), "") ?: ""
}

//Дабвляет строку в знак доллара
fun Double?.dollarString(): String {
    return this?.let {
        val numberFormat = DecimalFormat("#,##0.00")
        "$ ${numberFormat.format(this)}"
    } ?: ""
}