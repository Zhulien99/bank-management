package com.julien.bankmanagement.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object Utils {

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.INVISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun hideSoftKeyboard(fragment: Fragment?) {
        if (fragment?.activity != null) {
            try {
                val inputMethodManager = fragment.activity?.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(fragment.requireView().windowToken, 0)
            } catch (e: Exception) {
                // Handle any exceptions, log or ignore as needed
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }

    fun convertTimestampToTime(timestamp: Long): String  {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        return sdf.format(date)
    }


}