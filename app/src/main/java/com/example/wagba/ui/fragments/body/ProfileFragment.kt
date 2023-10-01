package com.example.wagba.ui.fragments.body

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.R
import com.example.wagba.data.models.UserViewModel
import com.example.wagba.data.models.factories.UserViewModelFactory
import com.example.wagba.databinding.FragmentProfileBinding
import com.example.wagba.ui.activities.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory
        get(){
            return UserViewModelFactory(requireActivity().application)
        }

    private val fAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        userViewModel.readAllData.observe(viewLifecycleOwner, Observer{

            if(it.isNotEmpty()){
                binding.profileNameText.text = it[0].name
                binding.profileEmailText.text = it[0].email
                binding.profilePhoneText.text = it[0].phone
                binding.profilePassText.text = it[0].pass
                binding.profileIdText.text = it[0].id
                Log.d("Logging profile data" +
                        "", "${it[0].name} // ${it[0].email} // ${it[0].phone} // ${it[0].pass} // ${it[0].id}")
            }else{
                Toast.makeText(context, "LMFAO", Toast.LENGTH_SHORT).show()
            }
        })

        userViewModel.printData()

        binding.profileBtnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        userViewModel.clearUsers()
        Toast.makeText(context, "WHY HAST THOU SUMMONED THINE ELDERS", Toast.LENGTH_SHORT).show()
        fAuth.signOut()
        startActivity(Intent(activity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}