package com.example.tripit.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripit.PlaceItem
import com.example.tripit.PostApiPlaces
import com.example.tripit.PredictionAdapter
import com.example.tripit.Preditction
import com.example.tripit.R
import com.example.tripit.RecommendationRequest
import com.example.tripit.RecommendationResponse
import com.example.tripit.databinding.FragmentTripPredictBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.apache.commons.text.similarity.FuzzyScore
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class TripPredictFragment : Fragment() {

    private lateinit var binding : FragmentTripPredictBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var progressDialog: ProgressDialog? = null
    val NamesOfPlaces = listOf(
        "andretta",
        "awahdevi",
        "baba_balak_nath_temple",
        "baba_garib_nath_temple",
        "bahardurpur_fort",
        "bangana_lathain_piplu",
        "barot_valley",
        "bhagsu_nath_temple",
        "bhakra_dam",
        "bhuru_singh_museum",
        "bijli_mahadev_temple",
        "bil_kaleshwar",
        "bon_monastery",
        "chamba_chaugan",
        "chamba_palace",
        "chamera_lake",
        "champavati",
        "chamunda_devi",
        "chandrataal_lake",
        "changar",
        "chintapurni_temple",
        "chitkul_village",
        "christ_church",
        "dagshai",
        "deoli_fish_farm",
        "deosidh_temple",
        "dera_baba_barbhag",
        "dera_baba_rudru",
        "dera_sant_pura",
        "dhankar_monastery",
        "dharamshala_mahanta",
        "dharamshala",
        "gobind_sagar_lake",
        "great_himalayan_national_park",
        "gurudwara_sahib_amb",
        "hamirpur_stadium",
        "himalayan_bird_park",
        "hippie_point",
        "iias",
        "jakhoo_temple",
        "janhjeli",
        "jatoli_shiv_temple",
        "kalatop",
        "kalpa",
        "kamru_fort",
        "kamrunag_lake",
        "kandraur_bridge",
        "kangra_fort",
        "kangra_valley",
        "kareri_lake_trek",
        "karol_tibba",
        "kasauli",
        "kasol",
        "katghar_temple",
        "kaza",
        "key_monastery",
        "khajjar",
        "kiala_forest",
        "kibber_village",
        "kinnaur_kailaish",
        "kothi",
        "kufri",
        "kunzum+pass",
        "kuthar_fort",
        "langza",
        "laxmi_narayan_temple",
        "losar_khas",
        "maharana_pratap_sagar",
        "malana",
        "mall_road",
        "manali_sanctuary",
        "mandi_palace",
        "manikaran_sahib",
        "manimahesh",
        "markandeya_temple",
        "masroor_temple",
        "mohan_shakti_heritage_park",
        "nadaun",
        "naggar_castle",
        "naina_devi",
        "nako_lake",
        "nhutnath_temple",
        "pandoh_dam",
        "pangi",
        "panj_teerthi",
        "parashar_lake",
        "pin_valley",
        "pong_dam",
        "raghunath_temple",
        "reckong_pao",
        "rewalsar_lake",
        "ribba_village",
        "roghi",
        "sangla_valley",
        "sheetla_mata_temple",
        "shikari_devi_temple",
        "shimla_state_museum",
        "shiv_temple",
        "shoolani_temple",
        "sirmaur_palace",
        "sirmaur_park",
        "solan_brewery",
        "solang",
        "sujanpur_tira",
        "sundar_nagar_lake",
        "swarghat",
        "tabo_monastery",
        "talai",
        "tara_devi_temple",
        "tauni_devi_temple",
        "the_ridge",
        "tirthan_valley",
        "triund_trek",
        "una_fort"
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripPredictBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check for runtime location permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            binding.Location.setOnClickListener {
//                requestLocation()
//            }

        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        binding.itineraryBtn.setOnClickListener {
            loadFragment(ItineraryFragment())
        }

        val items = listOf(
           "Landmarks",
                    "Religious Sites",
                    "Adventure",
                    "Historical",
                    "Nature",
                    "Wildlife",
                    "Scenic Views"
        )


//        val adapter = ArrayAdapter(requireContext(),R.layout.item_text,items)
//        binding.signupClass.setAdapter(adapter)

//        val recommendedDestinations = listOf(
//            Preditction("Bilaspur", "Naina Devi Temple", "Religious Sites", 4.5),
//            Preditction("Una", "Chintpurni Temple", "Religious Sites", 4.5),
//            Preditction("Una", "Dharamshala Mahanta", "Religious Sites", 4.1),
//            Preditction("Simraur", "Baba Balak Nath Temple", "Religious Sites", 4.2),
//            Preditction("Simraur", "Kathgarh Temple", "Religious Sites", 4.1),
//            Preditction("Una", "Gurudwara Sahib Amb", "Religious Sites", 4.0),
//            Preditction("Hamirpur", "Baba Balak Nath Temple", "Religious Sites", 4.4),
//            Preditction("Simraur", "Shiv Temple", "Religious Sites", 4.0),
//            Preditction("Una", "Dera Sant Pura Danna", "Religious Sites", 4.0),
//            Preditction("Bilaspur", "Markandeya Temple", "Religious Sites", 4.1)
//        )
        binding.PredictionRecyclerview.layoutManager = LinearLayoutManager(context)
     //   binding.PredictionRecyclerview.adapter = PredictionAdapter(recommendedDestinations.toList())



        binding.apply {

        }



        binding.Search.setOnClickListener {

         //   Log.d("Text","$theme $ratingStr $daysStr $latitudeStr $longitudeStr")

            if (binding.inputCity.text.toString().isNullOrEmpty() ) {

                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()


                // Continue with your API call or other logic
            } else {
                showprogressbar()
                getPrediction()
                // Show a toast indicating that one or more fields are empty

            }
        }



        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
        transaction.addToBackStack(null)
    }

    private fun getPrediction(){


        val jsonObject = JSONObject().apply {
            put("theme", "Religious Sites")
            put("rating", 3)
            put("days", 1)
            put("latitude",31.59331277)
            put("longitude",76.27356938)

        }



        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())

        val apiservce = PostApiPlaces.apiService
//        val call = apiservce.postData(theme = binding.signupClass.text.toString(),
//            rating = binding.Ratings.text.toString().toInt(),
//            days =  binding.DaysEditText.text.toString().toInt(),
//            latitude = binding.Latitude.text.toString().toDouble(),
//            longitude = binding.Longitude.text.toString().toDouble())

//        val call = apiservce.postData(binding.Latitude.text.toString(),
//            top_n =  binding.DaysEditText.text.toString().toInt(),
//            city = binding.Longitude.text.toString())

        val request = RecommendationRequest(
            user_input = ,
            city =  binding.inputCity.text.toString(),
            top_n = 5
        )

        PostApiPlaces.apiService.getRecommendations(request).enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: Response<RecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    val recommendations = response.body()?.recommendations
                    val destinationList = mutableListOf<Preditction>()
                    recommendations?.forEach { recommendation ->
                        val infor = Preditction(recommendation.place_name,recommendation.city,recommendation.description)
                        destinationList.add(infor)
                        Log.d("Recommendation", "${recommendation.place_name}: ${recommendation.description}")

                    }

                    dismissprogressbar()
                binding.PredictionRecyclerview.adapter = PredictionAdapter(destinationList)
                } else {
                    Log.e("API Error", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                Log.e("API Failure", t.message.toString())
            }
        })
    }


