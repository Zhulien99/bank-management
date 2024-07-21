package com.julien.bankmanagement.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.julien.bankmanagement.login.signIn.SignInFragment
import com.julien.bankmanagement.login.signUp.RegisterFragment

class LogInAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private var totalCount: Int) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignInFragment()
            1 -> RegisterFragment()
            else -> SignInFragment()
        }
    }
}