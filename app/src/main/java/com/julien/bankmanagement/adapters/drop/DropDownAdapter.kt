package com.julien.bankmanagement.adapters.drop

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.julien.bankmanagement.R
import com.julien.bankmanagement.database.entities.BankAccountEntity

class DropDownAdapter(context: Context, resource: Int, objects: List<BankAccountEntity>) :
    ArrayAdapter<BankAccountEntity>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position)
        view.findViewById<TextView>(R.id.txtAccount).text = view.context.getString(R.string.account_balance,item?.name,item?.availableAmount.toString())
        return view
    }

}