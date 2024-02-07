package com.example.burrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.burrito.databinding.ItemBurritoCardBinding

class BurritoCardAdapter(private val burritos: List<Burrito>) : RecyclerView.Adapter<BurritoCardAdapter.BurritoCardViewHolder>() {

    class BurritoCardViewHolder(val binding: ItemBurritoCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurritoCardViewHolder {
        val binding = ItemBurritoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BurritoCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BurritoCardViewHolder, position: Int) {
        val burrito = burritos[position]
        holder.binding.tvBurritoTitle.text = burrito.title
    }

    override fun getItemCount(): Int = burritos.size
}

data class Burrito(
    val id: Int = 0,
    val title: String,
    val ingredients: List<String> = listOf(),
    val price: Long
) {
    companion object {
        private var counter = 1

        fun createNextId(): Int {
            return counter++
        }
    }
}

data class Order(
    val items: List<Burrito> = listOf(),
    var price: Double = 0.0
)

