package com.example.wagba.ui.fragments.body

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R
import com.example.wagba.data.adapters.MenuItemAdapter
import com.example.wagba.data.internal.MenuItem
import com.example.wagba.data.internal.RestaurantLite
import com.example.wagba.data.models.MenuItemViewModel
import com.example.wagba.data.models.OrderItemViewModel
import com.example.wagba.data.models.factories.MenuItemViewModelFactory
import com.example.wagba.data.models.factories.OrderItemViewModelFactory
import com.example.wagba.databinding.FragmentRest2Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class RestFragment2 : Fragment(R.layout.fragment_rest2) {

    private var _binding: FragmentRest2Binding? = null
    private val binding get() = _binding!!

    private lateinit var orderItemViewModel: OrderItemViewModel
    private lateinit var viewModel: MenuItemViewModel
    private lateinit var menuItemRecyclerView: RecyclerView
    lateinit var menuItemAdapter: MenuItemAdapter
    private val viewModelFactory: MenuItemViewModelFactory
        get() {
            return MenuItemViewModelFactory(arguments?.get(ARG_PARAM2) as Int + 1)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRest2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("mAB", "Saving bta3a")
        super.onSaveInstanceState(outState)
        val frameVisibility: Boolean = binding.frameShowCart.visibility != View.GONE
        outState.putBoolean("frame_visibility", frameVisibility)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillMenuItem()
        checkCart()

        binding.btnShowCart.setOnClickListener{
            showCartClick()
        }
    }

    private fun showCartClick(){
        binding.fragCCart.visibility = View.VISIBLE

        Log.d("RestFrag", "Showing Cart! with: " +
                "${arguments?.get(ARG_PARAM1) as String} " +
                "/ ${arguments?.get(ARG_PARAM2) as Int} " +
                "/ ${arguments?.get(ARG_PARAM3) as String}")
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            val mFrag = CartFragment.newInstance(
                arguments?.get(ARG_PARAM1) as String,
                arguments?.get(ARG_PARAM2) as Int,
                arguments?.get(ARG_PARAM3) as String
            )
            replace(R.id.frag_c_cart, mFrag, "cf_OG")
            Log.d("RestFrag", "Replaced with $mFrag")
            addToBackStack("rf_OG")
        }
    }

    private fun fillMenuItem(){
        menuItemRecyclerView = binding.recyclerMenuItems
        menuItemRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )

        menuItemAdapter = MenuItemAdapter(object: MenuItemAdapter.MenuItemClickListener{
            override fun onAddClick(mHolder: MenuItemAdapter.ViewHolder, mItem: MenuItem) {
                orderItemViewModel.postOrderItem(
                    mItem,
                    RestaurantLite(
                        arguments?.get(ARG_PARAM1) as String,
                        arguments?.get(ARG_PARAM3) as String,
                        arguments?.get(ARG_PARAM2) as Int
                    ),
                    context!!
                )
            }

        })
        menuItemRecyclerView.adapter = menuItemAdapter
        viewModel = ViewModelProvider(this, viewModelFactory)[MenuItemViewModel::class.java]
        viewModel.allMenuItems.observe(viewLifecycleOwner, Observer{
            menuItemAdapter.updateMenuItemList(it)
        })
    }

    private fun checkCart(){
        val viewModelFactory = OrderItemViewModelFactory()
        orderItemViewModel = ViewModelProvider(this, viewModelFactory)[OrderItemViewModel::class.java]

        orderItemViewModel.currOrder.observe(viewLifecycleOwner, Observer{
            if (it.orderItemList?.isNotEmpty() == true){
                binding.frameShowCart.visibility = View.VISIBLE
            }else{
                binding.frameShowCart.visibility = View.GONE
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int, param3: String) =
            RestFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }


}