package com.example.tripit.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.ItineraryAdapter
import com.example.tripit.LocationUtils
import com.example.tripit.R
import com.example.tripit.databinding.FragmentItineraryBinding
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.IconFactory
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.annotations.PolygonOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.location.LocationComponentActivationOptions
import com.mappls.sdk.services.account.MapplsAccountManager

class ItineraryFragment : Fragment(),OnMapReadyCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentItineraryBinding
    private lateinit var mapView: MapView
    var mapplsMap: MapplsMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        geofence = Geofenceclass(requireContext())

        MapplsAccountManager.getInstance().setRestAPIKey("ced1b2f5797366ff5dd8f36048e116ad")
        MapplsAccountManager.getInstance().setMapSDKKey("ced1b2f5797366ff5dd8f36048e116ad")
        MapplsAccountManager.getInstance().setAtlasClientId("33OkryzDZsLALlzrrV7urjMNezZlPPX_m8hVIf7BjWGnPNo80opSLaBTTYyR15dKXApf7wco0fARYlrJ6NjrnA==")
        MapplsAccountManager.getInstance().setAtlasClientSecret("lrFxI-iSEg_QxP7kjBWw2z9uUkCoSub3N3R6sTkRba2YMxolnTZxT_JC2OHd0_Yt1-D7wufEu2EHdxnAD8_MWAtS9DHVcBKZ")
        Mappls.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItineraryBinding.inflate(layoutInflater, container, false)


//        recyclerView = rootView.findViewById(R.id.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ItineraryAdapter(getSampleData()) // Replace with your data source
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun getSampleData(): List<String> {
        // Replace this with your actual data retrieval logic
        return listOf("Item 1", "Item 2", "Item 3", "Item 4")
    }

    override fun onMapReady(p0: MapplsMap) {
        currentLocation(p0)
        this.mapplsMap = mapplsMap

        val currentLocation = LocationUtils.getCurrentLatLng(requireContext())
        mapplsMap?.getStyle {
            mapplsMap?.locationComponent?.activateLocationComponent(
                LocationComponentActivationOptions.builder(requireContext(), it).build()
            )
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return null
            }
            mapplsMap!!.locationComponent.isLocationComponentEnabled = true
        }

        if (currentLocation != null) {
            val latitude = currentLocation.latitude
            val longitude = currentLocation.longitude

//            binding.AddGeofence.setOnClickListener {
//                Geofence.startGeofencing(latitude, longitude, 100)
//            }

            val position = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(14.0)
                .tilt(45.0)
                .build()

            mapplsMap?.setCameraPosition(position)

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.location)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 45, 45, false)
            val scaledIcon = IconFactory.getInstance(requireContext()).fromBitmap(scaledBitmap)

            val markerOptions = MarkerOptions()
                .position(currentLocation)
                .icon(scaledIcon)
                .title("Your Location")

            val marker = mapplsMap?.addMarker(markerOptions)

            mapplsMap?.addPolygon(
                PolygonOptions()
                    .add(currentLocation)
                    .fillColor(Color.parseColor("#3bb2d0"))
            )
        }
    }

    override fun onMapError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }
    override fun onStart() {
        super.onStart()
        binding.MapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.MapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.MapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        binding.MapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.MapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.MapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.MapView.onSaveInstanceState(outState)
    }

    private fun currentLocation(mapplsMap: MapplsMap) {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                val currentLatLng = LatLng(latitude, longitude)

                if (mapplsMap != null) {
                    mapplsMap.setCameraPosition(
                        CameraPosition.Builder()
                            .target(currentLatLng)
                            .zoom(14.0)
                            .build()
                    )
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            10f,
            locationListener
        )
    }

}
