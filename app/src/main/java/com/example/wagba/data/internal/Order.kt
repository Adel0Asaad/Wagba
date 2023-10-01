package com.example.wagba.data.internal

import com.google.firebase.database.PropertyName

data class Order (
    @PropertyName("orderItemList")
    val orderItemList: ArrayList<OrderItem>? = null,
    val rest: RestaurantLite? = null,
    val price: Float? = null,
    @PropertyName("itemCount")
    val itemCount: Int? = null,
//    val date: Date? = null
)