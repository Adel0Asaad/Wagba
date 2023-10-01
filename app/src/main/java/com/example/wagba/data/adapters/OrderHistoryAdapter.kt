package com.example.wagba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.internal.Order
import com.squareup.picasso.Picasso

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    private val orderList = ArrayList<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_order_history,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        holder.orderIDTextView.text = "Order Number ${position + 1}"
        holder.orderPriceTextView.text = order.price.toString() + " EGP"
        Picasso.get().load(order.rest!!.image).into(holder.restImageView)
        holder.restNameView.text = order.rest.name
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun updateOrderList(orderList: List<Order>){
        this.orderList.clear()
        this.orderList.addAll(orderList)
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val orderIDTextView: TextView = itemView.findViewById(R.id.order_history_id)
        val orderPriceTextView: TextView = itemView.findViewById(R.id.order_history_price)
        val restImageView: ImageView = itemView.findViewById(R.id.order_history_rest_img)
        val restNameView: TextView = itemView.findViewById(R.id.order_history_rest_name)
    }
}