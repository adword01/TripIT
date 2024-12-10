package com.example.tripit.viewmodels

import android.gesture.Prediction
import com.example.tripit.Preditction
import com.example.tripit.RecommendedDestination

interface OnPredictionClickListener {
    fun onPredictionClick(prediction: RecommendedDestination)
}
