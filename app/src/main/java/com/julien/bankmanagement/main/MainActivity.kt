package com.julien.bankmanagement.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.julien.bankmanagement.R
import com.julien.bankmanagement.databinding.ActivityMainBinding
import com.julien.bankmanagement.utils.Utils.gone
import com.julien.bankmanagement.utils.Utils.show

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavVisibility(destination)
        }
    }

    private fun updateBottomNavVisibility(destination: NavDestination){

        when(destination.id){
            R.id.transfersFragment , R.id.accountDetailsFragment-> hideBottomNav()
            else -> showBottomNav()
        }
    }

    private fun showBottomNav(){
        binding.bottomNavigationView.show()
    }

    private fun hideBottomNav(){
        binding.bottomNavigationView.gone()
    }


}