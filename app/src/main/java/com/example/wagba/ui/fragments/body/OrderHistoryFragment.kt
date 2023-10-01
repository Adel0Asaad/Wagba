package com.example.wagba.ui.fragments.body

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.adapters.OrderHistoryAdapter
import com.example.wagba.data.models.OrderItemViewModel
import com.example.wagba.data.models.factories.OrderItemViewModelFactory
import com.example.wagba.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrderItemViewModel
    private lateinit var orderRecyclerView: RecyclerView
    lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillOrderList()
    }

    private fun fillOrderList(){
        orderRecyclerView = binding.recyclerOrderHistory
        orderRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        orderHistoryAdapter = OrderHistoryAdapter()
        orderRecyclerView.adapter = orderHistoryAdapter

        val viewModelFactory = OrderItemViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[OrderItemViewModel::class.java]
        viewModel.orderHistory.observe(viewLifecycleOwner, Observer{
            orderHistoryAdapter.updateOrderList(it)
        })
    }
}