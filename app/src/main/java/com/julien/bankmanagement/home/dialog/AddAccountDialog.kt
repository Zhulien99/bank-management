package com.julien.bankmanagement.home.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.julien.bankmanagement.R
import com.julien.bankmanagement.databinding.AddAccountDialogBinding

class AddAccountDialog: DialogFragment() {

    private lateinit var binding: AddAccountDialogBinding

    private val viewModel by viewModels<AddAccountViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AddAccountDialogBinding.inflate(layoutInflater)
        // Create and configure your dialog
        val mBuilder = AlertDialog.Builder(requireContext()).setView(binding.root)

        //show dialog
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(true)
        mAlertDialog.setCanceledOnTouchOutside(true)

        mAlertDialog.window!!.setBackgroundDrawable(
            AppCompatResources.getDrawable(requireContext(),
                R.drawable.rounded_corners_view))

        mAlertDialog?.setCancelable(true)
        mAlertDialog?.setCanceledOnTouchOutside(true)

        return mAlertDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddAccountDialogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.accountInsertionResult.observe(viewLifecycleOwner){
            if (it.success == 1) dismiss()
            else Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
        }

        binding.btnAction.setOnClickListener {
            val accountName = binding.editTextAccount.text.toString()
            val initialBalance = binding.editTextBalance.text.toString()

            if (accountName.isNotEmpty() && initialBalance.isNotEmpty()){
                viewModel.insertBankAccount(accountName,initialBalance.toDouble())
            }
        }

        binding.dialogClose.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        fun showAddAccountDialog(fragmentManager: FragmentManager) {
            AddAccountDialog().show(fragmentManager, AddAccountDialog::class.java.simpleName)
        }

    }



}