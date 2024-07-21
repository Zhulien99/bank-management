package com.julien.bankmanagement.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.julien.bankmanagement.main.MainActivity
import com.julien.bankmanagement.adapters.LogInAdapter
import com.julien.bankmanagement.databinding.ActivityLoginBinding
import com.julien.bankmanagement.utils.PreferenceUtils

class LoginActivity: AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForLoggedUser()

        viewModel.viewLogin.observe(this){ userEntity ->

            if (userEntity != null) {
                PreferenceUtils.saveUser(userEntity, this)
                navigateToMain(this)
            }else Toast.makeText(this,"User does not exist!", Toast.LENGTH_SHORT).show()

        }

        binding.viewPager.adapter = LogInAdapter(supportFragmentManager,lifecycle,2)

        binding.viewPager.isUserInputEnabled = false
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun navigateToMain(context: Context?) {
        val intent = Intent(context, MainActivity::class.java)
        context?.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    private fun checkForLoggedUser(){
        val loginResponse = PreferenceUtils.getUser(this)
        if (loginResponse != null ){
            navigateToMain(this)
        }
    }
}