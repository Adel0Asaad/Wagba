package com.example.wagba.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.wagba.data.internal.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuItemRepo {

    private lateinit var myRef: DatabaseReference

    fun setRef(mKey: Int){
        myRef = FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Restaurants/$mKey/Dishes")
    }

    @Volatile private var _INSTANCE: MenuItemRepo? = null

    fun getInstance(): MenuItemRepo{
        return _INSTANCE ?: synchronized(this){
            val instance = MenuItemRepo()
            _INSTANCE = instance
            instance
        }
    }

    fun loadMenuItem(menuItemList: MutableLiveData<List<MenuItem>>){

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    val _menuItemList : List<MenuItem> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(MenuItem::class.java)!!
                    }
                    menuItemList.postValue(_menuItemList)
                }catch (e: java.lang.Exception){Log.e("MenuItem_Map", e.toString())}
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

}