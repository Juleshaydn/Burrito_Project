package com.example.burrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Assuming you have a LiveData or state to hold cart items
    private val _cartItems = MutableLiveData<List<Burrito>>(emptyList())
    val cartItems: LiveData<List<Burrito>> = _cartItems

    fun addToCart(burrito: Burrito) {
        val currentItems = _cartItems.value ?: emptyList()
        _cartItems.value = currentItems + burrito
    }

    fun removeFromCart(item: Burrito) {
        val updatedList = _cartItems.value.orEmpty().toMutableList()
        updatedList.remove(item)
        _cartItems.value = updatedList
    }
}
