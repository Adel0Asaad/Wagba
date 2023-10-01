package com.example.wagba.ui.fragments.body

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.adapters.OrderItemAdapter
import com.example.wagba.data.models.OrderItemViewModel
import com.example.wagba.data.models.factories.OrderItemViewModelFactory
import com.example.wagba.databinding.FragmentCartBinding
import com.squareup.picasso.Picasso
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrderItemViewModel
    private lateinit var orderItemRecyclerView: RecyclerView
    lateinit var orderItemAdapter: OrderItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = OrderItemViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[OrderItemViewModel::class.java]

        binding.cartBtnClose.setOnClickListener{
            closeCart()
        }

        fillOrderDetail()
        binding.cartBtnCheckout.setOnClickListener {
            checkout()
        }
    }

    
    private fun checkout(){
        viewModel.tempOrder.observe(viewLifecycleOwner, Observer {
            if(it == null){ // if no other order
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                if((hour < 10) || (hour == 10 && minute <= 30)){
                    Toast.makeText(context, "This will be delivered by 12:00 PM", Toast.LENGTH_SHORT).show()
                }else if((hour < 13) || (hour == 13 && minute <= 30)){
                    Toast.makeText(context, "This will be delivered by 3:00 PM", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "This will be delivered by tomorrow", Toast.LENGTH_SHORT).show()
                }
                viewModel.checkout()
                activity?.findViewById<View>(R.id.frame_show_cart)?.visibility = View.GONE
                closeCart()
            }else{ // if there's order
                Toast.makeText(context, "You already ordered something, please cancel your order first", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.checkTemp()
    }

    private fun fillOrderDetail(){
        orderItemRecyclerView = binding.orderDetailFrame.findViewById(R.id.order_detail_recycler)
        var subTotal: Float
        var total: Float
        val subtotalTextVew = binding.orderPriceFrame.findViewById<TextView>(R.id.order_price_subtotal_value)
        val dFeeTextView = binding.orderPriceFrame.findViewById<TextView>(R.id.order_price_dfee_value)
        val totalTextView = binding.orderPriceFrame.findViewById<TextView>(R.id.order_price_total_value)
        val restImg = binding.orderTitleFrame.findViewById<ImageView>(R.id.order_title_rest_img)
        val restName = binding.orderTitleFrame.findViewById<TextView>(R.id.order_title_rest_name)

        orderItemRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        orderItemAdapter = OrderItemAdapter()
        orderItemRecyclerView.adapter = orderItemAdapter

        viewModel.currOrder.observe(viewLifecycleOwner, Observer{

            if (it?.rest != null){
                Log.d("Printing it", it.toString())
                Picasso.get().load(it.rest!!.image).into(restImg)
                restName.text = it.rest.name

                orderItemAdapter.updateOrderItemList(it.orderItemList!!)
                subTotal = 0f
                for(mOrder in it.orderItemList){
                    subTotal += mOrder.mItem!!.price as Long * mOrder.count!!
                }
                subtotalTextVew.text = (subTotal).toString() + " EGP"
                total = dFeeTextView.text.subSequence(0, dFeeTextView.length()-4).toString().toFloat() + subTotal
                totalTextView.text = (total).toString() + " EGP"
            }
        })

    }

    private fun closeCart(){
        activity?.findViewById<View>(R.id.frag_c_cart)?.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int, param3: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}