//        call.enqueue(object : Callback<RecommendationResponse> {
//            override fun onResponse(
//                call: Call<RecommendationResponse>,
//                response: Response<RecommendationResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val recommendationResponse = response.body()
//                    val recommendedDestinations = recommendationResponse?.recommendedDestinations
//
//                    if (recommendedDestinations != null && recommendedDestinations.isNotEmpty()) {
//                        // Create a list to store destination information
//                        val destinationList = mutableListOf<Preditction>()
//
//                        // Process the list of recommended destinations
//                        for (destination in recommendedDestinations) {
//
//                            val infor = Preditction(destination.place_name,destination.city,destination.description)
//                            val destinationInfo = "" +
//                                    "" +
//                                    "" +
//                                    "Place Name: ${destination.place_name}, " +
//                                    "Theme: ${destination.city}, " +
//                                    "Rating: ${destination.description}, "
//
//                            // Add the destination information to the list
//                            destinationList.add(infor)
//
//                            Log.d("Kanishk1", destinationInfo)
//                            // Add your logic to handle each recommended destination
//                        }
//                        dismissprogressbar()
//
//                        binding.PredictionRecyclerview.adapter = PredictionAdapter(destinationList)
//                        // Now, destinationList contains information about each recommended destination
//                        // You can use this list as needed
//                    } else {
//                        dismissprogressbar()
//                        Log.d("Kanishk1", "No recommended destinations found")
//                    }
//                } else {
//                    // Request failed
//                    dismissprogressbar()
//                    Log.e("Kanishk", "HTTP status code: ${response.code()}")
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("KanishkErr", "Error body: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
//                // Request failed due to network error or other issues
//                dismissprogressbar()
//                Log.d("Kanishk", t.toString())
//            }
//        })

//        call.enqueue(object : Callback<RecommendationResponse> {
//            override fun onResponse(call: Call<RecommendationResponse>, response: Response<RecommendationResponse>) {
//                if (response.isSuccessful) {
//
//                    Log.d("Kanishk1", response.body().toString())
//
//                    //  Toast.makeText(requireContext(),"Notice Sent Successfully",Toast.LENGTH_SHORT).show()
//                } else {
//                    //dismissprogressbar()
//                    // Request failed
//                    Log.e("Kanishk", "HTTP status code: ${response.code()}")
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("KanishkErr", "Error body: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
//                // Request failed due to network error or other issues
//                Log.d("Kanishk",t.toString())
//            }
//        })







//    private fun putImage(ImageName : String){
//
//        val fuzzyScore = FuzzyScore(Locale.getDefault())
//        val similarity = fuzzyScore.fuzzyScore(firebaseStorageName.toLowerCase(), ImageName)
//
//        val threshold = 80  // Adjust the threshold based on your tolerance for variations
//
//        if (similarity >= threshold) {
//            // Strings match, perform your logic here
//            // For example, load the image
//            println("Strings match with a similarity of $similarity%")
//        } else {
//            // Strings do not match
//            println("Strings do not match. Similarity is $similarity%")
//        }
//    }


    /*@SuppressLint("MissingPermission")
    private fun requestLocation() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Handle the obtained location
                    val latitude = location.latitude
                    val longitude = location.longitude


//                    binding.Latitude.setText(latitude.toString())
//                    binding.Longitude.setText(longitude.toString())

                    // Use latitude and longitude as needed
                    println("Latitude: $latitude, Longitude: $longitude")
                } ?: run {
                    // Last known location is null, request location updates if needed
                    // e.g., fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to get location
                println("Failed to get location: ${exception.message}")
            }
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
//                requestLocation()
            } else {
                // Permission denied, handle accordingly
                println("Location permission denied")
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }


    private fun showprogressbar(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

    }
    private fun dismissprogressbar(){
        progressDialog?.dismiss()
    }

}