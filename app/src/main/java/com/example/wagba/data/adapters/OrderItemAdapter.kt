package com.example.wagba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.internal.OrderItem


class OrderItemAdapter: RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    private val orderItemList = ArrayList<OrderItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mOrder = orderItemList[position]

        val countString = "x" + mOrder.count.toString()
        holder.countTextView.text = countString
        holder.nameTextView.text = mOrder.mItem!!.name
        val priceString = (mOrder.mItem.price!! * mOrder.count!!).toString() + " EGP"
        holder.priceTextView.text = priceString


    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    fun updateOrderItemList(orderItemList: List<OrderItem>){
        this.orderItemList.clear()
        this.orderItemList.addAll(orderItemList)
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val countTextView: TextView = itemView.findViewById(R.id.list_item_order_item_quantity)
        val nameTextView: TextView = itemView.findViewById(R.id.list_item_order_item_name)
        val priceTextView: TextView = itemView.findViewById(R.id.list_item_order_item_price)
    }
}