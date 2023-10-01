package com.example.wagba.ui.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wagba.R
import com.example.wagba.databinding.FragmentHeaderBinding

class HeaderFragment : Fragment(R.layout.fragment_header){

    private var _binding: FragmentHeaderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeaderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}
