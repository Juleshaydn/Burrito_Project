package com.example.burrito

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    private lateinit var burritoAdapter: BurritoCardAdapter
    private val viewModel: SharedViewModel by activityViewModels() // Assuming you have a ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        loadBurritosFromJson()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvBurritos)
        burritoAdapter = BurritoCardAdapter(emptyList()) { burrito ->
            viewModel.addToCart(burrito) // Call a function in your ViewModel to update the cart
            Toast.makeText(context, "${burrito.title} added to cart", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = burritoAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadBurritosFromJson() {
        try {
            val jsonString = requireContext().assets.open("burritos.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val itemType = object : TypeToken<List<Burrito>>() {}.type
            val burritosList: List<Burrito> = gson.fromJson(jsonString, itemType)
            burritoAdapter.updateBurritos(burritosList)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load burritos: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("MenuFragment", "Error loading burritos", e)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}