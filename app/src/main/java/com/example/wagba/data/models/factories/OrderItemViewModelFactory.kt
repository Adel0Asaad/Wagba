package com.example.wagba.data.models.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.data.models.OrderItemViewModel

class OrderItemViewModelFactory(private var mKey: Int? = null): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(OrderItemViewModel::class.java)){
            return OrderItemViewModel(mKey) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}