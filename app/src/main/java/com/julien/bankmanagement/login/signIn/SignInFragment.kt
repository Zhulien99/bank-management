package com.julien.bankmanagement.login.signIn

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.julien.bankmanagement.R
import com.julien.bankmanagement.databinding.FragmentLogInBinding
import com.julien.bankmanagement.login.LoginViewModel
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils
import com.julien.bankmanagement.utils.Utils.gone
import com.julien.bankmanagement.utils.Utils.show

class SignInFragment: Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private var isShowPassword = false

    private val viewModel by viewModels<LoginViewModel>(
        ownerProducer = { requireActivity() }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners(){

        val colorStateListPink: ColorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.real_pink))
        val colorStateListGrey: ColorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.grey))

        binding.imgShowPassword.setOnClickListener {
            isShowPassword = !isShowPassword
            binding.imgShowPassword.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    if (isShowPassword) R.drawable.eye_slash else R.drawable.eye
                )
            )

            // Toggle between visible and password input types
            binding.etPassword.inputType =
                if (isShowPassword) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            // Move cursor to the end of the text
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }

        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()){
                    binding.txtEnterUsername.gone()
                    binding.usernameHolder.background = ContextCompat.getDrawable(requireContext(),
                        R.drawable.large_corner_background)
                    binding.imgUser.imageTintList = colorStateListGrey
                    binding.etUsername.setHintTextColor(colorStateListGrey)
                }
            }
        })


        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()){
                    binding.txtEnterPassword.gone()
                    binding.passwordHolder.background = ContextCompat.getDrawable(requireContext(),
                        R.drawable.large_corner_background)
                    binding.imgLock.imageTintList = colorStateListGrey
                    binding.etPassword.setHintTextColor(colorStateListGrey)
                }
            }
        })

        binding.btnLogin.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()


            if (username.isEmpty()) {
                binding.txtEnterUsername.show()
                binding.usernameHolder.background = ContextCompat.getDrawable(requireContext(), R.drawable.corner_background_pink)
                binding.imgUser.imageTintList = colorStateListPink
                binding.etUsername.setHintTextColor(colorStateListPink)

            }

            if (password.isEmpty()){
                binding.txtEnterPassword.show()
                binding.passwordHolder.background = ContextCompat.getDrawable(requireContext(), R.drawable.corner_background_pink)
                binding.imgLock.imageTintList = colorStateListPink
                binding.etPassword.setHintTextColor(colorStateListPink)
            }


            if (username.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(username,password)
            }

            Utils.hideSoftKeyboard(this)
        }

        binding.signUpHolder.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

            viewPager?.setCurrentItem(Constants.REGISTER_FRAGMENT, true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}