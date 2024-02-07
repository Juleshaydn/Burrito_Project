package com.example.burrito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.GsonBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class CartFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String? = null

    // Declare totalPrice as a global variable
    private var totalPriceInCents: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        view.findViewById<Button>(R.id.btnPayTotalPrice).setOnClickListener {
            paymentIntentClientSecret?.let { secret ->
                paymentSheet.presentWithPaymentIntent(secret)
            } ?: Toast.makeText(activity, "Error: Client secret is null", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PaymentConfiguration.init(requireContext(), "pk_test_51OfC7RKYr1gbOYbz3bcanFhFoEnc03s2KWKfSQciP3t2NXLXejvtaZt6KoGGWyN4nkWx7vuUtg4Ob8VFxVRWPEWX00VZIX6RP7")
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        observeCartItems()
        fetchPaymentIntent()
    }

    private fun observeCartItems() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            refreshCartItems(items)
            // Calculate and update the total price whenever the cart items change
            totalPriceInCents = (items.sumOf { it.price } * 100).toLong()
            updateTotalPrice()
        }
    }

    // Update this function to use the global totalPriceInCents
    private fun updateTotalPrice() {
        view?.findViewById<TextView>(R.id.tvTotalPrice)?.text = "Total Price: $${totalPriceInCents / 100}"
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

    data class PaymentIntentRequest(val amount: Number)
    data class PaymentIntentResponse(val paymentIntent: String)
    interface BackendService {
        @FormUrlEncoded
        @POST("/Burrito-Server/index.php")
        fun createPaymentIntent(@Field("amount") amount: Long): Call<PaymentIntentResponse>
    }


    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                // Handle the payment success
            }
            is PaymentSheetResult.Canceled -> {
                // Handle the payment cancellation
            }
            is PaymentSheetResult.Failed -> {
                // Handle the payment failure
            }
        }
    }

    private fun fetchPaymentIntent() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2/Burrito-Server/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val backendService = retrofit.create(BackendService::class.java)

        val request = PaymentIntentRequest(amount = totalPriceInCents)

        val totalPriceInCents = viewModel.cartItems.value?.sumOf { it.price * 100 } ?: 0L

        backendService.createPaymentIntent(totalPriceInCents).enqueue(object : Callback<PaymentIntentResponse> {
            override fun onResponse(call: Call<PaymentIntentResponse>, response: Response<PaymentIntentResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("PaymentIntentResponse", "Response: $responseBody")
                    paymentIntentClientSecret = responseBody?.paymentIntent

                    if (paymentIntentClientSecret != null) {
                        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret!!)
                    } else {
                        Log.e("PaymentIntentError", "Client secret is null")
                        Toast.makeText(activity, "Error: Client secret is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PaymentIntentError", "Error creating payment intent: $errorBody")
                    Toast.makeText(activity, "Error creating payment intent", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentIntentResponse>, t: Throwable) {
                Log.e("NetworkError", "Failed to create payment intent", t)
                Toast.makeText(activity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}