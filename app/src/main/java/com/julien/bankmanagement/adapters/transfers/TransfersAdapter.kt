package com.julien.bankmanagement.adapters.transfers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.julien.bankmanagement.database.entities.TransferEntity
import com.julien.bankmanagement.databinding.TransferItemBinding

class TransfersAdapter(
    private val onItemClick: (TransferEntity) -> Unit
): PagingDataAdapter<TransferEntity, TransfersViewHolder>(DIFF_CALLBACK){

    private lateinit var binding: TransferItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransfersViewHolder {
        binding = TransferItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TransfersViewHolder(binding.root,binding)
    }

    override fun onBindViewHolder(holder: TransfersViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)

            holder.itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransferEntity>(){
            override fun areItemsTheSame(oldItem: TransferEntity, newItem: TransferEntity): Boolean {
                return oldItem.transactionID == newItem.transactionID
            }

            override fun areContentsTheSame(oldItem: TransferEntity, newItem: TransferEntity): Boolean {
                return oldItem == newItem
            }

        }
    }


}