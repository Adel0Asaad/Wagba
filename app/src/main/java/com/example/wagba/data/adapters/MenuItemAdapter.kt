package com.example.wagba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.internal.MenuItem
import com.squareup.picasso.Picasso

class MenuItemAdapter (private var myListener: MenuItemClickListener): RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    private val menuItemList = ArrayList<MenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_menu_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mItem = menuItemList[position]

        holder.titleTextView.text = mItem.name
        holder.descTextView.text = mItem.desc


        val priceString = mItem.price.toString() + " EGP"
        holder.priceTextView.text = priceString
        Picasso.get().load(mItem.img).into(holder.imageView)

        holder.addBtnView.setOnClickListener {
            myListener.onAddClick(holder, mItem)
        }

    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    fun updateMenuItemList(menuItemList: List<MenuItem>){
        this.menuItemList.clear()
        this.menuItemList.addAll(menuItemList)
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val addBtnView: Button = itemView.findViewById(R.id.menu_item_add_btn)
        val titleTextView: TextView = itemView.findViewById(R.id.menu_item_title_text)
        val descTextView: TextView = itemView.findViewById(R.id.menu_item_desc_text)
        val priceTextView: TextView = itemView.findViewById(R.id.menu_item_price_text)
        val imageView: ImageView = itemView.findViewById(R.id.menu_item_img)
    }

    interface MenuItemClickListener{
        fun onAddClick(mHolder: ViewHolder, mItem: MenuItem)
    }

}