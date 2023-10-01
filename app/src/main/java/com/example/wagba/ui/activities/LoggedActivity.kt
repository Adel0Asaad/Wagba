package com.example.wagba.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.R
import com.example.wagba.data.models.UserViewModel
import com.example.wagba.data.models.factories.UserViewModelFactory
import com.example.wagba.databinding.ActivityLoggedBinding
import com.example.wagba.ui.fragments.body.OrderHistoryFragment
import com.example.wagba.ui.fragments.body.ProfileFragment
import com.example.wagba.ui.fragments.body.RestListFragment
import com.example.wagba.ui.fragments.body.TrackFragment
import com.example.wagba.ui.fragments.header.HeaderLoggedFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoggedActivity : AppCompatActivity(R.layout.activity_logged) {

    private var _binding: ActivityLoggedBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory
        get(){
            return UserViewModelFactory(application)
        }

    private val fAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)

                replace<HeaderLoggedFragment>(R.id.fragC1, "h_OG")
                replace<RestListFragment>(R.id.fragC2, "rl_OG")
            }
        }
        _binding = ActivityLoggedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
        userViewModel.currUser.observe(this, Observer{
            userViewModel.addUser(it!!)
        })

        binding.navView.itemIconTintList = null

        binding.navView.menu[0].setOnMenuItemClickListener {
            if (supportFragmentManager.findFragmentByTag("p_OG") in supportFragmentManager.fragments){
                Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show()
                binding.drawerLayout.close()
                return@setOnMenuItemClickListener true
            }
            gotoProfile()
            return@setOnMenuItemClickListener true

        }
        binding.navView.menu[1].setOnMenuItemClickListener {
            if (supportFragmentManager.findFragmentByTag("to_OG") in supportFragmentManager.fragments){
                Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show()
                binding.drawerLayout.close()
                return@setOnMenuItemClickListener true
            }
            trackOrder()
            return@setOnMenuItemClickListener true
        }
        binding.navView.menu[2].setOnMenuItemClickListener {
            if (supportFragmentManager.findFragmentByTag("oh_OG") in supportFragmentManager.fragments){
                Toast.makeText(this, "You're already here!", Toast.LENGTH_SHORT).show()
                binding.drawerLayout.close()
                return@setOnMenuItemClickListener true
            }
            gotoOrderHistory()
            return@setOnMenuItemClickListener true
        }
        binding.navView.menu[3].setOnMenuItemClickListener {
            logout()
            return@setOnMenuItemClickListener true
        }

        userViewModel.currUser.observe(this, Observer {
            Log.d("Printing current user!", it.toString())
        })
        userViewModel.getCurrUser()
    }

    private fun gotoOrderHistory(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<OrderHistoryFragment>(R.id.fragC2, "oh_OG")
            addToBackStack("rl_OG")
        }
        binding.drawerLayout.close()
    }

    private fun gotoProfile(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ProfileFragment>(R.id.fragC2, "p_OG")
            addToBackStack("rl_OG")
        }
        binding.drawerLayout.close()
    }

    private fun trackOrder(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<TrackFragment>(R.id.fragC2, "to_OG")
            addToBackStack("rl_OG")
        }
        binding.drawerLayout.close()
    }
    
    private fun logout(){
        userViewModel.clearUsers()
        fAuth.signOut()
        startActivity(Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
