package com.example.burrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<Burrito>>(emptyList())
    val cartItems: LiveData<List<Burrito>> = _cartItems

    fun addToCart(item: Burrito) {
        val updatedList = _cartItems.value.orEmpty().toMutableList()
        updatedList.add(item)
        _cartItems.value = updatedList
    }

    fun removeFromCart(item: Burrito) {
        val updatedList = _cartItems.value.orEmpty().toMutableList()
        updatedList.remove(item)
        _cartItems.value = updatedList
    }
}
