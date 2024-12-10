package com.example.tripit.fragments

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripit.HorizontalSpaceItemDecoration
import com.example.tripit.RecommendedDestination
import com.example.tripit.SCROLL_SLOW_FACTOR
import com.example.tripit.SlowScrollingLinearLayoutManager
import com.example.tripit.SlowScrollingPagerSnapHelper
import com.example.tripit.adapters.CardAdapter
import com.example.tripit.adapters.CardViewHolder
import com.example.tripit.databinding.FragmentPlaceDetailsBinding
import com.example.tripit.viewmodels.Card
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

    fun setDetails(detail: RecommendedDestination) {
        this.recommendedDestination = detail
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

        binding.apply {


            placeName.text=recommendedDestination?.place_name
            description.text=recommendedDestination?.description
            tripDuration.text=recommendedDestination?.duration
            city.text=recommendedDestination?.city
            placeTags.text=recommendedDestination?.processed_tags
            travelOptions.text=recommendedDestination?.travel_option
            travelStyle.text=recommendedDestination?.travel_style
            CoroutineScope(Dispatchers.Main).launch {
                val weatherDetails = fetchWeather(recommendedDestination?.city.toString())
                println(weatherDetails)
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
                        return@withContext parseWeatherResponse(it)
                    }
                } else {
                    return@withContext "Failed to fetch weather: ${response.code}"
                }
            } catch (e: Exception) {
                return@withContext "Error: ${e.message}"
            }
            return@withContext "Unexpected error occurred"
        }
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

        binding.weather.text= "$temperature°C"

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