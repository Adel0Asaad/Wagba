package com.example.wagba.data.models.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.data.models.MenuItemViewModel

class MenuItemViewModelFactory(private var mKey: Int): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(MenuItemViewModel::class.java)){
            return MenuItemViewModel(mKey) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}