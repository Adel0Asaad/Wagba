package com.example.wagba.data.internal

import com.google.firebase.database.PropertyName

data class OrderItem (
    @PropertyName("mitem")
    val mItem: MenuItem? = null,

    val count: Int? = null)