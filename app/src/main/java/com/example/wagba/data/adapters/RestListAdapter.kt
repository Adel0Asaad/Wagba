package com.example.wagba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.internal.Restaurant
import com.example.wagba.ui.fragments.body.RestFragment2
import com.squareup.picasso.Picasso

class RestListAdapter : RecyclerView.Adapter<RestListAdapter.ViewHolder>() {

    private val restList = ArrayList<Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_rest,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val restaurant = restList[position]
        Picasso.get().load(restaurant.image).into(holder.imageView)
        holder.textView.text = restaurant.name

        holder.itemView.setOnClickListener {
            switchFrags(restaurant, holder, position)
        }
    }

    override fun getItemCount(): Int {
        return restList.size
    }

    fun updateRestList(restList: List<Restaurant>){
        this.restList.clear()
        this.restList.addAll(restList)
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rest_list_img)
        val textView: TextView = itemView.findViewById(R.id.rest_list_name)

    }

    private fun switchFrags(rest: Restaurant, holder: ViewHolder, mKey: Int){
        val activity: AppCompatActivity = holder.itemView.context as AppCompatActivity
        activity.supportFragmentManager.commit {
            val mFrag = RestFragment2.newInstance(
                rest.name!!,
                mKey,
                rest.image!!
            )
            replace(R.id.fragC2, mFrag, "rf_OG")
            addToBackStack("rl_OG")
        }
    }
}