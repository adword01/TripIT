package com.example.tripit.fragments

import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R

import androidx.lifecycle.lifecycleScope
import com.example.tripit.HorizontalSpaceItemDecoration
import com.example.tripit.RecommendedDestination
import com.example.tripit.SCROLL_SLOW_FACTOR
import com.example.tripit.SlowScrollingLinearLayoutManager
import com.example.tripit.SlowScrollingPagerSnapHelper
import com.example.tripit.adapters.CardAdapter
import com.example.tripit.adapters.CardViewHolder
import com.example.tripit.customInfoDialog
import com.example.tripit.customprogressbar
import com.example.tripit.databinding.FragmentPlaceDetailsBinding
import com.example.tripit.dataclasses.Category
import com.example.tripit.dataclasses.categoryMap
import com.example.tripit.viewmodels.Card
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.math.floor


class PlaceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaceDetailsFragment()
    }

    private var recommendedDestination: RecommendedDestination? = null
    private lateinit var binding : FragmentPlaceDetailsBinding

    private lateinit var customDialog : customprogressbar

    fun setDetails(detail: RecommendedDestination) {
        this.recommendedDestination = detail
    }

    private fun showprogressbar(){
        customDialog = customprogressbar(requireActivity());
        customDialog.create()
        customDialog.setDialogText("Loading Info....")
        customDialog.setCancelable(false)
        customDialog.show()

    }
    private fun dismissprogressbar(){
        customDialog.dismiss()
    }

    private lateinit var recyclerView1: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceDetailsBinding.inflate(layoutInflater,container,false)

        showprogressbar()
        binding.apply {


            placeName.text=recommendedDestination?.Place_name
            description.text=recommendedDestination?.description
            tripDuration.text=recommendedDestination?.Duration
            city.text=recommendedDestination?.District
            placeTags.text=recommendedDestination?.Tags
            travelOptions.text=recommendedDestination?.Travel_option
            travelStyle.text=recommendedDestination?.Travel_style
            Log.d("TripPredicted",recommendedDestination.toString())
            CoroutineScope(Dispatchers.Main).launch {
                val weatherDetails = fetchWeather(recommendedDestination?.city.toString())
                println(weatherDetails)
            }
            viewPackingList.setOnClickListener{
                showprogressbar()
                fetchPackingList()
            }



        setupRecyclerViews()

        lifecycleScope.launchWhenResumed {
            val order = listOf(0, 1, 2, 1, 0)
            var i = 0
            while (true) {
                val position = order[i % order.size]
                recyclerView1.smoothScrollToPosition(position)
                i++
                delay(floor(300 * SCROLL_SLOW_FACTOR).toLong())
            }
        }
        }
        return binding.root
    }

    private fun setupRecyclerViews() {
        binding.recyclerView1.init(withParallax = true)
    }

    private fun RecyclerView.init(withParallax: Boolean) {
        adapter = CardAdapter(Card.MOCKED_ITEMS)
        layoutManager = SlowScrollingLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL)

        SlowScrollingPagerSnapHelper(context).attachToRecyclerView(this)

        val spacing = resources.getDimensionPixelOffset(R.dimen.spacing_card)
        addItemDecoration(HorizontalSpaceItemDecoration(spacing))

        if (withParallax) setupParallax()
    }

    private fun RecyclerView.setupParallax() {
        val viewBounds = Rect()
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = layoutManager as? LinearLayoutManager ?: return
                val scrollOffset = recyclerView.computeHorizontalScrollOffset()
                layoutManager.visiblePositions.forEach { position ->
                    val viewHolder = findViewHolderForAdapterPosition(position) as? CardViewHolder
                        ?: return@forEach

                    recyclerView.getDecoratedBoundsWithMargins(viewHolder.itemView, viewBounds)

                    val width = viewBounds.width().toFloat()
                    val viewStart = position * width
                    viewHolder.parallaxOffset = (viewStart - scrollOffset) / width
                }
            }
        })
    }

    fun fetchPackingList() {
        val db = FirebaseFirestore.getInstance()

        // Reference to the global list document in the packing list collection
        val docRef = db.collection("PackingList").document("Global_Packing_List")

        // Fetch the document
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val stringBuilder = StringBuilder()
                    val spannableStringBuilder = SpannableStringBuilder()
                    // Fetch all categories (maps) from the global list document
                    val categories = document.data?.map { entry ->
                        // Extracting each category map (e.g., category1, category2)
                        val categoryName = entry.key // This will give the category name (category1, category2, etc.)
                        val categoryItemsMap = entry.value as? Map<String, Any> ?: return@map null

                        // Extract items in the category
                        val items = categoryItemsMap.mapNotNull { itemEntry ->
                            val itemMap = itemEntry.value as? Map<String, Any> ?: return@mapNotNull null
                            val name = itemMap["name"] as? String ?: ""
                            val quantity = itemMap["quantity"] as? String ?: ""
                            val conditions = itemMap["conditions"] as? List<String> ?: emptyList()

                            // Creating a MedicalItem from the extracted data
                            categoryMap(name, quantity, conditions)
                        }

                        // Creating a Category object for each category
                        Category(categoryName, items)
                    }?.filterNotNull() ?: emptyList()

                    // Processing the fetched categories and items

                    categories.forEach { category ->
                        val categoryTitle = "Category: ${category.categoryName}\n"
                        val categoryStart = spannableStringBuilder.length
                        spannableStringBuilder.append(categoryTitle)
                        spannableStringBuilder.setSpan(
                            StyleSpan(Typeface.BOLD),
                            categoryStart,
                            spannableStringBuilder.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        category.items.forEach { item ->
                            spannableStringBuilder.append(" - Name: ${item.name}\n")
                            spannableStringBuilder.append("   Quantity: ${item.quantity}\n")
                            spannableStringBuilder.append("   Conditions: ${item.conditions.joinToString(", ")}\n\n")

                            println("Name: ${item.name}, Quantity: ${item.quantity}, Conditions: ${item.conditions}")
                        }
                    }
                    dismissprogressbar()
                    val customDialog = customInfoDialog(requireContext());
                    customDialog.create()
                    customDialog.setDialogText("Packing List",spannableStringBuilder.toString())
                    customDialog.setCancelable(false)
                    customDialog.show()
                } else {
                    dismissprogressbar()
                    println("Document does not exist!")
                }
            }
            .addOnFailureListener { exception ->
                dismissprogressbar()
                println("Error fetching data: ${exception.message}")
            }
    }


    suspend fun fetchWeather(city: String): String {
        return withContext(Dispatchers.IO) { // Perform network request on IO dispatcher
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://open-weather13.p.rapidapi.com/city/$city/EN")
                .get()
                .addHeader("x-rapidapi-key", "6d81e91983msha29f724ed180833p1c98a4jsnb9dddd5f4933")
                .addHeader("x-rapidapi-host", "open-weather13.p.rapidapi.com")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        dismissprogressbar()
                        return@withContext parseWeatherResponse(it)
                    }
                } else {
                    dismissprogressbar()
                    return@withContext "Failed to fetch weather: ${response.code}"
                }
            } catch (e: Exception) {
                dismissprogressbar()
                return@withContext "Error: ${e.message}"
            }
            return@withContext "Unexpected error occurred"
        }
    }

    fun convertFahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

    fun parseWeatherResponse(response: String): String {
        val jsonObject = JSONObject(response)
        val weatherArray = jsonObject.getJSONArray("weather")
        val weatherDescription = weatherArray.getJSONObject(0).getString("description")
        val mainObject = jsonObject.getJSONObject("main")
        val temperature = mainObject.getDouble("temp")
        val feelsLike = mainObject.getDouble("feels_like")
        val humidity = mainObject.getInt("humidity")
        val windObject = jsonObject.getJSONObject("wind")
        val windSpeed = windObject.getDouble("speed")

        binding.weather.text = "${"%.2f".format(convertFahrenheitToCelsius(temperature))} °C"


        return """
        Weather: $weatherDescription
        Temperature: $temperature°C
        Feels Like: $feelsLike°C
        Humidity: $humidity%
        Wind Speed: $windSpeed m/s
    """.trimIndent()
    }


    private val LinearLayoutManager.visiblePositions: IntRange
        get() = (findFirstVisibleItemPosition()..findLastVisibleItemPosition())
}