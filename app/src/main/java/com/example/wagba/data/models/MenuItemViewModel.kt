package com.example.wagba.data.models

import androidx.lifecycle.*
import com.example.wagba.data.internal.MenuItem
import com.example.wagba.data.repo.MenuItemRepo

class MenuItemViewModel(mKey: Int? = null): ViewModel() {

    private val repo: MenuItemRepo
    private val _allMenuItems = MutableLiveData<List<MenuItem>>()
    val allMenuItems : LiveData<List<MenuItem>> = _allMenuItems
    var restKey = mKey!!

    init {
        repo = MenuItemRepo().getInstance()
        repo.setRef(restKey)
        repo.loadMenuItem(_allMenuItems)
    }
}