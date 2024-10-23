package com.example.tripit.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripit.R
import com.example.tripit.adapters.AddPlaceImageAdapter
import com.example.tripit.databinding.FragmentAddPlaceBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AddPlaceFragment : Fragment() {

    private lateinit var binding: FragmentAddPlaceBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var imageAdapter: AddPlaceImageAdapter
    private val imageUris = mutableListOf<Uri>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPlaceBinding.inflate(layoutInflater, container, false)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupSpinner(binding.addPlaceDistrictSpinner, resources.getStringArray(R.array.Himachal_Districts))
//        setupChipGroup(binding.addPlaceCategorySpinner, resources.getStringArray(R.array.Interests))
        setupSpinner(binding.addPlaceDurationSpinner, resources.getStringArray(R.array.Duration))
        setupSpinner(binding.addPlaceBudgetSpinner, resources.getStringArray(R.array.Budget))
        setupChipGroup(binding.addPlaceTravelStyleSpinner, resources.getStringArray(R.array.Travel_Style))
        setupSpinner(binding.addPlaceGroupSizeSpinner, resources.getStringArray(R.array.Group_Size))
        setupChipGroup(binding.addPlaceTimeVisitSpinner, resources.getStringArray(R.array.Time_Visit))
        setupSpinner(binding.addPlaceTransportSpinner, resources.getStringArray(R.array.Travel_Options))

        setupAddPlaceImageRecyclerView()

        binding.addPlaceReviewBtn.setOnClickListener {
            reviewPlaceData()
        }

    }

    private fun setupAddPlaceImageRecyclerView() {
    }

    private fun setupSpinner(spinner: Spinner,dataArray: Array<String>){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
    private fun setupChipGroup(chipGroup: ChipGroup, items: Array<String>) {
        chipGroup.removeAllViews()
        items.forEach { item ->
            val chip = Chip(context).apply {
                text = item
                isCheckable = true
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupImageRecyclerView() {
        imageAdapter = AddPlaceImageAdapter(imageUris)
        binding.addPlaceImgRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.addPlaceImgRv.adapter = imageAdapter

        // Handle image selection
        binding.addPlaceImg.setOnClickListener {
            // Trigger image picker (not included)
            // After picking, add URIs to imageUris and notify adapter
        }
    }


    private fun getSelectedChips(chipGroup: ChipGroup): List<String> {
        val selectedChips = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChips.add(chip.text.toString())
            }
        }
        return selectedChips
    }

    private fun reviewPlaceData() {
        val placeName = binding.addPlaceName.text.toString()
        val district = binding.addPlaceDistrictSpinner.selectedItem.toString()
        val groupSize = binding.addPlaceGroupSizeSpinner.selectedItem.toString()
        val description = binding.addPlaceDescription.text.toString()
        val travelStyles = getSelectedChips(binding.addPlaceTravelStyleSpinner)
        val timeVisit = getSelectedChips(binding.addPlaceTimeVisitSpinner)
        val budget = binding.addPlaceBudgetSpinner.selectedItem.toString()
        val duration = binding.addPlaceDurationSpinner.selectedItem.toString()
        val transportOptions = binding.addPlaceTransportSpinner.selectedItem.toString()

        // Save the place data
        val placeData = hashMapOf(
            "placeName" to placeName,
            "district" to district,
            "groupSize" to groupSize,
            "description" to description,
            "travelStyles" to travelStyles,
            "timeVisit" to timeVisit,
            "budget" to budget,
            "duration" to duration,
            "transportOptions" to transportOptions
        )

        firestore.collection("places").add(placeData).addOnSuccessListener { documentReference ->
            uploadImages(placeName)
        }.addOnFailureListener {
            // Handle error
        }
    }

    private fun uploadImages(placeId: String) {
        imageUris.forEachIndexed { index, uri ->
            val fileName = "$placeId/$index.jpg"
            val storageRef = storage.reference.child("places/$fileName")
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    firestore.collection("places").document(placeId)
                        .update("images", FieldValue.arrayUnion(downloadUrl.toString()))
                }
            }.addOnFailureListener {
                // Handle error
            }
        }
    }


}