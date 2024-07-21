package com.julien.bankmanagement.base

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

abstract class BaseViewModelFragment: Fragment() {

    protected fun navigateTo(@IdRes resId: Int? = null, action: NavDirections? = null) {
        view ?: return

        if (resId != null) {
            try {
                Navigation.findNavController(requireView()).navigate(resId)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        if (action != null) {
            try {
                findNavController().navigate(action)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    protected fun navigateBack() {
        view ?: return

        try {
            Navigation.findNavController(requireView()).navigateUp()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}