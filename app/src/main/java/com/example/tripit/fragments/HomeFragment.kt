package com.example.tripit.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tripit.District
import com.example.tripit.Global
import com.example.tripit.adapters.DistrictAdapter
import com.example.tripit.adapters.ImageAdapter
import com.example.tripit.R
import com.example.tripit.adapters.CategoryAdapter
import com.example.tripit.databinding.FragmentHomeBinding
import com.example.tripit.viewmodels.onCategoryClicked
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var global: Global
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var adapter : DistrictAdapter
    private lateinit var sharedPreferences: SharedPreferences
    val districtNames = listOf(
        "Bilaspur", "Chamba", "Hamirpur", "Kangra", "Kinnaur",
        "Kullu", "Lahaul", "Mandi", "Shimla", "Sirmaur", "Solan", "Una"
    )
    val districtDataList = listOf(
        District(R.drawable.bilaspurmap, "Bilaspur"),
        District(R.drawable.chambamap, "Chamba"),
        District(R.drawable.hamirpurmap, "Hamirpur"),
        District(R.drawable.kangramap, "Kangra"),
        District(R.drawable.kinnaurmap, "Kinnaur"),
        District(R.drawable.kullumap, "Kullu"),
        District(R.drawable.lahaulspitimap, "Lahaul & Spiti"),
        District(R.drawable.mandimap, "Mandi"),
        District(R.drawable.shimlamap, "Shimla"),
        District(R.drawable.sirmaurmap, "Sirmaur"),
        District(R.drawable.solanmap, "Solan"),
        District(R.drawable.unamap, "Una")
    )
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        global = Global.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("DistrictPref", Context.MODE_PRIVATE)

        Log.d("Name",global.name.toString())

        if (global.name==null){
            _binding.progressBar.visibility = View.VISIBLE
            checkProfileImageUrlInDatabase()
        }else{
            binding.UserName.text = global.name
            Picasso.get().load(global.imageUrl).into(_binding.profileImage)
        }


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.DistrictRecylerview.layoutManager = layoutManager


       val adapter = DistrictAdapter(districtDataList)

        adapter.setOnItemClickListener(object : DistrictAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                loadFragment(DistrictFragment())

                val editor = sharedPreferences.edit()
                editor.putString("district", districtDataList[position].purpose)
                editor.apply()
            }
        })
        binding.DistrictRecylerview.adapter = adapter

        setupSearchEditText(adapter)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<Int>() // Populate this list with your image resources
        imageList.add(R.drawable.hp1)
        imageList.add(R.drawable.hp2)
        imageList.add(R.drawable.hp3)
        imageList.add(R.drawable.hp4)
        imageList.add(R.drawable.hp5)
        imageList.add(R.drawable.hp6)

        val imageName = ArrayList<String>()
        imageName.add("Dhankar Lake")
        imageName.add("Pangong Lake (Pangong Tso)")
        imageName.add("Kasol")
        imageName.add("Kasauli")
        imageName.add("Hanogi Bridge")

        val imageLocation = ArrayList<String>()
        imageLocation.add("Lahaul & Spiti, Himachal Pradesh")
        imageLocation.add("Leh, Ladakh")
        imageLocation.add("Kullu, Himachal Pradesh")
        imageLocation.add("Kasauli, Himachal Pradesh")
        imageLocation.add("Mandi, Himachal Pradesh")

        val adapter = ImageAdapter(imageList,imageName,imageLocation)
        viewPager2 = binding.defaultViewpager
        viewPager2.adapter = adapter


        //category adapter

        // Sample data
        val catList = listOf(
            R.drawable.shangarh, R.drawable.bir, R.drawable.padampalace, /* add more images */
            R.drawable.pinvalley, R.drawable.baijnath, R.drawable.keymonastry, /* add more images */
            // Ensure you have 16 images or adjust the size accordingly
        )
        val catName = listOf(
            "Nature ", "Adventure", "Historical", /* add more names */
            "Wildlife", "Religious", "Scenic Views", /* add more names */
            // Ensure you have names corresponding to the images
        )

        val categoryAdapter = CategoryAdapter(catList, catName,object : onCategoryClicked{
            override fun onCategoryClicked(category: String) {

            }

        })
        binding.categoryRv.adapter = categoryAdapter

        // Set up GridLayoutManager with 2 columns
        binding.categoryRv.layoutManager = GridLayoutManager(requireContext(), 2)





        // Set the number of items to be kept in memory on each side of the current page
        viewPager2.offscreenPageLimit = 2 // Adjust as needed

        // Set page margin to create the carousel effect
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        viewPager2.setPageTransformer { page, position ->
            val offset = position * -pageMargin
            page.translationX = offset

            // Optionally, you can scale the pages based on their position
            val scaleFactor = 0.8f
            val scale = if (position < -1 || position > 1) {
                scaleFactor
            } else {
                (1 - abs(position)) * (1 - scaleFactor) + scaleFactor
            }
            page.scaleX = scale
            page.scaleY = scale
        }

        // Optionally, set the initial item to make the second item visible
        viewPager2.setCurrentItem(2, true)
    }
    private fun checkProfileImageUrlInDatabase() {

        firebaseAuth = FirebaseAuth.getInstance()

        val useruid = firebaseAuth.currentUser?.uid.toString()

        // Check if the profile image URL exists in the database
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        databaseReference.child(useruid).get().addOnSuccessListener {datasnapshot ->
            val value = datasnapshot.value.toString()
            val imageurl = datasnapshot.child("profileImageUrl").value.toString()
            val Name = datasnapshot.child("name").value.toString()
            binding.UserName.text = Name
            global.name=Name
            global.imageUrl=imageurl
            Picasso.get().load(imageurl).into(_binding.profileImage)
            _binding.progressBar.visibility = View.GONE
            Log.d("kanishk",value)

        }

//        databaseReference.child(useruid).child("profileImageUrl")
//
//
//
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val imageUrl = dataSnapshot.value.toString()
//                    // If a profile image URL is available in the database, load the image
//                    Picasso.get().load(imageUrl).into(_binding.profileImage)
//                    _binding.progressBar.visibility = View.GONE
//
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Handle errors here
//                    _binding.progressBar.visibility = View.GONE
//
//                }
//            })
    }


    private fun setupSearchEditText(adapter: DistrictAdapter) {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                val searchEmployeeId = s.toString()
                val searchEmpName = s.toString().capitalize().trim()

                if (searchEmpName.isBlank()) {
                    // If the search text is empty, restore the original data in the RecyclerView
                    Log.d("Search",districtDataList.toString())
                    adapter.submitList(districtDataList)
                } else {

                    binding.srchbtn.setOnClickListener {
                        // Filter the employee data based on the entered employee ID
                        val filteredList = districtDataList.filter { it.purpose == searchEmpName }
                        if (filteredList.isNotEmpty()) {
                            Log.d("Search",filteredList.toString())
                            adapter.submitList(filteredList)
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(requireContext(), "No employee found with ID $searchEmpName", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        })
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
        transaction.addToBackStack(null)
    }
}