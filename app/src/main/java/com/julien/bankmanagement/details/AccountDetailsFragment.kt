package com.julien.bankmanagement.details

import android.content.res.ColorStateList
import android.net.http.UrlRequest.Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.julien.bankmanagement.R
import com.julien.bankmanagement.base.BaseViewModelFragment
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.databinding.FragmentAccountDetailsBinding
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils
import com.julien.bankmanagement.utils.Utils.show

class AccountDetailsFragment: BaseViewModelFragment() {

    private var _binding: FragmentAccountDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<AccountDetailsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentAccount(AccountDetailsFragmentArgs.fromBundle(requireArguments()).accountID)

        viewModel.viewState.observe(viewLifecycleOwner){ bankAccount ->
            if (bankAccount != null) {
                initViews(bankAccount)
                initSave(bankAccount)
            }
        }

        viewModel.accountUpdateResult.observe(viewLifecycleOwner){
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
        }

        binding.imgBackBtn.setOnClickListener {
            navigateBack()
        }
    }

    private fun initViews(bankAccountEntity: BankAccountEntity){
        binding.txtIBAN.text = bankAccountEntity.iBAN
        binding.etAccountName.setText(bankAccountEntity.name)
        binding.etAvailableAmount.setText(bankAccountEntity.availableAmount.toString())
        binding.txtCreatedOn.text = Utils.convertTimestampToTime(bankAccountEntity.createdOn.toLong())
        binding.txtModifiedOn.text = bankAccountEntity.modifiedOn?.toLong()
            ?.let { Utils.convertTimestampToTime(it) }

        binding.switchStatus.isChecked = bankAccountEntity.status == Constants.STATUS_ACTIVE

        binding.txtStatus.text = bankAccountEntity.status

        binding.switchStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.txtStatus.text = if (isChecked) Constants.STATUS_ACTIVE else Constants.STATUS_FROZEN
        }
    }

    private fun initSave(account: BankAccountEntity){
        val colorStateListPink: ColorStateList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(),
                R.color.real_pink))


        binding.btnAction.setOnClickListener {
            val accountName = binding.etAccountName.text.toString()
            val amount = binding.etAvailableAmount.text.toString()
            val status = if (binding.switchStatus.isChecked) Constants.STATUS_ACTIVE else Constants.STATUS_FROZEN

            if (accountName.isEmpty()){
                handleValidationError(binding.etAccountName,binding.etAccountName,colorStateListPink)
            }

            if (amount.isEmpty()){
                handleValidationError(binding.etAvailableAmount,binding.etAvailableAmount,colorStateListPink)
            }

            if (accountName.isNotEmpty() && amount.isNotEmpty()){

                viewModel.updateAccount(
                    accountID = account.accountID,
                    userID = account.userID,
                    name = accountName,
                    iBAN = account.iBAN,
                    status = status,
                    availableAmount = amount.toDouble(),
                    createdOn = account.createdOn,
                    modifiedOn = (System.currentTimeMillis() / 1000L).toString()
                )
            }

        }
    }

    // Add this method to handle validation errors
    private fun handleValidationError(
        editText: EditText,
        holderView: View,
        colorStateList: ColorStateList
    ) {
        holderView.background = ContextCompat.getDrawable(requireContext(), R.drawable.corner_background_pink)
        editText.setHintTextColor(colorStateList)
    }

}