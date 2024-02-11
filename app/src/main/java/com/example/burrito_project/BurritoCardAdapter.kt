package com.example.burrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.burrito.databinding.ItemBurritoCardBinding

class BurritoCardAdapter(
    private var burritos: List<Burrito>,
    private val onAddToCartClicked: (Burrito) -> Unit
) : RecyclerView.Adapter<BurritoCardAdapter.BurritoCardViewHolder>() {

    class BurritoCardViewHolder(val binding: ItemBurritoCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(burrito: Burrito, onAddToCartClicked: (Burrito) -> Unit) {
            with(binding) {
                tvBurritoTitle.text = burrito.title
                tvBurritoPrice.text = "Price: $${burrito.price}"

                // Use the image name to get the drawable resource ID
                val imageResId = itemView.context.resources.getIdentifier(burrito.image, "drawable", itemView.context.packageName)
                ivBurritoImage.setImageResource(imageResId)

                btnAddToCartCard.setOnClickListener {
                    onAddToCartClicked(burrito)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurritoCardViewHolder {
        val binding = ItemBurritoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BurritoCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BurritoCardViewHolder, position: Int) {
        val burrito = burritos[position]
        holder.bind(burrito, onAddToCartClicked)
    }

    override fun getItemCount(): Int = burritos.size

    fun updateBurritos(burritosList: List<Burrito>) {
        this.burritos = burritosList
        notifyDataSetChanged()
    }
}

data class Burrito(
    val id: Int = 0,
    val title: String,
    val ingredients: List<String> = listOf(),
    val price: Long,
    val image: String // This will store the image name without the extension
)
 {
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

