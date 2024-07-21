package com.julien.bankmanagement.adapters.transfers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.julien.bankmanagement.database.entities.TransferEntity
import com.julien.bankmanagement.databinding.TransferItemBinding
import com.julien.bankmanagement.utils.Utils

class TransfersViewHolder(
    itemView: View,
    private val binding: TransferItemBinding
): RecyclerView.ViewHolder(itemView) {


    fun bind(transfer: TransferEntity){

        binding.txtTransferAmount.text = transfer.amount.toString()

        binding.txtTransferDate.text = Utils.convertTimestampToTime(transfer.modifiedOn.toLong())

    }

}