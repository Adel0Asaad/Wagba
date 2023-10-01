package com.example.wagba.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.wagba.data.internal.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantRepo {

    private val myRef = FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Restaurants")

    @Volatile private var _INSTANCE: RestaurantRepo? = null

    fun getInstance(): RestaurantRepo{
        return _INSTANCE ?: synchronized(this){
            val instance = RestaurantRepo()
            _INSTANCE = instance
            instance
        }
    }

    fun loadRest(restList: MutableLiveData<List<Restaurant>>){

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    val _restList : List<Restaurant> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Restaurant::class.java)!!
                    }
                    restList.postValue(_restList)
                }catch (e: java.lang.Exception){Log.e("Rest_Map", e.toString())}
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }
}