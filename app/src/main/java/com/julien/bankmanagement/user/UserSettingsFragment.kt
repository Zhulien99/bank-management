package com.julien.bankmanagement.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.julien.bankmanagement.base.BaseViewModelFragment
import com.julien.bankmanagement.databinding.FragmentUserSettingsBinding
import com.julien.bankmanagement.login.LoginActivity
import com.julien.bankmanagement.utils.PreferenceUtils

class UserSettingsFragment: BaseViewModelFragment() {

    private var _binding: FragmentUserSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }


    private fun initViews(){
        val user = PreferenceUtils.getUser(requireContext())
        binding.apply {
            txtID.text = user?.userID.toString()
            txtAccountName.text = user?.username
            txtEmail.text = user?.email
        }

        binding.btnAction.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin(){

        val loginIntent = Intent(activity, LoginActivity::class.java)
        loginIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK


        PreferenceUtils.clearPreferences(requireContext())

        activity?.startActivity(loginIntent)
        activity?.finish()
    }
}