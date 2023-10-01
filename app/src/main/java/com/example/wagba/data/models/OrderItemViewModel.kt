package com.example.wagba.data.models

import android.content.Context
import androidx.lifecycle.*
import com.example.wagba.data.internal.MenuItem
import com.example.wagba.data.internal.Order
import com.example.wagba.data.internal.OrderItem
import com.example.wagba.data.internal.RestaurantLite
import com.example.wagba.data.repo.OrderRepo

class OrderItemViewModel(mKey: Int? = null): ViewModel() {

    private val orderRepo: OrderRepo
    private val _currOrderItems = MutableLiveData<List<OrderItem>>()
    private val _currOrder = MutableLiveData<Order>()
    val currOrder: LiveData<Order> = _currOrder
    private val _currStatus = MutableLiveData<String>()
    val currStatus: LiveData<String> = _currStatus
    private val _tempOrder = MutableLiveData<Order>()
    val tempOrder: LiveData<Order> = _tempOrder
    private val _orderHistory = MutableLiveData<List<Order>>()
    val orderHistory: LiveData<List<Order>> = _orderHistory

    init {
        orderRepo = OrderRepo().getInstance()
        orderRepo.loadCurrOrder(_currOrder)
        orderRepo.loadOrderHistory(_orderHistory)
    }

    fun loadOrderStatus(){
        orderRepo.loadOrderStatus(_currStatus)
    }

    fun checkout(){
        orderRepo.checkout()
    }

    fun checkTemp(){
        orderRepo.checkTemp(_tempOrder)
    }

    fun cancelOrder(){
        orderRepo.cancelOrder()
    }

    fun postOrderItem(orderItem: MenuItem, rest: RestaurantLite, context: Context){
        orderRepo.postOrderItemCurr(orderItem, rest, context)
    }
}