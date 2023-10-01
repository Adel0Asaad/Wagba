package com.example.wagba.ui.fragments.body

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.ui.activities.LoggedActivity
import com.example.wagba.R
import com.example.wagba.data.models.UserViewModel
import com.example.wagba.data.models.factories.UserViewModelFactory
import com.example.wagba.databinding.FragmentAuthBinding
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory
        get(){
            return UserViewModelFactory(requireActivity().application)
        }

    private val fAuth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener{
            login()
        }
        binding.button2.setOnClickListener{
            signup()
        }

        binding.editTextTextEmailAddress.filters = arrayOf(object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                return source?.subSequence(start, end)
                    ?.replace(Regex("[^A-Za-z0-9 @.]"), "")
            }
        })
    }

    private fun localCheckAuthInput(mUser: String) : Boolean {
        if (mUser.indexOf('@') == -1){
            Log.d("{@} & E-mail rule ", "Accepted{No @ input, assumed @eng.asu.edu.eg}")
            if(mUser.lastIndex == 6){ //01X3456
                Log.d("Username rule ", "Accepted")
                Log.d("Username is ", mUser)
                return true
            }else{
                Log.d("Username rule ", "Denied{No username}")
            }
        }else if(mUser.indexOf('@') == mUser.lastIndexOf('@')){
            Log.d("{@} rule: ", "Accepted")
            if(mUser.substring(mUser.indexOf('@')) == "@eng.asu.edu.eg"){
                Log.d("E-mail rule ", "Accepted")
                if(mUser.indexOf('@') == 7){ //01x3456@ @ = 7
                    Log.d("Username rule ", "Accepted")
                    Log.d("Username is ", mUser.substring(0, mUser.indexOf('@')))
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

    private fun signup(){
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            replace<SignupFragment>(R.id.fragC2)
            addToBackStack("auth_OG")
        }
    }

    private fun login(){
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        var validInput: Boolean

        var mUser = binding.editTextTextEmailAddress.text.toString()
        val mPass = binding.editTextTextPassword.text.toString()

        validInput = localCheckAuthInput(mUser)

        if(mUser.indexOf('@') == -1){
            mUser += "@eng.asu.edu.eg"
        }

        if(validInput){
            fAuth.signInWithEmailAndPassword(mUser, mPass).addOnCompleteListener {
                try {
                    it.result
                    if (it.isSuccessful){
                        val intent = Intent(activity, LoggedActivity::class.java)
                        intent.putExtra("haha", "hahaData")
                        activity?.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                    }else if (it.isCanceled){
                        Log.d("LoginFailure", "Your login request failed, idk why so.... sorry?")
                        Toast.makeText(context, "Your login request failed, idk why so.... sorry?", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: RuntimeExecutionException){
                    Log.d("LOGIN FAILED:", "${e.message?.substring(e.message?.lastIndexOf(':')
                        ?.plus(1) ?: 0)}")
                }
            }
        }
    }
}