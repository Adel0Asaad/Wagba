package com.example.wagba.data.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wagba.data.database.UserDatabase
import com.example.wagba.data.internal.User
import com.example.wagba.data.internal.UserEntity
import com.example.wagba.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<UserEntity>>
    private val repo: UserRepo

    private val _currUser = MutableLiveData<User>()
    val currUser: LiveData<User> = _currUser

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repo = UserRepo(userDao)
        readAllData = repo.readAllData
    }

    fun printData(){
        repo.printData()
    }

    fun getCurrUser(){
        repo.getCurrentUser(_currUser)
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addUser(UserEntity(user))
        }
    }

    fun clearUsers(){
        viewModelScope.launch(Dispatchers.IO){
            repo.clearUsers()
        }
    }

    fun signup(name: String, pass: String, email: String, phone: String){
        repo.signup(name, pass, email, phone)

    }

}