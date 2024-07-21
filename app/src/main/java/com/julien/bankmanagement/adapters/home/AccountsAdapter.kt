package com.julien.bankmanagement.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.databinding.AccountItemBinding

class AccountsAdapter(
    private val itemListener: OnAccountClickListener
) : PagingDataAdapter<BankAccountEntity,AccountsViewHolder>(DIFF_CALLBACK) {

    private lateinit var binding: AccountItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        binding = AccountItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AccountsViewHolder(binding.root,binding,itemListener)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }

    override fun onViewRecycled(holder: AccountsViewHolder) {
        holder.recycleView()
        super.onViewRecycled(holder)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BankAccountEntity>(){
            override fun areItemsTheSame(oldItem: BankAccountEntity, newItem: BankAccountEntity): Boolean {
                return oldItem.accountID == newItem.accountID
            }

            override fun areContentsTheSame(oldItem: BankAccountEntity, newItem: BankAccountEntity): Boolean {
                return oldItem == newItem || oldItem.status == newItem.status
            }

        }
    }


}

interface OnAccountClickListener{

    fun delete(account: BankAccountEntity)

    fun changeStatus(account: BankAccountEntity,position: Int)

    fun optionsMenuClick(account: BankAccountEntity,itemID: Int)

}