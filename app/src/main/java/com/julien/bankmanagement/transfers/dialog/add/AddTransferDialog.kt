package com.julien.bankmanagement.transfers.dialog.add

import android.app.Dialog
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
import com.julien.bankmanagement.adapters.drop.DropDownAdapter
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.databinding.AddTransferDialogBinding
import com.julien.bankmanagement.utils.PreferenceUtils

class AddTransferDialog(
    private val currentAccountID: Int
): DialogFragment() {

    private lateinit var binding: AddTransferDialogBinding

    private val viewModel by viewModels<AddTransferViewModel>()

    private var selectedBeneficiaryAccountID: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AddTransferDialogBinding.inflate(layoutInflater)
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
        binding = AddTransferDialogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceUtils.getUser(requireContext())?.userID?.let { viewModel.getAccountsForUser(it,currentAccountID) }

        viewModel.accountList.observe(viewLifecycleOwner){
            setBeneficiaryAccountID(it)
        }

        viewModel.transferInsertionResult.observe(viewLifecycleOwner){
            if (it.success == 1){
                dismiss()
            }
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
        }

        initViews()

    }

    private fun initViews(){
        viewModel.getCurrentAccount(currentAccountID){
            binding.autoCompleteAccount.setText(getString(R.string.account_balance,it.name,it.availableAmount.toString()))
        }

        binding.btnAction.setOnClickListener {
            val amount = binding.editTextTransferAmount.text.toString()

            if (amount.isEmpty()){
                Toast.makeText(context,"Please input transfer amount",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.createTransfer(currentAccountID,
                    selectedBeneficiaryAccountID,binding.editTextTransferAmount.text.toString().toDouble())
            }

        }

        binding.txtCancel.setOnClickListener {
            dismiss()
        }
        binding.dialogClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setBeneficiaryAccountID(list: List<BankAccountEntity>){


        val arrayAdapter = context?.let { DropDownAdapter(it,R.layout.drop_down_item,list) }

        binding.autoCompleteAccountBeneficiary.setAdapter(arrayAdapter)

        binding.autoCompleteAccountBeneficiary.setOnItemClickListener { _, _, position, _ ->
            // Handle item selection here, e.g., update the text
            selectedBeneficiaryAccountID = list[position].accountID
        }
    }


    companion object {
        fun showTransfersDialog(fragmentManager: FragmentManager,currentAccountID: Int) {
            AddTransferDialog(currentAccountID).show(fragmentManager, AddTransferDialog::class.java.simpleName)
        }

    }

}