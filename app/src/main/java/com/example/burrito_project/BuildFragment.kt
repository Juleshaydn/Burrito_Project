package com.example.burrito


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuildFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuildFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()


    // Other necessary code ...

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_build, container, false)

        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
        btnAddToCart.setOnClickListener {
            addToCart()
        }

        return view
    }

    private fun calculatePrice(ingredients: List<String>): Double {
        val pricePerIngredient = mapOf(
            "Rice" to 1.0,
            "Beans" to 1.5,
            "Cheese" to 2.0,
            "Salsa" to 1.2
            // Add more ingredients and their prices here
        )

        return ingredients.sumOf { ingredient ->
            pricePerIngredient[ingredient] ?: 0.0
        }
    }

    private fun addToCart() {
        val selectedIngredients = mutableListOf<String>()
        view?.findViewById<CheckBox>(R.id.cbRice)?.let {
            if (it.isChecked) selectedIngredients.add(it.text.toString())
        }
        view?.findViewById<CheckBox>(R.id.cbBeans)?.let {
            if (it.isChecked) selectedIngredients.add(it.text.toString())
        }
        view?.findViewById<CheckBox>(R.id.cbCheese)?.let {
            if (it.isChecked) selectedIngredients.add(it.text.toString())
        }
        view?.findViewById<CheckBox>(R.id.cbSalsa)?.let {
            if (it.isChecked) selectedIngredients.add(it.text.toString())
        }

        val priceInDollars = calculatePrice(selectedIngredients)
        val priceInCents = (priceInDollars * 100).toLong() // Convert to cents and then to Long

        // Assign a string that represents the image resource or file name
        val imageResource = "burrito_image1" // Replace with actual resource name

        val newBurrito = Burrito(
            id = Burrito.createNextId(),
            title = "Custom Burrito",
            ingredients = selectedIngredients,
            price = priceInCents,
            image = imageResource // Assign the image resource name here
        )
        viewModel.addToCart(newBurrito)
        Toast.makeText(context, "Added custom burrito to cart", Toast.LENGTH_SHORT).show()
    }

    private fun generateUniqueID(): Int {
        // Implement a way to generate a unique ID for each burrito
        return Random.nextInt()
    }
}
