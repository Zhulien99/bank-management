package com.julien.bankmanagement.adapters.home

import android.os.Build
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.julien.bankmanagement.R
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.databinding.AccountItemBinding
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils

class AccountsViewHolder(
    itemView: View,
    private val binding: AccountItemBinding,
    private val itemListener: OnAccountClickListener
): RecyclerView.ViewHolder(itemView) {


    fun bind(account: BankAccountEntity){
        binding.txtAccountName.text = account.name
        binding.txtIBAN.text = account.iBAN
        binding.txtTimeModified.text = account.modifiedOn?.toLong()?.let { Utils.convertTimestampToTime(it) }
        binding.txtAccountAmount.text = account.availableAmount.toString()

        if (account.availableAmount < 0) {
            // Set the text color to red if the amount is negative
            binding.txtAccountAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
        } else {
            // Optionally, set the text color to a default color if the amount is not negative
            binding.txtAccountAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
        }

        if (account.status == Constants.STATUS_ACTIVE){
            binding.status.setImageResource(R.drawable.check)
            binding.imgReadStatus.setImageResource(R.drawable.check)
        }else {
            binding.status.setImageResource(R.drawable.frozen)
            binding.imgReadStatus.setImageResource(R.drawable.frozen)
        }

        binding.changeStatusHolder.setOnClickListener {
            itemListener.changeStatus(account,bindingAdapterPosition)
        }

        binding.trashHolder.setOnClickListener {
            itemListener.delete(account)
        }

        binding.accountHolder.setOnClickListener {
            performOptionsMenuClick(account,it)
        }

    }

    fun recycleView(){
        binding.swipeLayout.close(true)
    }

    private fun performOptionsMenuClick(account: BankAccountEntity,view: View) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(itemView.context , view)

        // add the menu
        popupMenu.inflate(R.menu.account_options_menu)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }

        // implement on menu item click Listener
        popupMenu.setOnMenuItemClickListener { item ->
            itemListener.optionsMenuClick(account,item.itemId)
            false
        }
        popupMenu.show()
    }
}