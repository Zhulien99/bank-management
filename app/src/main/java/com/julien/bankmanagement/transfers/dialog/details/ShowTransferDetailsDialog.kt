package com.julien.bankmanagement.transfers.dialog.details

import android.app.Dialog
import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.julien.bankmanagement.R
import com.julien.bankmanagement.database.entities.TransferEntity
import com.julien.bankmanagement.databinding.DialogTransferDetailsBinding
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils

class ShowTransferDetailsDialog(
    private val transfer: TransferEntity
): DialogFragment() {

    private lateinit var binding: DialogTransferDetailsBinding

    private val viewModel by viewModels<TransferDetailsViewModel>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogTransferDetailsBinding.inflate(layoutInflater)
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
        binding = DialogTransferDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews(){
        binding.dialogClose.setOnClickListener {
            dismiss()
        }
        binding.apply {
            txtType.text = transfer.type
            txtAmount.text = transfer.amount.toString()
             viewModel.getAccountName(transfer.accountID){
                txtAccountName.text = it
            }
            viewModel.getAccountName(transfer.beneficiaryAccountID){
                txtBeneficiaryAccount.text = it
            }

            txtCreatedOn.text = Utils.convertTimestampToTime(transfer.createdOn.toLong())
            txtModifiedOn.text = Utils.convertTimestampToTime(transfer.modifiedOn.toLong())
        }


    }

    companion object {
        fun showTransferDetailsDialog(fragmentManager: FragmentManager, transfer: TransferEntity) {
            ShowTransferDetailsDialog(transfer).show(fragmentManager, ShowTransferDetailsDialog::class.java.simpleName)
        }

    }


}