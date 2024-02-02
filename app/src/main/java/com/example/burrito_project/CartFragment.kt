package com.example.burrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class CartFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCartItems()
    }

    private fun observeCartItems() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            refreshCartItems(items)
            updateTotalPrice(items)
        }
    }

    private fun refreshCartItems(items: List<Burrito>) {
        val linearLayout = view?.findViewById<LinearLayout>(R.id.cartItemsContainer)
        linearLayout?.removeAllViews()

        items.forEach { burrito ->
            val titleTextView = TextView(context).apply {
                text = "${burrito.title}"
                // Layout parameters, padding, and text size
            }

            val ingredientsTextView = TextView(context).apply {
                text = "Ingredients: ${burrito.ingredients.joinToString(", ")}"
                // Layout parameters, padding, and text size
            }

            val priceTextView = TextView(context).apply {
                text = "Price: $${burrito.price}"
                // Layout parameters, padding, and text size
            }

            val removeButton = Button(context).apply {
                text = "Remove"
                setOnClickListener {
                    viewModel.removeFromCart(burrito)
                }
                // Layout parameters
            }

            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(titleTextView)
                addView(ingredientsTextView)
                addView(priceTextView)
                addView(removeButton)
            }

            linearLayout?.addView(container)
        }
    }

    private fun updateTotalPrice(items: List<Burrito>) {
        val totalPrice = items.sumOf { it.price }
        view?.findViewById<TextView>(R.id.tvTotalPrice)?.text = "Total Price: $${totalPrice}"
    }
}
