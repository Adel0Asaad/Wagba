package com.example.wagba.ui.fragments.body

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.R
import com.example.wagba.data.models.OrderItemViewModel
import com.example.wagba.data.models.factories.OrderItemViewModelFactory
import com.example.wagba.databinding.FragmentTrackBinding

class TrackFragment : Fragment(R.layout.fragment_track){

    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderViewModel: OrderItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderViewModelFactory = OrderItemViewModelFactory()
        orderViewModel = ViewModelProvider(this, orderViewModelFactory)[OrderItemViewModel::class.java]
        orderViewModel.currStatus.observe(viewLifecycleOwner, Observer{
            Log.d("TrackOrder", it)
            if(it != null){
                binding.trackOrderStatusText.text = it
            }else{
                binding.trackOrderStatusText.text = "Delivered"
            }
        })
        orderViewModel.loadOrderStatus()

        binding.trackOrderBtn.setOnClickListener {
            cancelOrder()
        }
    }

    private fun cancelOrder(){
        orderViewModel.cancelOrder()
    }

}
