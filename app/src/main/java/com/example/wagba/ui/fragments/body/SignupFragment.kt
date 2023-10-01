package com.example.wagba.ui.fragments.body

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.R
import com.example.wagba.data.internal.User
import com.example.wagba.data.models.UserViewModel
import com.example.wagba.data.models.factories.UserViewModelFactory
import com.example.wagba.databinding.FragmentSignupBinding
import com.example.wagba.ui.activities.LoggedActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val userViewModelFactory: UserViewModelFactory
        get(){
            return UserViewModelFactory(requireActivity().application)
        }
    private val userViewModel: UserViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.currUser.observe(viewLifecycleOwner, Observer {
            if (it != null){
                userViewModel
                userViewModel.addUser(it)
                Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                activity?.startActivity(Intent(activity, LoggedActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        })

        //signup button
        binding.button2.setOnClickListener {
            signup()
        }
    }

    private fun signup(){
        val name: String = binding.signupNameText.text.toString()
        val phone: String = binding.signupPhoneText.text.toString()
        var email: String = binding.signupEmailText.text.toString()
        val pass: String = binding.signupPasswordText.text.toString()
        if (localCheckAuthInput(email)){
            if(email.indexOf('@') == -1){
                email += "@eng.asu.edu.eg"
            }

            userViewModel.signup(
                    name,
                    pass,
                    email,
                    phone
            )
        }
    }

    private fun localCheckAuthInput(email: String) : Boolean {
        if (email.indexOf('@') == -1){
            Log.d("{@} & E-mail rule ", "Accepted{No @ input, assumed @eng.asu.edu.eg}")
            if(email.lastIndex == 6){ //01X3456
                Log.d("Username rule ", "Accepted")
                Log.d("Username is ", email)
                return true
            }else{
                Log.d("Username rule ", "Denied{No username}")
            }
        }else if(email.indexOf('@') == email.lastIndexOf('@')){
            Log.d("{@} rule: ", "Accepted")
            if(email.substring(email.indexOf('@')) == "@eng.asu.edu.eg"){
                Log.d("E-mail rule ", "Accepted")
                if(email.indexOf('@') == 7){ //01x3456@ @ = 7
                    Log.d("Username rule ", "Accepted")
                    Log.d("Username is ", email.substring(0, email.indexOf('@')))
                    return true
                }else{
                    Log.d("Username rule ", "Denied{No username}")
                }
            }else{
                Log.d("E-mail rule ", "Denied{Wrong e-mail}")
            }
        }else {
            Log.d("{@} rule ", "Denied{Multiple @'s}")
        }
        return false
    }

}