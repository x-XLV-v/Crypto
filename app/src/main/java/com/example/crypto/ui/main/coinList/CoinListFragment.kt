package com.example.crypto.ui.main.coinList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crypto.R

class CoinListFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Функция вызывается, когда фрагмент прорисовывается впервые. Переменная inflater это объект класса LayoutInflater.
        //Данный класс из содержимого Layout файла создавать view-элемент. Далее идёт вызов метода inflate. При помощи
        // метода inflate view-элемент создаётся. В него передаётся идентификатор Layout файла из которого view будет
        // создаваться. Родительский ViewGroup элемент для создаваемого view, то есть это переменная container. Последний
        // параметр указывает на то, сделать ли передаваемый в ViewGroup элемент корневым для этого ViewGroup. В данном случае
        // мы передаём false, так как нам не нужно устанавливать данный фрагмент в качестве root элемента, нам нужны только
        // некоторые параметры от этого root-элемента.
        return inflater.inflate(R.layout.fragment_coin_list, container, false)
    }
}