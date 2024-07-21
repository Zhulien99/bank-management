package com.julien.bankmanagement.login.signUp

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.julien.bankmanagement.R
import com.julien.bankmanagement.databinding.FragmentSignUpBinding
import com.julien.bankmanagement.login.LoginViewModel
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils
import com.julien.bankmanagement.utils.Utils.gone
import com.julien.bankmanagement.utils.Utils.show
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterFragment: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val calendar: Calendar = Calendar.getInstance()

    private val viewModel by viewModels<LoginViewModel>(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
    }

    private fun initViews(){

        binding.etEmail.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etEmail.clearFocus()
                binding.birthdayHolder.requestFocus()
                binding.etBirthday.performClick()
                true // Consume the action
            } else {
                false // Allow other actions to be handled
            }
        }

    }


    private fun setListeners(){

        val colorStateListPink: ColorStateList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(),
                R.color.real_pink))
        val colorStateListGrey: ColorStateList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(),
                R.color.grey))



        handleTextChanges(binding.etUsername, binding.txtEnterUsername, binding.usernameHolder, binding.imgUser, colorStateListGrey)
        handleTextChanges(binding.etPassword, binding.txtEnterPassword, binding.passwordHolder, binding.imgEye, colorStateListGrey)
        handleTextChanges(binding.etEmail, binding.txtEnterEmail, binding.emailHolder, binding.imgLock, colorStateListGrey)
        handleTextChanges(binding.etBirthday, binding.txtEnterBirthday, binding.birthdayHolder, binding.imgCalendar, colorStateListGrey)

        binding.btnSignUp.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val email = binding.etEmail.text.toString()
            val birthday = binding.etBirthday.text.toString()


            if (username.isEmpty()) {
                handleValidationError(binding.etUsername, binding.txtEnterUsername, binding.usernameHolder, binding.imgUser, colorStateListPink)
            }
            if (password.isEmpty()) {
                handleValidationError(binding.etPassword, binding.txtEnterPassword, binding.passwordHolder, binding.imgEye, colorStateListPink)
            }
            if (email.isEmpty()) {
                handleValidationError(binding.etEmail, binding.txtEnterEmail, binding.emailHolder, binding.imgLock, colorStateListPink)
                binding.txtEnterEmail.text = getString(R.string.required)
            }else if(!Utils.isEmailValid(email)){
                handleValidationError(binding.etEmail, binding.txtEnterEmail, binding.emailHolder, binding.imgLock, colorStateListPink)
                binding.txtEnterEmail.text = getString(R.string.invalid_email_address_provided)
            }

            if (birthday.isEmpty()) {
                handleValidationError(binding.etBirthday, binding.txtEnterBirthday, binding.birthdayHolder, binding.imgCalendar, colorStateListPink)
            }


            if (username.isNotEmpty()&& password.isNotEmpty() && email.isNotEmpty() && Utils.isEmailValid(email) && birthday.isNotEmpty() ){
                viewModel.register(username,password,email,birthday)
            }

            Utils.hideSoftKeyboard(this)


        }

        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }



        binding.etBirthday.setOnClickListener {
            context?.let { context ->
                val calendar = Calendar.getInstance()

                // Set the maximum date to the current date minus 18 years
                val maxDate = Calendar.getInstance().apply {
                    add(Calendar.YEAR, -18)
                }

                val datePickerDialog = DatePickerDialog(
                    context,
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                // Set the date picker dialog to spinner mode

                // Set the maximum date
                datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

                datePickerDialog.show()
            }

        }

        binding.loginHolder.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

            viewPager?.setCurrentItem(Constants.SIGN_IN_FRAGMENT, true)
        }

    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etBirthday.setText(dateFormat.format(calendar.time))
    }


    private fun handleTextChanges(
        editText: EditText,
        hintTextView: TextView,
        holderView: View,
        iconImageView: ImageView?,
        colorStateList: ColorStateList
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    hintTextView.gone()
                    holderView.background = ContextCompat.getDrawable(requireContext(), R.drawable.large_corner_background)
                    iconImageView?.imageTintList = colorStateList
                    editText.setHintTextColor(colorStateList)
                }
            }
        })
    }

    private fun enableRegisterButton(isLoading: Boolean){
        binding.btnSignUp.isEnabled = !isLoading

        if (isLoading){
            binding.btnSignUp.text = ""
            binding.progressIndicator.show()
        }else {
            binding.btnSignUp.text = getString(R.string.login)
            binding.progressIndicator.gone()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Add this method to handle validation errors
    private fun handleValidationError(
        editText: EditText,
        hintTextView: TextView,
        holderView: View,
        iconImageView: ImageView?,
        colorStateList: ColorStateList
    ) {
        hintTextView.show()
        holderView.background = ContextCompat.getDrawable(requireContext(), R.drawable.corner_background_pink)
        iconImageView?.imageTintList = colorStateList
        editText.setHintTextColor(colorStateList)
    }

}
