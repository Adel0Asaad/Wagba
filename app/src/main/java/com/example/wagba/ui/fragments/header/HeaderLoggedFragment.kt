package com.example.wagba.ui.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wagba.R
import com.example.wagba.databinding.FragmentHeaderLoggedBinding

class HeaderLoggedFragment : Fragment(R.layout.fragment_header_logged){

    private var _binding: FragmentHeaderLoggedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHeaderLoggedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.drawerBtn.setOnClickListener {
            openDrawer()
        }
    }

    private fun openDrawer(){
        activity?.findViewById<DrawerLayout>(R.id.drawer_layout)!!.open()
    }


}
