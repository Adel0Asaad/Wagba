package com.example.wagba.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.R
import com.example.wagba.data.models.UserViewModel
import com.example.wagba.data.models.factories.UserViewModelFactory
import com.example.wagba.databinding.ActivityMainBinding
import com.example.wagba.ui.fragments.body.AuthFragment
import com.example.wagba.ui.fragments.header.HeaderFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val fAuth = Firebase.auth

    private val userViewModelFactory: UserViewModelFactory
        get(){
            return UserViewModelFactory(application)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<HeaderFragment>(R.id.fragC1)
                replace<AuthFragment>(R.id.fragC2, "auth_OG")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val userViewModel: UserViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        val currentUser = fAuth.currentUser
        if(currentUser != null){
            userViewModel.getCurrUser()
            userViewModel.currUser.observe(this, Observer{
                userViewModel.clearUsers()
                userViewModel.addUser(it)
                Toast.makeText(this, "Session resumed..", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoggedActivity::class.java)
                startActivity(intent)
            })
        }
    }

}