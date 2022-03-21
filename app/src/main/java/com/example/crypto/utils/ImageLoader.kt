package com.example.crypto.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import com.bumptech.glide.Glide
import com.example.crypto.R

object ImageLoader {
    fun loadImage(view: ImageView, url: String, placeholder: Int = R.drawable.baseline_image) {
        Glide.with(view)               //Передаём ImageView или Context
            .load(url)                 //Передаём ссылку на изображение
            .placeholder(placeholder)  //Указываем картинку по умолчанию
            .error(placeholder)        //Указываем картинку на случай какой-то ошибки
            .fallback(placeholder)     //Указываем картинку на случай, если сервер не отдаст картинку
            .into(view)                //Передаём ImageView, в который хотим загрузить изображение

    }
}