package com.example.wagba.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wagba.data.dao.UserDao
import com.example.wagba.data.internal.User
import com.example.wagba.data.internal.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class UserRepo(private val userDao: UserDao) {

    // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA //

    private val myRef = FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
    private val fAuth: FirebaseAuth = Firebase.auth

    // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA // // ONLINE FIREBASE DATA //



    //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  //

    val readAllData: LiveData<List<UserEntity>> = userDao.readAllData()

    //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  // //  ROOM DATABASE DATA  //


    // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS //

    fun getCurrentUser(currUser: MutableLiveData<User>) {

        if (fAuth.currentUser != null){
            val firebaseCurrUser = fAuth.currentUser!!
            myRef.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val _currUser = snapshot.child(firebaseCurrUser.uid).getValue(User::class.java)
                    currUser.postValue(_currUser)
                    Log.d("RepoCrrUser", _currUser.toString())
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

    }


    fun signup(name: String, pass: String, email: String, phone: String){
        var currUser: User
        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("AuthSuccess", "The it: $it, The user: ${fAuth.currentUser}")

                currUser = User(
                    fAuth.currentUser!!.uid,
                    name,
                    pass,
                    email,
                    phone
                )
                myRef.child(fAuth.currentUser!!.uid).setValue(currUser)

                Log.d("AuthSuccess", "LocalUser: $currUser")
            }else if (it.isCanceled){
                Log.d("AuthFailure", "The it: $it, The user: ${fAuth.currentUser}")
            }
        }
    }

    // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS // // ONLINE FIREBASE FUNCTIONS //




    //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  //

    fun printData(){
        Log.d("Repo Prtining", readAllData.toString())
    }

    suspend fun addUser(user: UserEntity){
        val myResult = userDao.addUser(user)
        Log.d("Adding: ", myResult.toString())
    }

    suspend fun clearUsers(){
        userDao.clearUsers()
    }

    //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  // //  ROOM DATABASE FUNCTIONS  //


}