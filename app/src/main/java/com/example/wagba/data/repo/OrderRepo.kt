package com.example.wagba.data.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.wagba.data.internal.MenuItem
import com.example.wagba.data.internal.Order
import com.example.wagba.data.internal.OrderItem
import com.example.wagba.data.internal.RestaurantLite
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class OrderRepo {

    private val fAuth = Firebase.auth
    private val currRef =
        FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users/${fAuth.currentUser!!.uid}/Orders/Current Order")
    private val tempRef =
        FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users/${fAuth.currentUser!!.uid}/Orders/Temp")
    private val oldRef =
        FirebaseDatabase.getInstance("https://wagba-33dd3-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users/${fAuth.currentUser!!.uid}/Orders/Old")

    @Volatile
    private var _INSTANCE: OrderRepo? = null

    fun getInstance(): OrderRepo {
        return _INSTANCE ?: synchronized(this) {
            val instance = OrderRepo()
            _INSTANCE = instance
            instance
        }
    }

    fun loadOrderHistory(orderList: MutableLiveData<List<Order>>){
        oldRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val _orderList: List<Order> =
                    if (snapshot.hasChildren()){
                        snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(Order::class.java)!!
                        }
                    }else{
                        emptyList()
                    }
                orderList.postValue(_orderList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun checkTemp(order: MutableLiveData<Order>){
        tempRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val _tempOrder: Order? =
                    snapshot.child("Order").getValue(Order::class.java)

                order.postValue(_tempOrder)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("OrderRepoErr/", error.toString())
            }

        })
    }

    fun loadCurrOrder(order: MutableLiveData<Order>) {
        currRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val _currOrderItemList: List<OrderItem> =
                    snapshot.child("OrderItemList").children.map { dataSnapshot ->
                        dataSnapshot.getValue(OrderItem::class.java)!!
                    }
                val rest: RestaurantLite? =
                    snapshot.child("rest").getValue(RestaurantLite::class.java)

                val currOrderItemList = ArrayList<OrderItem>()
                currOrderItemList.addAll(_currOrderItemList)
                val myOrder = Order(currOrderItemList, rest)
                order.postValue(myOrder)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("OrderRepoErr/", error.toString())
            }
        })
    }

    fun loadOrderStatus(orderStatus: MutableLiveData<String>) {
        tempRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _orderStatus: String = if(snapshot.child("Status").value != null){
                    snapshot.child("Status").value as String
                }else{
                    "Delivered"
                }
                orderStatus.postValue(_orderStatus)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("OrderRepoErr/", error.toString())
            }
        })
    }

    fun checkout() {
        currRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var myPrice = 10f
                var myItemCount = 0
                val _currOrderItemList: List<OrderItem> =
                    snapshot.child("OrderItemList").children.map { dataSnapshot ->
                        dataSnapshot.getValue(OrderItem::class.java)!!
                    }
                for (orderItem in _currOrderItemList) {
                    myPrice += ((orderItem.mItem?.price!!) * orderItem.count!!)
                    myItemCount += orderItem.count
                }
                val rest: RestaurantLite? =
                    snapshot.child("rest").getValue(RestaurantLite::class.java)
                val currOrderItemList = ArrayList<OrderItem>()
                currOrderItemList.addAll(_currOrderItemList)
                tempRef.child("Order")
                    .setValue(Order(currOrderItemList, rest, myPrice, myItemCount))
                tempRef.child("Status").setValue("Pending")// HAS: Pending, Accepted, Delivered
                currRef.removeValue()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("CheckoutErr/", error.toString())
            }
        })
    }

    fun cancelOrder() {
        tempRef.removeValue()
    }

    fun postOrderItemCurr(menuItem: MenuItem, rest: RestaurantLite, context: Context) {
        currRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var exists: Boolean = false
                var count = 0

                if (snapshot.child("rest/id").value != null && (snapshot.child("rest/id").value as Long).toInt() != rest.id) {
                    Toast.makeText(
                        context,
                        "You're trying to make an order from multiple restaurants",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                for (myOrderItem in snapshot.child("OrderItemList").children) {
                    if (myOrderItem.child("mitem/name").value == menuItem.name) {
                        exists = true
                        count = ((myOrderItem.child("count").value as Long).toInt() + 1)
                        currRef.child("OrderItemList/" + myOrderItem.key!! + "/count")
                            .setValue(count)
                    }
                }
                if (!exists) {
                    currRef.child("OrderItemList").push().setValue(OrderItem(menuItem, 1))
                    currRef.child("rest").setValue(rest)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("OrderRepoErr/", error.toString())
            }
        })
    }
}