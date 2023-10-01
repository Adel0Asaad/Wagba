package com.example.wagba.data.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wagba.data.internal.Restaurant
import com.example.wagba.data.repo.RestaurantRepo

class RestaurantViewModel: ViewModel() {

    private val repo: RestaurantRepo
    private val _allRests = MutableLiveData<List<Restaurant>>()
    val allRests : LiveData<List<Restaurant>> = _allRests

    init {
        Log.d("RVM", "Entered Restaurant ViewModel")
        repo = RestaurantRepo().getInstance()
        repo.loadRest(_allRests)
    }

}