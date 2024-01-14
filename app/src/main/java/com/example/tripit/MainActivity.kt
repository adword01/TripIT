package com.example.tripit



import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tripit.PostApiPlaces.apiService
import com.example.tripit.fragments.RecommendedDestination
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var tflite: Interpreter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val recommendationApi = PostApiPlaces.apiService

        val recommendationApi = PostApiPlaces.apiService

//        ApiTask(object : ApiTask.Callback {
//            override fun onResponse(response: String?) {
//                // Handle the response here, for example, update UI components
//                Log.d("API_RESPONSE", response ?: "No response")
//            }
//        }).execute()

        val request = RecommendationRequest(
            theme = "Religious Sites",
            rating = 3,
            days = 1,
            latitude = 31.59331277,
            longitude = 76.27356938
        )


        val jsonObject = JSONObject().apply {
            put("theme", "Religious Sites")
            put("rating", 3)
            put("days", 1)
            put("latitude",31.59331277)
            put("longitude",76.27356938)

        }



        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())

        val apiservce = PostApiPlaces.apiService
        val call = apiservce.postData(theme = "Religious Sites",
            rating = 3,
            days =  1,
            latitude = 31.59331277,
            longitude = 76.27356938)

        call.enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: Response<RecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    val recommendationResponse = response.body()
                    val recommendedDestinations = recommendationResponse?.recommendedDestinations

                    if (recommendedDestinations != null && recommendedDestinations.isNotEmpty()) {
                        // Create a list to store destination information
                        val destinationList = mutableListOf<Preditction>()

                        // Process the list of recommended destinations
                        for (destination in recommendedDestinations) {

                            val infor = Preditction(destination.District,destination.placeName,destination.Theme,destination.Rating)
                            val destinationInfo = "" +
                                    "" +
                                    "" +
                                    "Place Name: ${destination.placeName}, " +
                                    "Theme: ${destination.Theme}, " +
                                    "Rating: ${destination.Rating}, " +
                                    "District: ${destination.District}"

                            // Add the destination information to the list
                            destinationList.add(infor)

                            Log.d("Kanishk1", destinationInfo)
                            // Add your logic to handle each recommended destination
                        }

                        // Now, destinationList contains information about each recommended destination
                        // You can use this list as needed
                    } else {
                        Log.d("Kanishk1", "No recommended destinations found")
                    }
                } else {
                    // Request failed
                    Log.e("Kanishk", "HTTP status code: ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("KanishkErr", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                // Request failed due to network error or other issues
                Log.d("Kanishk", t.toString())
            }
        })


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
//                        // Process the list of recommended destinations
//                        for (destination in recommendedDestinations) {
//
//
//                            Log.d("Kanishk1", "Place Name: ${destination.placeName}, Rating: ${destination.Rating}")
//                            // Add your logic to handle each recommended destination
//                        }
//                    } else {
//                        Log.d("Kanishk1", "No recommended destinations found")
//                    }
//                } else {
//                    // Request failed
//                    Log.e("Kanishk", "HTTP status code: ${response.code()}")
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("KanishkErr", "Error body: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
//                // Request failed due to network error or other issues
//                Log.d("Kanishk", t.toString())
//            }
//        })


//        val call = recommendationApi.getRecommendations(request)
//
//        call.enqueue(object : Callback<RecommendationResponse> {
//            override fun onResponse(
//                call: Call<RecommendationResponse>,
//                response: Response<RecommendationResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val recommendations = response.body()?.recommendedDestinations
//                    // Process the recommendations as needed
//                    Log.d("Kanishk",recommendations.toString())
//                } else {
//                    // Handle unsuccessful response
//                    Log.e("Kanishk", "HTTP status code: ${response.code()}")
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("KanishkErr", "Error body: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
//                // Handle network failure
//                Log.d("Kanishk",t.toString())
//            }
//        })


    }




    }





//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import com.example.tripit.ml.TfliteModel
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.nio.ByteBuffer
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//
//
//
//
//
//
//
//
//    //    var byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(4*4)
//
//
////        val stringValue = "Religious Sites"
////
////        val model = TfliteModel.newInstance(this)
////
////        val stringBytes = stringValue.toByteArray(Charsets.UTF_8)
////        val byteBuffer = ByteBuffer.allocateDirect(4 * (1 + 1 + stringBytes.size + 1 + 1)).apply {
////            // Put the string bytes
////            put(stringBytes)
////            // Put the float values
////            putFloat(31.1582f)
////            putFloat(77.2503f)
////            putInt(1)
////            putFloat(3.0f)
////
////
////        }
////
////// Creates inputs for reference.
////        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 6), DataType.FLOAT32)
////        inputFeature0.loadBuffer(byteBuffer)
////
////// Runs model inference and gets result.
////        val outputs = model.process(inputFeature0)
////        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
////
////        Log.d("Kanishk", outputFeature0.toString())
////
////// Releases model resources if no longer used.
////        model.close()
//
//
//
//    }
//}