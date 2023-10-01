package com.example.wagba.ui.fragments.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.databinding.FragmentRestListBinding
import com.example.wagba.data.adapters.RestListAdapter
import com.example.wagba.data.models.RestaurantViewModel

class RestListFragment : Fragment(R.layout.fragment_rest_list) {

    private var _binding: FragmentRestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RestaurantViewModel
    private lateinit var restRecyclerView: RecyclerView
    lateinit var restListAdapter: RestListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillRestItems()
    }

    private fun fillRestItems(){
        restRecyclerView = binding.recyclerRest
        restRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        restListAdapter = RestListAdapter()
        restRecyclerView.adapter = restListAdapter

        viewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        viewModel.allRests.observe(viewLifecycleOwner, Observer{
            restListAdapter.updateRestList(it)
        })
    }
}